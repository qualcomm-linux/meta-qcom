#!/bin/sh -e
# Copyright (c) 2024 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: MIT

TOPDIR=$(realpath $(dirname $(readlink -f $0))/..)

if [ ! -d /build ] || [ ! -d /work/build ] || [ "$TOPDIR" != "/repo" ] ; then
    echo "ERROR: This script is designed to run inside kas-container"
    exit 1
fi

/work/oe-core/scripts/contrib/patchreview.py -v -b -j status.json /repo

# return an error if any malformed patch is found
cat /work/build/status.json |
    python3 -c "import json,sys;obj=json.load(sys.stdin); sys.exit(1) if 'malformed-sob' in obj[0] or 'malformed-upstream-status' in obj[0] else sys.exit(0)"
