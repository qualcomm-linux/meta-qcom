SUMMARY = "Qualcomm SWIV (Software Image Version) ELF annotation tool"
DESCRIPTION = "Annotates a boot firmware ELF image with a SWIV segment, as \
required by the Qualcomm secure boot chain before the image is signed \
(e.g. U-Boot SPL signed as the TZ partition image)."
HOMEPAGE = "https://github.com/qualcomm-linux/boot-firmware-ci"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=223037c4be0bfc6cf757035432adf983"

SRC_URI = "git://github.com/qualcomm-linux/boot-firmware-ci.git;branch=main;protocol=https"
SRCREV = "a0f007327c1ffc0b568def06c4b035b8d3fb05ac"

INHIBIT_DEFAULT_DEPS = "1"

inherit python3native

do_install() {
    install -Dm 0755 ${S}/tools/swiv_build_utility.py \
        ${D}${bindir}/swiv_build_utility
}

RDEPENDS:${PN} = "python3-core"

BBCLASSEXTEND = "native nativesdk"
