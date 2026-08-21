SUMMARY = "Device Tree metadata blob for QCOM platforms"
HOMEPAGE = "https://github.com/qualcomm-linux/qcom-dtb-metadata"

LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=2998c54c288b081076c9af987bdf4838"

DEPENDS += "dtc-native"

SRC_URI = "git://github.com/qualcomm-linux/qcom-dtb-metadata.git;branch=main;protocol=https;tag=v${PV}"
SRC_URI:append = " file://0001-Add-nord-support.patch"

# Nord SoC variants (nord and qam8x97p) have no entries in the upstream
# qcom-dtb-metadata repository yet. This pending patch registers their
# msm-ids so the kernel FIT image can be built. Drop once Nord support
# lands upstream.
SRCREV = "7fc788a9758b572cbbbea312332b6621823d55c0"

INHIBIT_DEFAULT_DEPS = "1"

do_configure[noexec] = "1"

inherit deploy

do_deploy() {
    install -m 0644 ${B}/qcom-metadata.dtb -D ${DEPLOYDIR}/qcom-metadata.dtb
}

addtask deploy after do_compile before do_build
