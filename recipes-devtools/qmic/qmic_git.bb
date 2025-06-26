SUMMARY = "QMI compiler"
HOMEPAGE = "https://github.com/linux-msm/qmic.git"
SECTION = "devel"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=ca25dbf5ebfc1a058bfc657c895aac2f"

SRCREV = "815dd495eb087b3b3ea02a8ed43716efac43db1c"
SRC_URI = "git://github.com/linux-msm/${BPN}.git;branch=master;protocol=https"

PV = "0.0+"

BBCLASSEXTEND = "native nativesdk"

do_install () {
    oe_runmake install DESTDIR=${D} prefix=${prefix}
}
