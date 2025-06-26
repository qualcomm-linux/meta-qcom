SUMMARY = "A tool to debug Qualcomm clock controllers."
HOMEPAGE = "https://github.com/linux-msm/debugcc/"
SECTION = "devel"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://debugcc.c;beginline=5;endline=29;md5=5598b6b886a3af944e4d19bb7d947095"

SRC_URI = "\
    git://github.com/linux-msm/debugcc.git;branch=master;protocol=https \
"

SRCREV = "6444cf924b53b8453c2285cde48b6c52e5960089"

PV = "0.0+git"

inherit meson
