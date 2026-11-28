#!/bin/bash -e
# Copyright (c) Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: MIT

TOPDIR=$(realpath $(dirname $(readlink -f $0))/..)

# Run bsp test module
TESTS_MODULE="--run-tests bsp"

source $TOPDIR/ci/yocto-check-layer.sh
