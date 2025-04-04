SUMMARY = "Qualcomm partitioning tool"
DESCRIPTION = "Partitioning tool, generates the GPT and/or Partition MBN"
HOMEPAGE = "https://github.com/qualcomm-linux/qcom-ptool"
SECTION = "devel"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b0a8acd90d872086b279ead88af03369"

RDEPENDS:${PN} += "python3-xml"

SRC_URI = "git://github.com/qualcomm-linux/qcom-ptool.git;branch=main;protocol=https"

SRCREV = "34d6580574bafd38751f526e1674eec3167546dc"

PV = "0.0+git"
S = "${WORKDIR}/git"

inherit python3native

INHIBIT_DEFAULT_DEPS = "1"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
   install -d ${D}${bindir}
   install -m 755 ${S}/ptool.py ${D}${bindir}/ptool.py
   install -m 755 ${S}/gen_partition.py ${D}${bindir}/gen_partition.py
}

BBCLASSEXTEND = "native nativesdk"
