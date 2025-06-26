SUMMARY = "Qualcomm libvmmem library"
HOMEPAGE = "https://git.codelinaro.org/clo/le/platform/system/memory/libvmmem"

DESCRIPTION = "Qualcomm Libvmmem libraty provides interface to use membuf framework"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM += "\
    file://vmmem.cpp;beginline=1;endline=4;md5=e19ba7a648e00191388321a1e8a5c974 \
    file://vmmem_wrapper.cpp;beginline=1;endline=4;md5=e19ba7a648e00191388321a1e8a5c974 \
    file://include/vmmem.h;beginline=1;endline=4;md5=e19ba7a648e00191388321a1e8a5c974 \
    file://include/vmmem_wrapper.h;beginline=1;endline=4;md5=e19ba7a648e00191388321a1e8a5c974 \
    "

SRCREV = "2b88e9bc6030893b8e9b46ae85999c8de103858d"

PV = "0.0+git"

SRC_URI = " \
git://git.codelinaro.org/clo/le/platform/system/memory/libvmmem.git;branch=kernel.apps.lnx.4.0;protocol=https \
"

inherit autotools pkgconfig
