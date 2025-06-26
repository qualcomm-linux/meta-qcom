SUMMARY = "Tools to create boot images for QCOM SoC"

HOMEPAGE = "https://git.codelinaro.org/clo/qsdk/oss/tools/skales"
SECTION = "devel"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://mkbootimg;beginline=3;endline=29;md5=114b84083e657f3886bfa2c1e5de7deb"

SRCREV = "1ccd3e924f6955b1c9d5f921e5311c8db8411787"
PV = "1.5.0+git"

SRC_URI = "git://git.codelinaro.org/clo/qsdk/oss/tools/skales.git;protocol=https;branch=caf_migration/skales/master \
          file://0002-mkbootimg-use-python3.patch \
          "

do_install () {
    install -d ${D}${bindir}/skales
    install -m 0755 ${S}/mkbootimg ${D}${bindir}/skales
}

BBCLASSEXTEND = "native"
