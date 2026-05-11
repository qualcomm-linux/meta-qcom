#!/bin/sh -e
# Copyright (c) 2024 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: MIT

if [ -z $1 ] || [ -z $2 ] ; then
    echo "The REPO_DIR or WORK_DIR is empty and it needs to point to the corresponding directories."
    echo "Please run it with:"
    echo " $0 REPO_DIR WORK_DIR"
    exit 1
fi

REPO_DIR="$1"
WORK_DIR="$2"

_is_dir(){
    test -d "$1" && return
    echo "The '$1' is not a directory."
    exit 1
}

_is_dir "$REPO_DIR"
_is_dir "$WORK_DIR"

# latest buildstats folder
BUILDSTATS="$WORK_DIR/build/tmp/buildstats"
BUILDSTATS="$BUILDSTATS/$(ls $BUILDSTATS | tail -1)"
if [ "$(find $BUILDSTATS -maxdepth 1 -type d | wc -l)" -eq 1 ]; then
    echo "Skipping buildstats, '$BUILDSTATS' does not contain valid data"
    exit 0
fi

# pybootchartgui tool
CMD="$CMD $WORK_DIR/oe-core/scripts/pybootchartgui/pybootchartgui.py"
# display time in minutes instead of seconds
CMD="$CMD --minutes"
# display the full time regardless of which processes are currently shown
CMD="$CMD --full-time"
# image format (png, svg, pdf); default format png
CMD="$CMD --format=svg"
# output path (file or directory) where charts are stored
CMD="$CMD --output=buildchart"
# /path/to/tmp/buildstats/<recipe-machine>/<BUILDNAME>/
CMD="$CMD $BUILDSTATS"

exec $CMD
