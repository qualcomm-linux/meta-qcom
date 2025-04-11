SUMMARY = "Qualcomm Libdmabufheap library"
HOMEPAGE = "https://git.codelinaro.org/clo/le/platform/system/memory/libdmabufheap"

DESCRIPTION = "Qualcomm Libdmabufheap library provides interface to use DMABUF heap framework"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://BufferAllocator.cpp;beginline=1;endline=15;md5=f0d8beef101110303cf3728fcf3e73a9"

SRCREV = "5ead341efb667cbd2cf519bfaf2837ee8e97b747"
S = "${WORKDIR}/git"

PV = "0.0+git"

SRC_URI = " \
    git://git.codelinaro.org//clo/le/platform/system/memory/libdmabufheap.git;branch=memory-le-apps.lnx.3.0;protocol=https \
    "

inherit autotools pkgconfig

PACKAGECONFIG[ion] = "--with-ion, --without-ion, libion"
