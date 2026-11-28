#!/bin/bash -e
# Copyright (c) Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: MIT

TOPDIR=$(realpath $(dirname $(readlink -f $0))/..)

# Run common/distro tests module
TESTS_MODULE="--run-tests common distro"

source $TOPDIR/ci/yocto-check-layer.sh
