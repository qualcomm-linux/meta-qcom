SUMMARY = "Qualcomm qcom-tee library"
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

SRC_URI = "git://github.com/quic/quic-teec.git;nobranch=1;protocol=https;tag=v${PV}"
SRCREV = "34fa8197a34a06ec0187c2958c776f3c3a6b9ec1"

DEPENDS += "qcbor"
