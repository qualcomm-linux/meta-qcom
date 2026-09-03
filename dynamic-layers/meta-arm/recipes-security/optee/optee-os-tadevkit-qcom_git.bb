require optee-os-qcom.inc
require optee-qcom.inc

SUMMARY = "OP-TEE Trusted OS TA devkit"
DESCRIPTION = "OP-TEE TA devkit for build TAs"
HOMEPAGE = "https://www.op-tee.org/"

DEPENDS += "python3-pycryptodome-native"
DEPENDS:append:toolchain-clang = " lld-native"

do_install() {
    #install TA devkit
    install -d ${D}${includedir}/optee/export-user_ta/
    for f in ${B}/export-ta_${OPTEE_ARCH}/* ; do
        cp -aR $f ${D}${includedir}/optee/export-user_ta/
    done
}

do_deploy() {
        echo "Do not inherit do_deploy from optee-os."
}

FILES:${PN} = "${includedir}/optee/"

# Build paths are currently embedded
INSANE_SKIP:${PN}-dev += "buildpaths"
