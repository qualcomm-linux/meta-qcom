SECTION = "kernel"

DESCRIPTION = "Linux ${PV} kernel headers required to build userspace."
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

inherit kernel-arch

COMPATIBLE_MACHINE = "(qcom)"

LINUX_QCOM_GIT ?= "git://git.codelinaro.org/clo/la/kernel/qcom.git;protocol=https"
SRCBRANCH ?= "qclinux.6.12.y"
SRC_URI = "${LINUX_QCOM_GIT};branch=${SRCBRANCH}"

SRCREV = "608736233891ff0d6575f440d94cc2c821a7b87f"
PV = "6.12+git${SRCPV}"

S = "${WORKDIR}/git"

DEPENDS += "flex-native bison-native rsync-native"

do_configure[noexrec] = "1"
do_compile[noexec] = "1"

do_install () {
    cd ${B}
    headerdir=${B}/headers
    kerneldir=${D}${includedir}/linux-kernel-qcom
    install -d $kerneldir

    # Install all headers inside B and copy only required ones to D
    oe_runmake_call -C ${B} ARCH=${ARCH} headers_install O=$headerdir

    if [ -d $headerdir/include/generated ]; then
        mkdir -p $kerneldir/include/generated/
        cp -fR $headerdir/include/generated/* $kerneldir/include/generated/
    fi

    if [ -d $headerdir/arch/${ARCH}/include/generated ]; then
        mkdir -p $kerneldir/arch/${ARCH}/include/generated/
        cp -fR $headerdir/arch/${ARCH}/include/generated/* $kerneldir/arch/${ARCH}/include/generated/
    fi

    if [ -d $headerdir/${includedir} ]; then
        mkdir -p $kerneldir/${includedir}
        cp -fR $headerdir/${includedir}/* $kerneldir/${includedir}
    fi

    # Remove .install and .cmd files
    find $kerneldir/ -type f -name .install | xargs rm -f
    find $kerneldir/ -type f -name "*.cmd" | xargs rm -f
}

# kernel headers are generally machine specific
PACKAGE_ARCH = "${MACHINE_ARCH}"

# Allow to build empty main package, to include -dev package into the SDK
ALLOW_EMPTY_${PN} = "1"

FILES_${PN}-dev += "linux-qcom/*"

INHIBIT_DEFAULT_DEPS = "1"

python () {
    mach_overrides = d.getVar('MACHINEOVERRIDES').split(":")
    if ('qcom-custom-bsp' not in mach_overrides):
        raise bb.parse.SkipRecipe("linux-qcom-headers are compatible only with qcom-custom-bsp")
}
