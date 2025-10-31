SUMMARY = "Qualcomm camera User space libraries"

DESCRIPTION = "Collection of prebuilt libraries to support camera downstream functionality."

LICENSE = "CLOSED"

# no top-level dir in the archive, unpack to subdir to prevent UNPACKDIR pollution
SRC_URI = "https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/camx.qclinux.0.0/251030/prebuilt_yocto/${BPN}_${PV}_armv8-2a.tar.gz;subdir=${BPN}-${PV}"

SRC_URI[sha256sum] = "6deebd1220b22c0381a0d32b09e2413ca6e0ab8445c6c5ef43b5cec5c25fe62c"

DEPENDS += "camxlib-kt fastrpc libxml2 protobuf-native protobuf abseil-cpp"


do_install() {
    install -d ${D}${libdir}
    cp -r ${S}/usr/lib/* ${D}${libdir}

    install -d ${D}${includedir}
    cp -r ${S}/usr/include/* ${D}${includedir}

}

FILES:${PN} = "\
    /usr/lib/* "
FILES:${PN}-dev = "/usr/include/*"


INSANE_SKIP:${PN} = "dev-so"
INSANE_SKIP:${PN} += "file-rdeps"


