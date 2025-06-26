HOMEPAGE = "https://github.com/linux-msm/hexagonrpc"
SUMMARY = "Alternative implementation for FastRPC, targeting Sensors usecases"
DESCRIPTION = "FastRPC ioctl wrapper and a reverse tunnel, used to \
communicate with the Context Hub Runtime Environment, a program on the DSP \
that manages sensors, and to serve files to remote processors"
SECTION = "utils"

LICENSE = "GPL-3.0-or-later"
LIC_FILES_CHKSUM = "file://COPYING;md5=1ebbd3e34237af26da5dc08a4e440464"

SRC_URI = "\
    git://github.com/linux-msm/hexagonrpc;protocol=https;branch=main \
    file://0001-listener-fix-Wsign-compare-error.patch \
    file://0001-apps_mem-specify-int-argument-to-fastrpc_apps_mem_in.patch \
"
SRCREV = "2377fd46f0c121df0ed21f6530ebdda1ecb4339a"

PV = "0.3.2+"

inherit meson
