#!/bin/sh -e
# Copyright (c) 2025 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: MIT

TOPDIR=$(realpath $(dirname $(readlink -f $0))/..)

_help(){
    echo "The script path argument is missing, please run it with:"
    echo " $0 /path/to/script"
    exit 1
}

if [ -z "$1" ] ; then
    _help
fi

SCRIPT=$(realpath $1)

if ! [ -f "$SCRIPT" ]; then
    _help
fi

# make it relative to the TOPDIR
SCRIPT=${SCRIPT#$TOPDIR/}

# on ci the kas-container is not on the default path
KAS_CONTAINER=${KAS_CONTAINER:-$(which kas-container)}

exec $KAS_CONTAINER shell ${KAS_YAMLS:-$TOPDIR/ci/base.yml} --command "/repo/$SCRIPT /repo /work"
