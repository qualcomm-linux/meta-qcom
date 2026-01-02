# To apply patches to support Qualcomm specific formats and fixes
SRC_URI:append:qcom = " \
    git://github.com/Raja-Ganapathi-Busam/gst-qti-oss-patches.git;protocol=ssh;user=git;branch=${PV};name=qti_patches;destsuffix=patchrepo;subpath=${BPN} \
"

SRCREV_qti_patches = "${AUTOREV}"

qcom_do_patch() {
    PATCHREPO="${WORKDIR}/sources/patchrepo"
    cd ${S}

    export QUILT_PATCHES=${PATCHREPO}

    # Apply each patch listed in series file
    if [ -f "${PATCHREPO}/series" ]; then
        while read p; do
            bbnote "Iterating ${PATCHREPO}/${p}"
            # Skip empty/comment lines
            case "$p" in
                ""|\#*) continue ;;
            esac

            if [ -f "${PATCHREPO}/${p}" ]; then
                quilt import "${PATCHREPO}/${p}"
                quilt push || bbfatal "Failed to apply appended quilt series"
            else
                bbfatal "Patch '${p}' listed in series not found at ${PATCHREPO}/${p}"
            fi
        done < "${PATCHREPO}/series"
    else
        bbnote "No series file found at ${PATCHREPO}/series"
    fi
}

python do_patch:append:qcom() {
    bb.build.exec_func('qcom_do_patch', d)
}
