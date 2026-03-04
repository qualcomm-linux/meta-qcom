PLATFORM = "lemans"
PBT_BUILD_DATE = "260224.1"

require common.inc

SRC_URI[camxlib.sha256sum]     = "7b513e631acac4e62202f96acf7ca15dfe322094d11234288b89c529634e37f1"
SRC_URI[camx.sha256sum]        = "64f30d84d8c0b5dfcada0f3bbabc884b3d536b7c91d471404d20e55b3dcf69a1"
SRC_URI[chicdk.sha256sum]      = "02ad87b0e960364dcb4bb716b2a55f9498df14838ddc249973670fdb2b9e1111"
SRC_URI[camxcommon.sha256sum]  = "ec8481f6b5360336de9b1092c5a7acd2f90d0890993dc64233eaef1a67ec8866"

DEPENDS:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'opencl', 'qcom-adreno virtual/libopencl1', '', d)}"

do_install:append() {
    # Copy json only when /etc folder exists in ${S}
    if [ -d "${S}/etc" ]; then
        install -d ${D}${sysconfdir}/camera/test/NHX/
        cp -r ${S}/etc/camera/test/NHX/*.json ${D}${sysconfdir}/camera/test/NHX/
    fi
}

RPROVIDES:${PN} = "camxlib-monaco"
PACKAGE_BEFORE_PN += "camx-nhx"
RRECOMMENDS:${PN}:append = " ${@bb.utils.contains('DISTRO_FEATURES', 'opencl', 'virtual-opencl-icd', '', d)}"

FILES:camx-nhx = "\
    ${bindir}/nhx.sh \
    ${sysconfdir}/camera/test/NHX/ \
"

FILES:${PN} += "${@bb.utils.contains('DISTRO_FEATURES', 'opencl', '${libdir}/camx/${PLATFORM}/*.cl ${libdir}/camx/${PLATFORM}/libmctf_cl_program.bin', '', d)}"

# Algo librarires are pre-compiled, pre-stripped.
# Skipping QA checks: 'already-stripped' because:
# - Library files are Pre-stripped  (already-stripped)
INSANE_SKIP:${PN} = "already-stripped"
