SUMMARY = "simple low-level testing tool for qcom boards"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=987293312a134ab40eec5f3d446cfaff"

SRCREV = "7dd53678fe1e677a63883639c2dad927d7266c00"
SRC_URI = "\
	git://github.com/linux-msm/bootrr.git;branch=master;protocol=https \
"

PV = "0.0+git"

inherit allarch

do_install() {
	oe_runmake install 'DESTDIR=${D}'
}
