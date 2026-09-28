#
# Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
#
# SPDX-License-Identifier: BSD-3-Clause-Clear
#
# Convert a qcom-ptool partitions.xml into a wic kickstart layout for a
# single-disk image (e.g. an SD card).
#
# The partitions.xml layout describes one storage device, possibly split in
# several physical partitions (eMMC boot areas). A removable card only has
# one, so every physical partition is folded into a single GPT: physical
# partition 0 keeps its order and the partitions of the other physical
# partitions go right before the ESP, keeping the OS partitions together.
# Without an ESP they go before the last partition when that one grows to
# fill the disk, or at the end otherwise.
#

import os
import xml.etree.ElementTree as ET

ESP_TYPE_GUID = "C12A7328-F81F-11D2-BA4B-00A0C93EC93B"

# GPT attribute bits set by qcom-ptool (see qcom_ptool/ptool.py)
_ATTRIBUTE_FLAGS = {
    "readonly": 60,
    "hidden": 62,
    "dontautomount": 63,
    "system": 0,
    "active": 50,
    "successful": 54,
    "unbootable": 55,
}
_ATTRIBUTE_FIELDS = {
    "triesremaining": 51,
    "priority": 48,
}

# sfdisk --part-attrs names for the bits that are not "GUID:<bit>"
_SFDISK_ATTRIBUTE_NAMES = {
    0: "RequiredPartition",
    1: "NoBlockIOProtocol",
    2: "LegacyBIOSBootable",
}


class PartitionsWksError(Exception):
    pass


def _parse_instructions(text):
    params = {}
    for line in (text or "").splitlines():
        line = line.strip()
        if "=" in line:
            key, value = line.split("=", 1)
            params[key.strip()] = value.strip()
    return params


def load_partitions_xml(path):
    """Return (parser instructions, list of partition lists per physical partition)."""
    root = ET.parse(path).getroot()
    params = _parse_instructions(root.findtext("parser_instructions"))
    physical = [[dict(p.attrib) for p in pp.findall("partition")]
                for pp in root.findall("physical_partition")]
    return params, physical


def flatten(params, physical):
    """Fold all physical partitions into a single ordered partition list."""
    sector_size = int(params.get("SECTOR_SIZE_IN_BYTES", "512"))
    if sector_size != 512:
        raise PartitionsWksError("only 512 byte sector layouts can be converted, got %d" % sector_size)
    if not physical or not physical[0]:
        raise PartitionsWksError("no partitions in physical partition 0")

    main = list(physical[0])
    extra = [p for pp in physical[1:] for p in pp if int(p.get("size_in_kb", "0")) > 0]

    grow = params.get("GROW_LAST_PARTITION_TO_FILL_DISK", "false").lower() == "true"
    esp = [i for i, p in enumerate(main) if p.get("type", "").upper() == ESP_TYPE_GUID]
    if esp:
        index = esp[0]
    elif grow:
        index = len(main) - 1
    else:
        index = len(main)
    partitions = main[:index] + extra + main[index:]

    labels = [p["label"] for p in partitions]
    duplicates = sorted({label for label in labels if labels.count(label) > 1})
    if duplicates:
        raise PartitionsWksError("duplicate partition labels: %s" % ", ".join(duplicates))

    return partitions, grow


def gpt_attributes(partition):
    """GPT attribute bits qcom-ptool writes for this partition."""
    attrs = 0
    for key, bit in _ATTRIBUTE_FLAGS.items():
        if partition.get(key, "false") == "true":
            attrs |= 1 << bit
    for key, shift in _ATTRIBUTE_FIELDS.items():
        attrs |= int(partition.get(key, "0")) << shift
    return attrs


def sfdisk_attributes(attrs):
    """Format GPT attribute bits for sfdisk --part-attrs."""
    names = []
    for bit in range(64):
        if not attrs & (1 << bit):
            continue
        if bit in _SFDISK_ATTRIBUTE_NAMES:
            names.append(_SFDISK_ATTRIBUTE_NAMES[bit])
        elif bit >= 48:
            names.append("GUID:%d" % bit)
        else:
            raise PartitionsWksError("reserved GPT attribute bit %d cannot be set" % bit)
    return ",".join(names)


def generate_wks(xml_path, file_dir):
    """Return (wks text, [(partition number, sfdisk attributes)]).

    Files named in the layout are taken from file_dir, which must hold them
    under the names qcom-ptool uses (the qcomflash directory does).
    """
    try:
        return _generate_wks(xml_path, file_dir)
    except (ET.ParseError, KeyError, ValueError) as e:
        raise PartitionsWksError("invalid layout %s: %s: %s" % (xml_path, type(e).__name__, e)) from e


def _generate_wks(xml_path, file_dir):
    params, physical = load_partitions_xml(xml_path)
    partitions, grow = flatten(params, physical)
    align = params.get("PERFORMANCE_BOUNDARY_IN_KB", "")
    if params.get("ALIGN_PARTITIONS_TO_PERFORMANCE_BOUNDARY", "false").lower() != "true":
        align = ""

    lines = ["bootloader --ptable gpt"]
    attributes = []
    for num, part in enumerate(partitions, start=1):
        last = num == len(partitions)
        label = part["label"]
        filename = part.get("filename", "")
        size_kb = int(part.get("size_in_kb", "0"))

        args = ["part", "--part-name", label, "--part-type", part["type"], "--fstype", "none"]
        if part.get("uniqueguid"):
            args += ["--uuid", part["uniqueguid"]]
        if align:
            args += ["--align", align]

        if filename:
            if part.get("sparse", "false") == "true":
                raise PartitionsWksError("partition %s uses a sparse image, not supported" % label)
            source = os.path.join(file_dir, filename)
            if not os.path.exists(source):
                raise PartitionsWksError("partition %s: %s not found" % (label, source))
            args += ["--source", "rawcopy", '--sourceparams="file=%s"' % source]
        else:
            args += ["--source", "empty"]

        # The partition that grows to fill the disk keeps the size of its
        # content, so the image fits any card and can be grown on the target.
        if not (last and grow and filename):
            if size_kb <= 0:
                raise PartitionsWksError("partition %s has no size" % label)
            args += ["--fixed-size", "%dK" % size_kb]

        lines.append(" ".join(args))

        attrs = gpt_attributes(part)
        if attrs:
            attributes.append((num, sfdisk_attributes(attrs)))

    return "\n".join(lines) + "\n", attributes
