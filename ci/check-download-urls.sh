#!/bin/sh -e
# Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
# SPDX-License-Identifier: MIT

# Reject Qualcomm download hosts that are not meant for external use.
# Artifacts on qartifactory-edge are published externally under
# https://softwarecenter.qualcomm.com/nexus/generic/ instead.

cd "${1:-$(dirname "$0")/..}" || exit 2

PATTERN='qartifactory[a-z0-9.-]*\.qualcomm\.com|qcartifactoryedge\.jfrog\.io'
HINT="use https://softwarecenter.qualcomm.com/nexus/generic/ instead of qartifactory"

rc=0
files=$(git grep -l -I -i -E "${PATTERN}" -- ':(top,exclude)ci/check-download-urls.sh') || rc=$?
[ "${rc}" -eq 1 ] && exit 0
[ "${rc}" -ne 0 ] && exit "${rc}"

printf '%s\n' "${files}" | while IFS= read -r file; do
    grep -n -i -E "${PATTERN}" "${file}" | while IFS= read -r match; do
        printf '%s:%s\n' "${file}" "${match}"
        if [ -n "${GITHUB_ACTIONS}" ]; then
            printf '::error file=%s,line=%s::%s\n' "${file}" "${match%%:*}" "${HINT}"
        fi
    done
done
echo "Found qartifactory URLs; ${HINT}" >&2
exit 1
