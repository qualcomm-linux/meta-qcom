#!/usr/bin/env python3
#
# Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
# SPDX-License-Identifier: BSD-3-Clause
#
# Split ELF (or MBN) loadables for the SPL FIT. Each "name:image:entry_rule:
# arch[:os]" spec gets one FIT image node per PT_LOAD segment, "<name>_1",
# "<name>_2", ..., loaded at the segment's physical address. The segment
# data goes to <outdir>/<name>_<n>.bin, the nodes to <outdir>/<name>.dtsi
# and their quoted, comma separated names to <outdir>/<name>.loadables, for
# the ITS to take in. The data paths are relative to the directory this is
# run from.

import argparse
import os
import struct
import sys

PT_LOAD = 1


def fail(msg):
    sys.exit("qcom-spl-fit-elf: %s" % msg)


def read_load_segments(path):
    with open(path, 'rb') as f:
        data = f.read()

    if data[:4] != b'\x7fELF':
        fail("%s is not an ELF" % path)

    ei_class, ei_data = data[4], data[5]
    if ei_class not in (1, 2):
        fail("%s: unknown ELF class %d" % (path, ei_class))
    endian = '<' if ei_data == 1 else '>'

    if ei_class == 2:
        e_entry, e_phoff = struct.unpack_from(endian + 'QQ', data, 0x18)
        e_phentsize, e_phnum = struct.unpack_from(endian + 'HH', data, 0x36)
        phdr = endian + 'IIQQQQQQ'
    else:
        e_entry, e_phoff = struct.unpack_from(endian + 'II', data, 0x18)
        e_phentsize, e_phnum = struct.unpack_from(endian + 'HH', data, 0x2a)
        phdr = endian + 'IIIIIIII'

    segments = []
    for i in range(e_phnum):
        fields = struct.unpack_from(phdr, data, e_phoff + i * e_phentsize)
        if ei_class == 2:
            p_type, _, p_offset, p_vaddr, p_paddr, p_filesz, p_memsz, _ = fields
        else:
            p_type, p_offset, p_vaddr, p_paddr, p_filesz, p_memsz, _, _ = fields
        # The MBN hash segment is not PT_LOAD and BSS-only segments have
        # nothing to load.
        if p_type != PT_LOAD or p_filesz == 0 or p_memsz == 0:
            continue
        segments.append({
            'data': data[p_offset:p_offset + p_filesz],
            'vaddr': p_vaddr,
            'paddr': p_paddr,
            'memsz': p_memsz,
        })

    if not segments:
        fail("%s has no PT_LOAD segments" % path)

    # e_entry is a virtual address; the FIT entry has to be physical.
    entry = e_entry
    for seg in segments:
        if seg['vaddr'] <= e_entry < seg['vaddr'] + seg['memsz']:
            entry = seg['paddr'] + (e_entry - seg['vaddr'])
            break

    return segments, entry


def cells(addr, address_cells):
    if address_cells == 1:
        if addr > 0xffffffff:
            fail("address 0x%x does not fit in one cell" % addr)
        return "0x%08X" % addr
    return "0x%08X 0x%08X" % (addr >> 32, addr & 0xffffffff)


def split(args, spec):
    fields = spec.split(':')
    if len(fields) not in (4, 5):
        fail("%s: expected name:image:entry_rule:arch[:os]" % spec)
    name, image, entry_rule, arch = fields[:4]
    os_name = fields[4] if len(fields) == 5 else ''
    if entry_rule not in ('first_only', 'first_and_last', 'own_load'):
        fail("%s: unknown entry rule %s" % (spec, entry_rule))

    segments, elf_entry = read_load_segments(image)
    nodes = []
    out = []
    for i, seg in enumerate(segments, 1):
        node = "%s_%d" % (name, i)
        path = os.path.join(args.outdir, node + ".bin")
        with open(path, 'wb') as f:
            f.write(seg['data'])

        entry = None
        if entry_rule == 'own_load':
            entry = seg['paddr']
        elif i == 1 or (entry_rule == 'first_and_last' and i == len(segments)):
            entry = elf_entry

        out += [
            "        %s {" % node,
            "            description = \"%s segment %d\";" % (name, i),
            "            data = /incbin/(\"%s\");" % path,
            "            type = \"firmware\";",
            "            arch = \"%s\";" % arch,
        ]
        # os describes the image the SPL jumps to, so only segments
        # carrying an entry get it.
        if os_name and entry is not None:
            out.append("            os = \"%s\";" % os_name)
        out.append("            load = <%s>;" % cells(seg['paddr'], args.address_cells))
        if entry is not None:
            out.append("            entry = <%s>;" % cells(entry, args.address_cells))
        out.append("            compression = \"none\";")
        if args.node_extra:
            out.append(args.node_extra.rstrip('\n'))
        out.append("        };")
        nodes.append(node)

    with open(os.path.join(args.outdir, name + ".dtsi"), 'w') as f:
        f.write("\n".join(out) + "\n")
    with open(os.path.join(args.outdir, name + ".loadables"), 'w') as f:
        f.write(", ".join('"%s"' % n for n in nodes) + "\n")


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('--outdir', required=True)
    parser.add_argument('--address-cells', type=int, default=1)
    parser.add_argument('--node-extra', default='',
                        help="text added to every node, e.g. its hash node")
    parser.add_argument('specs', nargs='*', metavar='name:image:entry_rule:arch[:os]')
    args = parser.parse_args()

    os.makedirs(args.outdir, exist_ok=True)
    for spec in args.specs:
        split(args, spec)


if __name__ == '__main__':
    main()
