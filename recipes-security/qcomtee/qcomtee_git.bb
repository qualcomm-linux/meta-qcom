SUMMARY = "Qualcomm quic-teec library"
DESCRIPTION = " \
QCOM-TEE Library provides an interface for communication to \
the Qualcomm Trusted Execution Environment (QTEE) via the \
QCOM-TEE driver registered with the Linux TEE subsystem. \
"
HOMEPAGE = "https://github.com/quic/quic-teec.git"
SECTION = "libs"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=2b1366ebba1ebd9ae25ad19626bbca93"

inherit cmake

SRC_URI = "\
    git://github.com/quic/quic-teec.git;branch=main;protocol=https \
"
SRCREV = "87c95f1d44fc9522d4268a64f1fb47f9e87ddcf1"
PV = "0.0+git"

DEPENDS += " qcbor"

EXTRA_OECMAKE = " -DBUILD_UNITTEST=ON -DCMAKE_POLICY_VERSION_MINIMUM=3.5"

do_install:append() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/build/tests/unittest ${D}${bindir}/
}

FILES:${PN}-bin += " ${bindir}/unittest"

