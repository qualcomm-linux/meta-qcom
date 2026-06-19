#!/bin/sh -e
# Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
# SPDX-License-Identifier: MIT
#
# Run oe-selftest for tests provided by this layer.
#
# Usage:
#   ci/oe-selftest.sh REPO_DIR WORK_DIR [TEST_CASES]
#
# Arguments:
#   REPO_DIR    Path to the meta-qcom repository checkout
#   WORK_DIR    Path to the kas workspace (with oe-core, bitbake, etc.)
#   TEST_CASES  Optional: specific test module(s) to run (space-separated).
#               When omitted, all test modules under lib/oeqa/selftest/cases/
#               are discovered and executed automatically.
#
# Environment:
#   MACHINE     Machine to use for selftest (default: rb3gen2-core-kit)
#   SSTATE_DIR  Shared-state cache directory (passed to bitbake via local.conf)
#   DL_DIR      Download directory (passed to bitbake via local.conf)

if [ -z "$1" ] || [ -z "$2" ] ; then
    echo "The REPO_DIR or WORK_DIR is empty and it needs to point to the corresponding directories."
    echo "Please run it with:"
    echo " $0 REPO_DIR WORK_DIR [TEST_CASES]"
    exit 1
fi

REPO_DIR="$1"
WORK_DIR="$2"
shift 2
TEST_CASES="$*"

_is_dir(){
    test -d "$1" && return
    echo "The '$1' is not a directory."
    exit 1
}

_is_dir "$REPO_DIR"
_is_dir "$WORK_DIR"

# Auto-discover test modules if none specified
if [ -z "$TEST_CASES" ]; then
    CASES_DIR="$REPO_DIR/lib/oeqa/selftest/cases"
    if [ -d "$CASES_DIR" ]; then
        for f in $(find "$CASES_DIR" -maxdepth 1 -name "*.py" ! -name "__init__.py" -type f | sort); do
            module=$(basename "$f" .py)
            TEST_CASES="${TEST_CASES:+$TEST_CASES }$module"
        done
    fi

    if [ -z "$TEST_CASES" ]; then
        echo "No test modules found in $CASES_DIR"
        exit 1
    fi
fi

echo "Test modules to run: $TEST_CASES"

# Create a temporary build directory (same pattern as yocto-check-layer.sh)
BUILDDIR="$(mktemp -p "$WORK_DIR" -d -t build-oe-selftest-XXXX)"
cd "$WORK_DIR/oe-core"
. ./oe-init-build-env "$BUILDDIR"

# Add the meta-qcom layer
bitbake-layers add-layer "$REPO_DIR"

# Configure for selftest
cat >> conf/local.conf << EOF
MACHINE = "${MACHINE:-rb3gen2-core-kit}"
EOF

# Use shared sstate/download caches when available
if [ -n "$SSTATE_DIR" ]; then
    echo "SSTATE_DIR = \"$SSTATE_DIR\"" >> conf/local.conf
fi
if [ -n "$DL_DIR" ]; then
    echo "DL_DIR = \"$DL_DIR\"" >> conf/local.conf
fi

oe-selftest --run-tests "$TEST_CASES"
