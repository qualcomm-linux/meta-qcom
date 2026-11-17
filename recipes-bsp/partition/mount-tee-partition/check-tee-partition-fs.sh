#!/bin/sh
# Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
#
# SPDX-License-Identifier: BSD-3-Clause-Clear
set -e

DEV=/dev/disk/by-partlabel/persist
[ -b "$DEV" ] || exit 0

export PATH=/sbin:/usr/sbin

# Check if ext4 filesystem exists
if [ "$(blkid -o value -s TYPE "$DEV" 2>/dev/null)" != "ext4" ]; then
    echo "$DEV: no valid ext4 filesystem found, creating ext4"
    mkfs.ext4 -F "$DEV"
    exit 0
fi

# Run a read-only fsck check to detect corruptions
err=0
e2fsck -n "$DEV" >/dev/null 2>&1 || err=$?
if [ "$err" -gt 4 ]; then
    echo "$DEV: filesystem corrupted (e2fsck error=$err), recreating"
    mkfs.ext4 -F "$DEV"
    exit 0
else
    echo "$DEV: filesystem is OK"
fi
