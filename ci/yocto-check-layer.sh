#!/bin/sh -e
# Copyright (c) 2024 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: MIT

TOPDIR=$(realpath $(dirname $(readlink -f $0))/..)

# Ensure KAS workspace is outside of the checked out repo
# Allows the caller to specify KAS_WORK_DIR, otherwise make temp one
export KAS_WORK_DIR=$(realpath ${KAS_WORK_DIR:-$(mktemp -d)})

# Yocto Project layer checking tool
CMD="yocto-check-layer-wrapper"
# Layer to check
CMD="$CMD $TOPDIR"
# Disable auto layer discovery
CMD="$CMD --no-auto"
# Layers to process for dependencies
CMD="$CMD --dependency $KAS_WORK_DIR/oe-core/meta"
# Disable automatic testing of dependencies
CMD="$CMD --no-auto-dependency"
# Set machines to all machines defined in this BSP layer
CMD="$CMD --machines $(echo $(find $TOPDIR/conf/machine/ -maxdepth 1 -name *.conf -exec basename {} .conf \; ))"

echo "Running kas in $KAS_WORK_DIR"
exec kas shell $TOPDIR/ci/base.yml --command "$CMD"
