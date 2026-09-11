# Copyright (c) Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: BSD-3-Clause-Clear

python __anonymous() {
    bb_signature_handler = d.getVar('BB_SIGNATURE_HANDLER')
    if bb_signature_handler != 'OEBasicHash':
        bb.fatal("BB_SIGNATURE_HANDLER='%s' but image-on-sstate class only works with 'OEBasicHash'" % bb_signature_handler)
}

SSTATE_SKIP_CREATION_INITRAMFS_ONLY ?= "0"

SSTATE_SKIP_CREATION_IMAGE = "${@'0' if d.getVar('INITRAMFS_FSTYPES') == d.getVar('IMAGE_FSTYPES') else '${SSTATE_SKIP_CREATION_INITRAMFS_ONLY}'}"

SSTATE_SKIP_CREATION:task-image-complete = "${SSTATE_SKIP_CREATION_IMAGE}"
SSTATE_SKIP_CREATION:task-image-qa = "${SSTATE_SKIP_CREATION_IMAGE}"
SSTATE_SKIP_CREATION:task-create-image-spdx = "${SSTATE_SKIP_CREATION_IMAGE}"
SSTATE_SKIP_CREATION:task-create-image-sbom-spdx = "${SSTATE_SKIP_CREATION_IMAGE}"
