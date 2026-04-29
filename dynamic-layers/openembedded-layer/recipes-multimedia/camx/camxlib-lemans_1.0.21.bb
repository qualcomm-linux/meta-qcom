PLATFORM = "lemans"
PBT_BUILD_DATE = "260428.2"

require common.inc

SRC_URI[camxlib.sha256sum]     = "fcd93e8015461e71098017cfe157bf5c3c5183912bdd42906eeac253783e9a38"
SRC_URI[camx.sha256sum]        = "de99e8e74fb4d5d396d66a4000e369c5751bc76d6fbb3979e3a9efa6b32fbd73"
SRC_URI[chicdk.sha256sum]      = "1769d0f4954962c337de8d117ae74c31f571960f3574b34256192e53cfbd871d"
SRC_URI[camxcommon.sha256sum]  = "bf5255e784f222573f7fdfb14ea979b329e23ea5b54d204ac5fedb6bcaa4fe88"

DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'opencl', 'qcom-adreno virtual/libopencl1', '', d)}"

do_install:append() {
    # Copy json only when /etc folder exists in ${S}
    if [ -d "${S}/etc" ]; then
        install -d ${D}${sysconfdir}/camera/test/NHX/
        cp -r ${S}/etc/camera/test/NHX/*.json ${D}${sysconfdir}/camera/test/NHX/
    fi
}

RPROVIDES:${PN} = "camxlib-monaco"
PACKAGE_BEFORE_PN += "camx-nhx"
RRECOMMENDS:${PN} += "${@bb.utils.contains('DISTRO_FEATURES', 'opencl', 'virtual-opencl-icd', '', d)}"

FILES:camx-nhx = "\
    ${bindir}/nhx.sh \
    ${sysconfdir}/camera/test/NHX/ \
"

FILES:${PN} += "${@bb.utils.contains('DISTRO_FEATURES', 'opencl', '${libdir}/camx/${PLATFORM}/*.cl ${libdir}/camx/${PLATFORM}/libmctf_cl_program.bin', '', d)}"
