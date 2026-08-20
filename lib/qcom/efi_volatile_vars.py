# Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
#
# SPDX-License-Identifier: BSD-3-Clause-Clear
#
# Builds an EBBR-style VolatileVars.bin for pre-seeding UEFI variables, in
# the format read by the firmware's UpdateVariableFromRTVolatileBin().
#
# Format (all little-endian):
#
# EFI_VARIABLE_FILE header (24 bytes):
#     UINT64 Reserved      (0)
#     UINT8  Magic[7]      "UbEfiVa"
#     UINT8  Revision      1
#     UINT32 Length        total file size
#     UINT32 Crc32         zlib crc32 over everything after the header
#
# EFI_VARIABLE_ENTRY, repeated, each 8-byte aligned (32-byte header + name + data):
#     UINT32   DataSize
#     UINT32   Attributes
#     UINT64   Reserved     (0)
#     EFI_GUID VendorGuid   (16 bytes)
#     CHAR16   Name[]       UCS-2, NUL-terminated
#     UINT8    Data[DataSize]
#     <pad to 8-byte alignment>

import struct
import uuid
import zlib

MAGIC = b"UbEfiVa"
REVISION = 1
ALIGNMENT = 8

EFI_VARIABLE_NON_VOLATILE = 0x00000001
EFI_VARIABLE_BOOTSERVICE_ACCESS = 0x00000002
EFI_VARIABLE_RUNTIME_ACCESS = 0x00000004

VENDOR_DTB_OVERLAYS_GUID = "882f8c2b-9646-435f-8de5-f208ff80c1bd"
VENDOR_DTB_OVERLAYS_ATTRIBUTES = (
    EFI_VARIABLE_NON_VOLATILE
    | EFI_VARIABLE_BOOTSERVICE_ACCESS
    | EFI_VARIABLE_RUNTIME_ACCESS
)


def build_entry(guid_str, name, data, attributes):
    guid_bytes = uuid.UUID(guid_str).bytes_le
    name_bytes = name.encode("utf-16-le") + b"\x00\x00"
    header = struct.pack("<IIQ", len(data), attributes, 0) + guid_bytes
    entry = header + name_bytes + data
    pad = (-len(entry)) % ALIGNMENT
    return entry + b"\x00" * pad


def build_volatile_vars(entries):
    """entries: list of dicts with keys guid, name, data (bytes), attributes"""
    body = b"".join(
        build_entry(e["guid"], e["name"], e["data"], e["attributes"])
        for e in entries
    )
    length = 24 + len(body)
    crc32 = zlib.crc32(body) & 0xFFFFFFFF
    header = struct.pack("<Q7sBII", 0, MAGIC, REVISION, length, crc32)
    return header + body


def build_vendor_dtb_volatile_vars(vendor_dtb_overlays):
    """Build a VolatileVars.bin containing only the VendorDtbOverlays entry."""
    entries = [
        {
            "guid": VENDOR_DTB_OVERLAYS_GUID,
            "name": "VendorDtbOverlays",
            "data": vendor_dtb_overlays.encode("utf-8"),
            "attributes": VENDOR_DTB_OVERLAYS_ATTRIBUTES,
        }
    ]
    return build_volatile_vars(entries)
