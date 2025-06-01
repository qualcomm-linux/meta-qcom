#!/bin/bash -e
# Copyright (c) 2024 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: MIT

TOPDIR=$(realpath $(dirname $(readlink -f $0))/..)

if [ ! -d /work/build ] || [ "$TOPDIR" != "/repo" ] ; then
    echo "ERROR: This script is designed to run inside kas-container"
    exit 1
fi

# Creates a temporary build directory to run the yocto-check-layer
# script to avoid a contaminated environment.
BUILDDIR="$(mktemp -p /work/build -d -t yocto-check-layer-XXXX)"
source /work/oe-core/oe-init-build-env $BUILDDIR
git -c advice.detachedHead=false clone --quiet --shared /repo meta-qcom

# Yocto Project layer checking tool
CMD="yocto-check-layer"
# Layer to check
CMD="$CMD meta-qcom"
# Disable auto layer discovery
CMD="$CMD --no-auto"
# Layers to process for dependencies
CMD="$CMD --dependency /work/oe-core/meta"
# Disable automatic testing of dependencies
CMD="$CMD --no-auto-dependency"
# Set machines to all machines defined in this BSP layer
CMD="$CMD --machines $(echo $(find meta-qcom/conf/machine/ -maxdepth 1 -name *.conf -exec basename {} .conf \; ))"

exec $CMD
