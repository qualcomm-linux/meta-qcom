#!/bin/sh -e
# Copyright (c) 2024 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: MIT

TOPDIR=$(realpath $(dirname $(readlink -f $0))/..)

# Ensure KAS workspace is outside of the checked out repo
# Allows the caller to specify KAS_WORK_DIR, otherwise make temp one
export KAS_WORK_DIR=$(realpath ${KAS_WORK_DIR:-$(mktemp -d)})

echo "Running kas in $KAS_WORK_DIR"
kas shell $TOPDIR/ci/base.yml --command "$KAS_WORK_DIR/oe-core/scripts/contrib/patchreview.py -v -b -j status.json $TOPDIR"

# return an error if any malformed patch is found
cat $KAS_WORK_DIR/build/status.json |
    python -c "import json,sys;obj=json.load(sys.stdin); sys.exit(1) if 'malformed-sob' in obj[0] or 'malformed-upstream-status' in obj[0] else sys.exit(0)"
