SUMMARY = "Qualcomm partitioning tool"
DESCRIPTION = "Partitioning tool, generates the GPT and/or Partition MBN"
HOMEPAGE = "https://git.codelinaro.org/linaro/qcomlt/partioning_tool"
SECTION = "devel"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=7530c1d01d9cfee94e67d6a55e886db5"

RDEPENDS:${PN} += "python3-xml"

SRC_URI = "git://git.codelinaro.org/linaro/qcomlt/partioning_tool.git;branch=master;protocol=https \
	   file://0001-ptool.py-Generate-zero-files-in-output-folder-when-s.patch \
	   file://0002-ptool.py-Python-3-support.patch \
"

SRCREV = "3484fc0a88088dea00397774fc93f9acd3a23ce0"

PV = "0.0+git"
S = "${WORKDIR}/git"

inherit python3native

INHIBIT_DEFAULT_DEPS = "1"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
   install -d ${D}${bindir}
   install -m 755 ${S}/ptool.py ${D}${bindir}/ptool.py
}

BBCLASSEXTEND = "native nativesdk"
