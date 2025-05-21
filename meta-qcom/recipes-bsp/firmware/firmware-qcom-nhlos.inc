# Handle NON-HLOS.bin unpacking in a generic way
# Include the file to be able to dissect the image using handle_nonhlos_image()
# If NHLOS_URI is defined, the image will be dissected automatically

NHLOS_URI ??= ""

# List all firmware files to be installed
FW_QCOM_LIST ??= ""

DEPENDS += "pil-squasher-native mtools-native"

# Conditionally populate SRC_URI. We have to do it here rather than in python
# script to let base.bbclass to pick up dependencies
SRC_URI += "${NHLOS_URI}"

unpack_nhlos_image() {
    mkdir -p ${B}/firmware
    mcopy -n -s -i "$1" ::/* ${B}/firmware/
}

handle_nonhlos_image() {
    for fw in ${B}/firmware/image/*.mdt ; do
        pil-squasher ${B}/`basename $fw mdt`mbn $fw || exit 1
    done
}

# If the URL is the file:// URI, the whole local path will be duplicated in the UNPACKDIR.
# Otherwise we just need the last (filename) part of the path.
def get_nhlos_path(path):
    from urllib.parse import urlparse
    if path == "":
        return ""
    url = urlparse(path)
    if url.scheme == "file":
        return url.path
    return url.path.rsplit('/', 1)[1]

do_compile:prepend() {
    if [ -z "${NHLOS_URI}" ] ; then
        bbwarn "${PN}: not packaging NHLOS firmware. Please provide HNLOS_URI"
    else
        unpack_nhlos_image ${UNPACKDIR}/${@get_nhlos_path(d.getVar("NHLOS_URI"))}
        handle_nonhlos_image
    fi
}

do_install:prepend() {
    install -d ${D}${FW_QCOM_PATH}

    for fw in ${FW_QCOM_LIST} ; do
        if [ -r ${B}/$fw ] ; then
            install -m 0644 ${B}/$fw ${D}${FW_QCOM_PATH}
        fi

        if [ -r ${B}/firmware/image/$fw ] ; then
            install -m 0644 ${B}/firmware/image/$fw ${D}${FW_QCOM_PATH}
        fi
    done
}
