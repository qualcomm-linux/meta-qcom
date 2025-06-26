SUMMARY = "Qualcomm Libdmabufheap library"
HOMEPAGE = "https://git.codelinaro.org/clo/le/platform/system/memory/libdmabufheap"
DESCRIPTION = "Libdmabufheap library to access DMA-BUF heap framework with some custom changes \
removing dependency on libion and libbase"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://BufferAllocator.cpp;beginline=1;endline=15;md5=f0d8beef101110303cf3728fcf3e73a9"

PV = "0.0+git"
SRCREV = "5ead341efb667cbd2cf519bfaf2837ee8e97b747"
SRC_URI = " \
git://git.codelinaro.org//clo/le/platform/system/memory/libdmabufheap.git;branch=memory-le-apps.lnx.3.0;protocol=https \
"

inherit autotools pkgconfig
