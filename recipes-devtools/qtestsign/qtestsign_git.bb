SUMMARY = "qtestsign is a simple tool to sign ELF Qualcomm firmware images"
HOMEPAGE = "https://github.com/msm8916-mainline/qtestsign"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRC_URI = "git://github.com/msm8916-mainline/qtestsign.git;branch=main;protocol=https"
SRCREV = "ce6ba20f4ead008e5b522a81179c98dd48ccf06e"

INHIBIT_DEFAULT_DEPS = "1"

inherit python3native

do_install() {
    install -d ${D}${bindir}
    install -d ${D}${PYTHON_SITEPACKAGES_DIR}/qtestsign

    cd ${S}
    cp -r * ${D}${PYTHON_SITEPACKAGES_DIR}/qtestsign

    rm ${D}${PYTHON_SITEPACKAGES_DIR}/qtestsign/README.md
    rm ${D}${PYTHON_SITEPACKAGES_DIR}/qtestsign/COPYING
    rm ${D}${PYTHON_SITEPACKAGES_DIR}/qtestsign/requirements.txt

    ln -sr ${D}${PYTHON_SITEPACKAGES_DIR}/qtestsign/qtestsign.py ${D}${bindir}/qtestsign
}

FILES:${PN} += "${PYTHON_SITEPACKAGES_DIR}/qtestsign"
RDEPENDS:${PN} = "python3 python3-cryptography"

BBCLASSEXTEND = "native nativesdk"
