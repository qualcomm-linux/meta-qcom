#!/bin/sh -e
# Copyright (c) 2025 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: MIT

TOPDIR=$(realpath $(dirname $(readlink -f $0))/..)
SCRIPT=$(realpath $1)

if ! [ -f $SCRIPT ]; then
    echo "The script path argument is missing, please run it with:"
    echo " $0 /path/to/script"
    exit 1
fi

# Ensure KAS workspace is outside of the checked out repo
# Allows the caller to specify KAS_WORK_DIR, otherwise make temp one
export KAS_WORK_DIR=$(realpath ${KAS_WORK_DIR:-$(mktemp -d)})

echo "Running kas in $KAS_WORK_DIR"
exec kas shell $TOPDIR/ci/base.yml --command "$SCRIPT $TOPDIR $KAS_WORK_DIR"
