PLATFORM = "lemans"
PBT_BUILD_DATE = "260302.1"

require common.inc

SRC_URI[camxlib.sha256sum]     = "28505fd1fd2540fe39d93e43578c64423d9f6d30f90a31c81244db92f8e65b00"
SRC_URI[camx.sha256sum]        = "872737535cd6041761f794ddc9660cd046a7c81ea842db159afb71521ef6c0ab"
SRC_URI[chicdk.sha256sum]      = "94edcee66a5e375db88eb57094b2da6b14faf96002d80892412c243b63dff871"
SRC_URI[camxcommon.sha256sum]  = "1cdecf59c7d08fb60b323def8d7b2acbf941f274b9843b02af60f7d954032a27"

DEPENDS:append = "${@bb.utils.contains('DISTRO_FEATURES', 'opencl', ' qcom-adreno virtual/libopencl1', '', d)}"

do_install:append() {
    # Copy json only when /etc folder exists in ${S}
    if [ -d "${S}/etc" ]; then
        install -d ${D}${sysconfdir}/camera/test/NHX/
        cp -r ${S}/etc/camera/test/NHX/*.json ${D}${sysconfdir}/camera/test/NHX/
    fi
}

RPROVIDES:${PN} = "camxlib-monaco"
PACKAGE_BEFORE_PN += "camx-nhx"
RRECOMMENDS:${PN}:append = "${@bb.utils.contains('DISTRO_FEATURES', 'opencl', ' virtual-opencl-icd', '', d)}"

FILES:camx-nhx = "\
    ${bindir}/nhx.sh \
    ${sysconfdir}/camera/test/NHX/ \
"

FILES:${PN} += "${@bb.utils.contains('DISTRO_FEATURES', 'opencl', '${libdir}/camx/${PLATFORM}/*.cl ${libdir}/camx/${PLATFORM}/libmctf_cl_program.bin', '', d)}"

# Algo librarires are pre-compiled, pre-stripped.
# Skipping QA checks: 'already-stripped' because:
# - Library files are Pre-stripped  (already-stripped)
INSANE_SKIP:${PN} = "already-stripped"
