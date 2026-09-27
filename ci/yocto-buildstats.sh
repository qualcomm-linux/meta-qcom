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
BUILDSTATS="$(bitbake-getvar --value TMPDIR)/buildstats"
BUILDSTATS="$BUILDSTATS/$(ls $BUILDSTATS | tail -1)"

# add pybootchartgui path
PATH="$PATH:$WORK_DIR/oe-core/scripts/pybootchartgui"

# pybootchartgui tool
CMD="pybootchartgui.py"
# display time in minutes instead of seconds
CMD="$CMD --minutes"
# display the full time regardless of which processes are currently shown
CMD="$CMD --full-time"
# image format (png, svg, pdf); default format png
CMD="$CMD --format=svg"
# output path (file or directory) where charts are stored
CMD="$CMD --output=buildstats"
# buildstats log folder
CMD="$CMD $BUILDSTATS"

echo $CMD
eval $CMD

# buildstats-summary tool
CMD="buildstats-summary"
# sort by task duration
CMD="$CMD --sort duration"
# disable highlight
CMD="$CMD --highlight 0"
# buildstats log folder
CMD="$CMD $BUILDSTATS"
# show and save it to
CMD="$CMD | tee buildstats.log"

echo $CMD
eval $CMD

_ncdu(){
    du -sk $1/*/* | \
    sort -rn | \
    sed "s/^/$2 /" | \
    awk '{ \
        printf "%3s%% %6.2f", int(100*$2/$1), $2/(1024*1024); \
        n=split($3,recipe,"/"); printf " %-20s ", recipe[n]; \
        for(i=0; i<$2/10000; i++) printf "#"; print " " \
    }'
}

CCACHE_TOP_DIR=$(bitbake-getvar --value CCACHE_TOP_DIR --ignore-undefined)
if [ -d "$CCACHE_TOP_DIR" ]; then
    CCACHE_SIZE=$(du -sk $CCACHE_TOP_DIR | awk '{ print $1 }')
    CCACHE_SIZE_GB=$(echo $CCACHE_SIZE | awk '{ printf "%.2f", $1/(1024*1024)}')
    echo "Ccache size (${CCACHE_SIZE_GB}GB) [$CCACHE_TOP_DIR] ..."
    _ncdu $CCACHE_TOP_DIR $CCACHE_SIZE | cat
fi
