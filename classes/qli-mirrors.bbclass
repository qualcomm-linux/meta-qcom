# Qualcomm Linux download mirrors to minimize reproducibility issues
#
# Copyright (c) 2026 Qualcomm Innovation Center, Inc.
#
# SPDX-License-Identifier: MIT
#

QLI_MIRRORS_URI ?= "https://artifacts.codelinaro.org/aritfactory/qli-ci/downloads/${QLI_BASELINE}"

QLI_MIRRORS ?= " \
svn://.*/.*     ${QLI_MIRRORS_URI}/ \
git://.*/.*     ${QLI_MIRRORS_URI}/ \
gitsm://.*/.*   ${QLI_MIRRORS_URI}/ \
hg://.*/.*      ${QLI_MIRRORS_URI}/ \
p4://.*/.*      ${QLI_MIRRORS_URI}/ \
https?://.*/.*  ${QLI_MIRRORS_URI}/ \
ftp://.*/.*     ${QLI_MIRRORS_URI}/ \
npm://.*/?.*    ${QLI_MIRRORS_URI}/ \
s3://.*/.*      ${QLI_MIRRORS_URI}/ \
crate://.*/.*   ${QLI_MIRRORS_URI}/ \
gs://.*/.*      ${QLI_MIRRORS_URI}/ \
"

# Add Qualcomm Linux mirror so we can fallback to it
MIRRORS += "${QLI_MIRRORS}"
