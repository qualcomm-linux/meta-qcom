SUMMARY = "MDT to MBN conversion tool"
HOMEPAGE = "https://github.com/linux-msm/pil-squasher.git"
SECTION = "devel"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=aac743f09c3b727c9fa920ed48cc946d"

SRCREV = "e25efba9b3abb16643262e9a9e3eb330c48cf9a5"
SRC_URI = " \
	git://github.com/linux-msm/${BPN}.git;branch=master;protocol=https \
"

PV = "0.0+"

do_install () {
    oe_runmake install DESTDIR=${D} prefix=${prefix}
}

BBCLASSEXTEND = "native nativesdk"
