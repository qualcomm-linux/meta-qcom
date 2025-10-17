SECTION = "kernel"

DESCRIPTION = "Linux ${PV} kernel for QCOM devices"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

inherit kernel

COMPATIBLE_MACHINE = "(qcom)"

LINUX_VERSION ?= "6.17"

PV = "${LINUX_VERSION}+git"

# tag: qcom-next-6.17-20251006
SRCREV ?= "7c504609df4a658e3cbc81b44e859e46865b3237"

SRCBRANCH ?= "nobranch=1"
SRCBRANCH:class-devupstream ?= "branch=qcom-next"

SRC_URI = "git://github.com/qualcomm-linux/kernel.git;${SRCBRANCH};protocol=https"

# Additional kernel configs.
SRC_URI += " \
    file://configs/qcom.cfg \
"

# To build tip of qcom-next branch set preferred
# virtual/kernel provider to 'linux-qcom-next-upstream'
BBCLASSEXTEND = "devupstream:target"
PN:class-devupstream = "linux-qcom-next-upstream"
SRCREV:class-devupstream ?= "${AUTOREV}"

S = "${UNPACKDIR}/${BP}"

KBUILD_DEFCONFIG ?= "defconfig"
KBUILD_DEFCONFIG:qcom-armv7a = "qcom_defconfig"

KERNEL_CONFIG_FRAGMENTS ?= "${UNPACKDIR}/configs/qcom.cfg"

do_configure:prepend() {
    # Use a copy of the 'defconfig' from the actual repo to merge fragments
    cp ${S}/arch/${ARCH}/configs/${KBUILD_DEFCONFIG} ${B}/.config

    for f in ${KERNEL_CONFIG_FRAGMENTS}
    do
        # Check if the config fragment was copied into the UNPACKDIR
        if [ ! -e "$f" ]
        then
            echo "Could not find kernel config fragment $f"
            exit 1
        fi
    done

    # Merge fragment for QCOM value add features
    ${S}/scripts/kconfig/merge_config.sh -m -O ${B} ${B}/.config ${KERNEL_CONFIG_FRAGMENTS}
}
