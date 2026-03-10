PLATFORM = "lemans"
PBT_BUILD_DATE = "260307"

require common.inc

SRC_URI[camxlib.sha256sum]     = "c808f35f43ea25d526192bca83e1aaa6374b27da28d96723b6abd0a1dfc21693"
SRC_URI[camx.sha256sum]        = "36857e646fe396cafa2460cc1ea32fd0c27fb68f81a24923324479d7d42e6493"
SRC_URI[chicdk.sha256sum]      = "1f5e4074348e849c17ff22d4c6e82278b7d2f688fedb5422c92900e4565dee84"
SRC_URI[camxcommon.sha256sum]  = "53063a4563e74fed24667b6073abb95d365c9dd5a4f8e98a0a60a30a1917fa7d"

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

# Algo librarires are pre-compiled, pre-stripped.
# Skipping QA checks: 'already-stripped' because:
# - Library files are Pre-stripped  (already-stripped)
INSANE_SKIP:${PN} = "already-stripped"
