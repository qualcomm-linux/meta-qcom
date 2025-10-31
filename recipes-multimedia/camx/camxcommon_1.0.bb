SUMMARY = "Qualcomm camera User space libraries"

DESCRIPTION = "Collection of prebuilt libraries to support camera downstream functionality."

LICENSE = "CLOSED"

# no top-level dir in the archive, unpack to subdir to prevent UNPACKDIR pollution
SRC_URI = "https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/camx.qclinux.0.0/251030/prebuilt_yocto/${BPN}_${PV}_armv8-2a.tar.gz;subdir=${BPN}-${PV}"

SRC_URI[sha256sum] = "adc2507ed33e55500c24db90944296550c4c344d5596aa0a9064e709ade8df70"

DEPENDS += "glib-2.0"

do_install() {
    install -d ${D}${libdir}
    cp -r ${S}/usr/lib/* ${D}${libdir}

    install -d ${D}${includedir}
    cp -r ${S}/usr/include/* ${D}${includedir}
}

FILES:${PN}-dev = "/usr/include/*"
FILES:${PN} = "/usr/lib/*"

#Skips check for .so symlinks
INSANE_SKIP:${PN} = "dev-so"
