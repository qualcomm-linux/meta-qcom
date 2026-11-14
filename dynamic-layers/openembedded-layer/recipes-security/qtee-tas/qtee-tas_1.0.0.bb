SUMMARY = "Qualcomm TEE Trusted Applications"
DESCRIPTION = " \
Hosts Qualcomm Trusted Execution Environment supported Trusted Application. \
"
HOMEPAGE = "https://github.com/qualcomm/qtee-tas.git"
SECTION = "devel"

LICENSE = "LicenseRef-LICENSE.qcom-2 AND Apache-2.0 AND BSD-2-Clause AND BSD-3-Clause AND BSL-1.0"

LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=2b1366ebba1ebd9ae25ad19626bbca93 \
                    file://NO.LOGIN.BINARY.LICENSE.QTI.pdf;md5=a6e10719e84e1b5f21791e538bce263c \
                    file://NOTICE;md5=2eb26a6218a4f02fecdeefc48873a49d"

SRC_URI = "git://github.com/qualcomm/qtee-tas.git;branch=main;protocol=https;tag=v${PV}"

SRCREV = "503f019555a05454fc616dddb2dc86e8883fa1ca"

do_install() {
       mkdir -p ${D}${nonarch_base_libdir}/qtee-tas
       cp -R ${S}/ta/* ${D}${nonarch_base_libdir}/qtee-tas/
}

INSANE_SKIP:${PN} += "arch"
