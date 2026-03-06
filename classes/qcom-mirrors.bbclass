# Layer download mirror to minimize reproducibility issues 
#
# Copyright (c) 2026 Qualcomm Innovation Center, Inc.
#
# SPDX-License-Identifier: MIT
#

# Add qcom mirror so we can fallback to it
MIRRORS += "${QCOM_MIRRORS}"

QCOM_MIRRORS_URI ?= "https://artifacts.codelinaro.org/aritfactory/qli-ci/downloads/${QCOM_RELEASE}"

QCOM_MIRRORS ?= " \
svn://.*/.*     ${QCOM_MIRRORS_URI}/ \
git://.*/.*     ${QCOM_MIRRORS_URI}/ \
gitsm://.*/.*   ${QCOM_MIRRORS_URI}/ \
hg://.*/.*      ${QCOM_MIRRORS_URI}/ \
p4://.*/.*      ${QCOM_MIRRORS_URI}/ \
https?://.*/.*  ${QCOM_MIRRORS_URI}/ \
ftp://.*/.*     ${QCOM_MIRRORS_URI}/ \
npm://.*/?.*    ${QCOM_MIRRORS_URI}/ \
s3://.*/.*      ${QCOM_MIRRORS_URI}/ \
crate://.*/.*   ${QCOM_MIRRORS_URI}/ \
gs://.*/.*      ${QCOM_MIRRORS_URI}/ \
"
