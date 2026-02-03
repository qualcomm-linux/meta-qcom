# Handle dspso.bin unpacking in a generic way
# If DSPSO_URI is defined, the image will be dissected automatically

DSPSO_URI ??= ""

DSPSO_SOC ?= "unknown"
DSPSO_VENDOR ?= "Qualcomm"
DSPSO_DEVICE ?= "Unknown"

# Conditionally populate SRC_URI. We have to do it here rather than in python
# script to let base.bbclass to pick up dependencies
SRC_URI += "${DSPSO_URI}"

# Extract package name part
DSP_PKG_NAME ?= "${@d.getVar("PN").split("hexagon-dspso-")[-1]}"

DSP_QCOM_BASE_PATH = "${datadir}/qcom"
DSP_QCOM_PATH = "${DSP_QCOM_BASE_PATH}/${DSPSO_SOC}/${DSPSO_VENDOR}/${DSPSO_DEVICE}"

DEPENDS += "e2fsprogs-native"

inherit allarch

PACKAGES = " \
    hexagon-dsp-binaries-${DSP_PKG_NAME}-adsp \
    hexagon-dsp-binaries-${DSP_PKG_NAME}-cdsp \
    hexagon-dsp-binaries-${DSP_PKG_NAME}-sdsp \
"

FILES:hexagon-dsp-binaries-${DSP_PKG_NAME}-adsp = "${DSP_QCOM_PATH}/dsp/adsp"
FILES:hexagon-dsp-binaries-${DSP_PKG_NAME}-cdsp = "${DSP_QCOM_PATH}/dsp/cdsp"
FILES:hexagon-dsp-binaries-${DSP_PKG_NAME}-sdsp = "${DSP_QCOM_PATH}/dsp/sdsp"

ALLOW_EMPTY:hexagon-dsp-binaries-${DSP_PKG_NAME}-adsp = "1"
ALLOW_EMPTY:hexagon-dsp-binaries-${DSP_PKG_NAME}-cdsp = "1"
ALLOW_EMPTY:hexagon-dsp-binaries-${DSP_PKG_NAME}-sdsp = "1"

INSANE_SKIP:hexagon-dsp-binaries-${DSP_PKG_NAME}-adsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:hexagon-dsp-binaries-${DSP_PKG_NAME}-cdsp = "arch libdir file-rdeps textrel"
INSANE_SKIP:hexagon-dsp-binaries-${DSP_PKG_NAME}-sdsp = "arch libdir file-rdeps textrel"

# If the URL is the file:// URI, the whole local path will be duplicated in the UNPACKDIR.
# Otherwise we just need the last (filename) part of the path.
def get_dspso_path(path):
    from urllib.parse import urlparse
    if path == "":
        return ""
    url = urlparse(path)
    if url.scheme == "file":
        return url.path
    return url.path.rsplit('/', 1)[1]

handle_dspso_image() {
    for path in adsp cdsp sdsp ; do
        case "${PACKAGES}" in
	    *${path}*)
	    ;;
	    *)
	    continue
	    ;;
	esac
        mkdir -p ${B}/dspso-$path
        debugfs $1 -R "ls -p /$path" | \
            grep '^/[0-9]*/100' | cut -d/ -f6 | \
            while read name ; do echo "dump /$path/$name ${B}/dspso-$path/$name" ; done | \
            debugfs ${1}
    done
}

do_compile:prepend() {
    if [ -z "${DSPSO_URI}" ] ; then
        bbwarn "${PN}: not packaging DSPSO firmware. Please provide DSPSO_URI"
    else
        handle_dspso_image ${UNPACKDIR}/${@get_dspso_path(d.getVar("DSPSO_URI"))}
    fi
}

do_install:prepend() {
    ls ${B}/dspso-adsp/* && mkdir -p ${D}${DSP_QCOM_PATH}/dsp/adsp && install -m 0644 ${B}/dspso-adsp/* ${D}${DSP_QCOM_PATH}/dsp/adsp
    ls ${B}/dspso-cdsp/* && mkdir -p ${D}${DSP_QCOM_PATH}/dsp/cdsp && install -m 0644 ${B}/dspso-cdsp/* ${D}${DSP_QCOM_PATH}/dsp/cdsp
    ls ${B}/dspso-sdsp/* && mkdir -p ${D}${DSP_QCOM_PATH}/dsp/sdsp && install -m 0644 ${B}/dspso-sdsp/* ${D}${DSP_QCOM_PATH}/dsp/sdsp
}
