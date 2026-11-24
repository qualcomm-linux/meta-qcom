do_install:append() {
    install -d ${D}${datadir}/qcom
    for soc in ${CAMXLIB_SKEL_SOCS}; do
        cp -r ${S}/usr/share/qcom/$soc ${D}${datadir}/qcom/
    done

    # copy Deep Learning based binary
    if [ -d ${S}/usr/share/camx ]; then
        cp -r ${S}/usr/share/camx ${D}${datadir}
    fi
}

PACKAGE_BEFORE_PN += "${PN}-skel"
RDEPENDS:${PN} += "${PN}-skel"
FILES:${PN}-skel += "${datadir}/camx ${datadir}/qcom"

# The skel libraries are prebuilt Hexagon DSP binaries, loaded by FastRPC from
# a data directory.
INSANE_SKIP:${PN}-skel += "arch libdir already-stripped dev-so ldflags"
SKIP_FILEDEPS:${PN}-skel = "1"

# Preserve ${PN}-skel naming to avoid ambiguity in package identification.
DEBIAN_NOAUTONAME:${PN}-skel = "1"
