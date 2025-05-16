#!/bin/sh -e
# Copyright (c) 2024 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: MIT

TOPDIR=$(realpath $(dirname $(readlink -f $0))/..)

if [ ! -d /build ] || [ ! -d /work/build ] || [ "$TOPDIR" != "/repo" ] ; then
    echo "ERROR: This script is designed to run inside kas-container"
    exit 1
fi

# pybootchartgui tool
CMD="$CMD /work/oe-core/scripts/pybootchartgui/pybootchartgui.py"
# display time in minutes instead of seconds
CMD="$CMD --minutes"
# image format (png, svg, pdf); default format png
CMD="$CMD --format=svg"
# output path (file or directory) where charts are stored
CMD="$CMD --output=buildchart"
# /path/to/tmp/buildstats/<recipe-machine>/<BUILDNAME>/
CMD="$CMD /work/build/tmp/buildstats"

exec $CMD
