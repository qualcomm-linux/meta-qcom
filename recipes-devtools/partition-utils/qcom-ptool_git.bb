SUMMARY = "Qualcomm partitioning tool"
DESCRIPTION = "Partitioning tool, generates the GPT and/or Partition MBN"
HOMEPAGE = "https://github.com/qualcomm-linux/qcom-ptool"
SECTION = "devel"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b0a8acd90d872086b279ead88af03369"

RDEPENDS:${PN} += "python3-xml"

SRC_URI = "git://github.com/qualcomm-linux/qcom-ptool.git;branch=main;protocol=https"

SRCREV = "be9d04e7940c777d361f05f0468e260ee493b76e"

PV = "0.0+git"

inherit python3native

INHIBIT_DEFAULT_DEPS = "1"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    oe_runmake install DESTDIR=${D} PREFIX=${prefix}
}

BBCLASSEXTEND = "native nativesdk"
