#!/bin/sh -e
# Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
# SPDX-License-Identifier: MIT

# Fetches the sources of every target in the CI kas files. world is one of
# them, so run it with ci/world.yml in KAS_YAMLS to limit it to this layer.

if [ -z "$1" ] || [ -z "$2" ] ; then
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

TARGETS=$(python3 - "$REPO_DIR/ci" <<'EOF'
import glob
import os
import sys

import yaml

targets = set()
for path in glob.glob(os.path.join(sys.argv[1], "*.yml")):
    with open(path) as f:
        target = (yaml.safe_load(f) or {}).get("target", [])
    targets.update([target] if isinstance(target, str) else target)
print(" ".join(sorted(targets)))
EOF
)

echo "Fetching the sources of: $TARGETS"
bitbake --runall=fetch $TARGETS
