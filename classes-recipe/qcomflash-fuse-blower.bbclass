#
# Copyright (c) 2026 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: BSD-3-Clause-Clear
#
# Add the Fuse Blower images of firmware-qcom-fuse-blower to the qcomflash
# bundle under fuse/, each with a rawprogram XML that programs only the
# secdata partition.  The main rawprogram set is left alone, so a normal
# flash never fuses a device; fusing is a separate, explicit step:
#
#   qdl --allow-fusing prog_firehose_ddr.elf fuse/rawprogram_secdata-<stage>.xml
#
# image_types_qcom.bbclass inherits this class when QCOM_FUSE_BLOWER_ENABLE
# is "1".

QCOMFLASH_FUSE_BLOWER_SUBDIR ?= "fuse"

do_image_qcomflash[depends] += "firmware-qcom-fuse-blower:do_deploy"

# Ahead of create_qcomflash_tarball, see image_types_qcom.bbclass.
do_image_qcomflash[postfuncs] =+ "create_qcomflash_fuse_blower"
create_qcomflash_fuse_blower[dirs] = "${QCOMFLASH_DIR}"

create_qcomflash_fuse_blower() {
    fusedir="${QCOMFLASH_DIR}/${QCOMFLASH_FUSE_BLOWER_SUBDIR}"
    install -d "${fusedir}"

    # The secdata entry of this bundle's own partition layout.
    entry="$(grep -h 'label="secdata"' rawprogram*.xml | head -n 1)"
    if [ -z "${entry}" ]; then
        bbfatal "qcomflash-fuse-blower: no secdata partition in rawprogram*.xml"
    fi

    for stage in ${QCOM_FUSE_BLOWER_STAGES}; do
        image="sec-${stage}.elf"
        install -m 0644 "${DEPLOY_DIR_IMAGE}/fuse-blower/${image}" \
                        "${DEPLOY_DIR_IMAGE}/fuse-blower/sec-${stage}.txt" "${fusedir}/"
        {
            echo '<?xml version="1.0" ?>'
            echo '<data>'
            printf '%s\n' "${entry}" | sed "s/filename=\"[^\"]*\"/filename=\"${image}\"/"
            echo '</data>'
        } > "${fusedir}/rawprogram_secdata-${stage}.xml"
    done
}
