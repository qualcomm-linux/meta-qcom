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

# Yocto Project layer checking tool
CMD="yocto-check-layer-wrapper"
# Layer to check
CMD="$CMD $REPO_DIR"
# Disable auto layer discovery
CMD="$CMD --no-auto"
# Layers to process for dependencies
CMD="$CMD --dependency $WORK_DIR/oe-core/meta"
# Disable automatic testing of dependencies
CMD="$CMD --no-auto-dependency"
# Set machines to all machines defined in this BSP layer
CMD="$CMD --machines $(echo $(find $REPO_DIR/conf/machine/ -maxdepth 1 -name *.conf -exec basename {} .conf \; ))"

exec $CMD
