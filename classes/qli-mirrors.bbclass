# Qualcomm Linux download mirrors to minimize reproducibility issues
#
# Copyright (c) 2026 Qualcomm Innovation Center, Inc.
#
# SPDX-License-Identifier: MIT
#

QLI_MIRRORS_URI ?= "https://artifacts.codelinaro.org/artifactory/qli-ci/downloads/${QLI_BASELINE}"
# The 2.x mirror is refreshed on every release, so use it as a second fallback
QLI_RELEASE_MIRRORS_URI ?= "https://artifacts.codelinaro.org/artifactory/qli-ci/downloads/2.x"

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
svn://.*/.*     ${QLI_RELEASE_MIRRORS_URI}/ \
git://.*/.*     ${QLI_RELEASE_MIRRORS_URI}/ \
gitsm://.*/.*   ${QLI_RELEASE_MIRRORS_URI}/ \
hg://.*/.*      ${QLI_RELEASE_MIRRORS_URI}/ \
p4://.*/.*      ${QLI_RELEASE_MIRRORS_URI}/ \
https?://.*/.*  ${QLI_RELEASE_MIRRORS_URI}/ \
ftp://.*/.*     ${QLI_RELEASE_MIRRORS_URI}/ \
npm://.*/?.*    ${QLI_RELEASE_MIRRORS_URI}/ \
s3://.*/.*      ${QLI_RELEASE_MIRRORS_URI}/ \
crate://.*/.*   ${QLI_RELEASE_MIRRORS_URI}/ \
gs://.*/.*      ${QLI_RELEASE_MIRRORS_URI}/ \
"

# Add Qualcomm Linux mirrors so we can fallback to them
MIRRORS += "${QLI_MIRRORS}"
