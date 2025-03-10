SECTION = "kernel"

DESCRIPTION = "Linux ${PV} staging kernel for QCOM devices"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

inherit kernel

COMPATIBLE_MACHINE = "(qcom)"

LINUX_QCOM_GIT ?= "git://git.codelinaro.org/clo/la/kernel/qcom.git;protocol=https"
SRCBRANCH ?= "qclinux.6.12.y"
SRC_URI = "${LINUX_QCOM_GIT};branch=${SRCBRANCH}"

# To build bleeding edge qcom staging kernel set preferred
# provider of virtual/kernel to 'linux-qcom-staging-tip'
BBCLASSEXTEND = "devupstream:target"
PN:class-devupstream = "linux-qcom-staging-tip"
SRCREV:class-devupstream = "${AUTOREV}"

SRCREV = "10325dc2277dc03687683b9f5f57a472b37de0b4"
PV = "6.12+git"

S = "${WORKDIR}/git"

KERNEL_CONFIG ?= "qcom_defconfig"

# Additional fragment for qcom value add features
KERNEL_CONFIG_FRAGMENTS += " ${S}/arch/arm64/configs/qcom_addons.config"

do_configure:prepend() {
    if [ ! -f "${S}/arch/${ARCH}/configs/${KERNEL_CONFIG}" ]; then
        bbfatal "KERNEL_CONFIG '${KERNEL_CONFIG}' was specified, but not present in the source tree"
    else
        cp '${S}/arch/${ARCH}/configs/${KERNEL_CONFIG}' '${B}/.config'
    fi

    # Check for kernel config fragments.  The assumption is that the config
    # fragment will be specified with the absolute path.  For example:
    #   * ${WORKDIR}/config1.cfg
    #   * ${S}/config2.cfg
    # Iterate through the list of configs and make sure that you can find
    # each one.  If not then error out.
    # NOTE: If you want to override a configuration that is kept in the kernel
    #       with one from the OE meta data then you should make sure that the
    #       OE meta data version (i.e. ${WORKDIR}/config1.cfg) is listed
    #       after the in kernel configuration fragment.
    # Check if any config fragments are specified.
    if [ ! -z "${KERNEL_CONFIG_FRAGMENTS}" ]
    then
        for f in ${KERNEL_CONFIG_FRAGMENTS}
        do
            # Check if the config fragment was copied into the WORKDIR from
            # the OE meta data
            if [ ! -e "$f" ]
            then
                echo "Could not find kernel config fragment $f"
                exit 1
            fi
        done

        # Now that all the fragments are located merge them.
        ( cd ${WORKDIR} && ${S}/scripts/kconfig/merge_config.sh -m -r -O ${B} ${B}/.config ${KERNEL_CONFIG_FRAGMENTS} 1>&2 )
    fi
}
