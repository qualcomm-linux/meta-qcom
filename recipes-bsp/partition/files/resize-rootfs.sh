#!/bin/sh
# Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
# SPDX-License-Identifier: BSD-3-Clause-clear

[ $(whoami) = "root" ] || { echo "ERROR: Must be a root user to resize rootfs." && exit 1; }

RESIZE2FS=$(which resize2fs) || { echo "ERROR: 'resize2fs' utility not found." && exit 1; }

ROOT_DEVICE="$(readlink -f "$(findmnt / -o source -n -v)")"

${RESIZE2FS} "${ROOT_DEVICE}"
