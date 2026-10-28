#!/bin/sh
# Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
#
# SPDX-License-Identifier: BSD-3-Clause-Clear
set -e

DEV=/dev/disk/by-partlabel/persist
[ -b "$DEV" ] || exit 0

export PATH=/sbin:/usr/sbin

mkfs_persist_ext4() {
    uid=$(id -u teesuppl 2>/dev/null || true)
    gid=$(id -g teesuppl 2>/dev/null || true)

    if [ -n "$uid" ] && [ -n "$gid" ]; then
        mkfs.ext4 -F -E root_owner=${uid}:${gid} "$DEV"
    else
        mkfs.ext4 -F "$DEV"
    fi
}

# Check if ext4 filesystem exists
if [ "$(blkid -o value -s TYPE "$DEV" 2>/dev/null)" != "ext4" ]; then
    echo "$DEV: no valid ext4 filesystem found, creating ext4"
    mkfs_persist_ext4
    exit 0
fi

# Run a read-only fsck check to detect corruptions
e2fsck -n "$DEV" >/dev/null 2>&1
err=$?
if [ "$err" -gt 4 ]; then
    echo "$DEV: filesystem corrupted (e2fsck error=$err), recreating"
    mkfs_persist_ext4
    exit 0
else
    echo "$DEV: filesystem is OK"
fi
