SUMMARY = "Qualcomm DownLoader flashing tool"
DESCRIPTION = "Communicate with Qualcomm SoCs to upload new software or \
dump memory"
HOMEPAGE = "https://github.com/linux-msm/qdl.git"
SECTION = "devel"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=da6bfde9cb5bc5120a51775381f6edf1"

DEPENDS = "libxml2 libusb1"

inherit pkgconfig

SRCREV = "5db7794e9fdb73ed0c45384026cd8a62b5fff786"
SRC_URI = "git://github.com/linux-msm/${BPN}.git;branch=master;protocol=https"

PV = "2.1+${SRCREV}"

do_install () {
    oe_runmake install DESTDIR=${D} prefix=${prefix}
}

BBCLASSEXTEND = "native nativesdk"
