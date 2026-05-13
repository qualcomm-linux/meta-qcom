PLATFORM = "lemans"
PBT_BUILD_DATE = "260512.2"

require common.inc

SRC_URI[camxlib.sha256sum] = "6b3543e5234205174434ee9db42d7d7fc55958a8ca0f006cce0d2d00a0a96806"
SRC_URI[camx.sha256sum] = "663fcc6e18e6a7d0bcff67546b0e3805c30c1dd969429b31a3623e733370fac3"
SRC_URI[chicdk.sha256sum] = "07044c75046a0caf9c16193411748d6d8eae0faf2d8668e321e74cca0279f806"
SRC_URI[camxcommon.sha256sum] = "b3419ffac8927f1252cd50a15183d93de6ee2ed7a58641f408b7002491ee4369"

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
