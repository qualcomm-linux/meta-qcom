# Handle Adreno firmware unpacking in a generic way
# Include the file to be able to dissect the archive.
# If ADRENO_URI is defined, the image will be dissected automatically
# Handle fwpath=... as a path to Adreno firmware inside the archive

ADRENO_URI ??= ""

# Conditionally populate SRC_URI. We have to do it here rather than in python
# script to let base.bbclass to pick up dependencies
SRC_URI += "${@['', '${ADRENO_URI};subdir=adreno;name=adreno'][d.getVar('ADRENO_URI') != ''] }"

# the file is unpacked to this dir, clean it up
do_unpack[cleandirs] = "${UNPACKDIR}/adreno"

DEPENDS += "pil-squasher-native"

python () {
    uri = d.getVar("ADRENO_URI")
    if uri:
        urldata = bb.fetch2.FetchData(d.getVar("ADRENO_URI"), d)
        if "fwpath" in urldata.parm:
            d.setVar("ADRENO_PATH", urldata.parm["fwpath"])
        else:
            d.setVar("ADRENO_PATH", "")
}

do_compile:append() {
    if [ -z "${ADRENO_URI}" ] ; then
        bbwarn "${PN}: not packaging ADRENO firmware. Please provide ADRENO_URI"
    else
        ls ${UNPACKDIR}/adreno/${ADRENO_PATH}/*_zap.mdt | while read fw ; do
            pil-squasher ${B}/`basename $fw mdt`mbn $fw || exit 1
        done
        for fw in ${FW_QCOM_LIST} ; do
            if [ -r ${UNPACKDIR}/adreno/${ADRENO_PATH}/$fw ] ; then
                cp ${UNPACKDIR}/adreno/${ADRENO_PATH}/$fw ${B}/
            fi
        done
    fi
}
