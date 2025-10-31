SUMMARY = "Qualcomm camera User space libraries"

DESCRIPTION = "Collection of prebuilt libraries to support camera downstream functionality."

LICENSE = "CLOSED"

# no top-level dir in the archive, unpack to subdir to prevent UNPACKDIR pollution
SRC_URI = "https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/camx.qclinux.0.0/251030/prebuilt_yocto/${BPN}_${PV}_armv8-2a.tar.gz;subdir=${BPN}-${PV}"

SRC_URI[sha256sum] = "a4d6ac09e44a2f1cafe37031c1dd0ab7788b987258bfcd42d368ce5b6c8fdba1"

DEPENDS += "fastrpc protobuf-native protobuf protobuf-native libxml2  virtual/egl virtual/libgles2"

# Install lib file at right location
do_install() {
    install -d ${D}${libdir}
    cp -r ${S}/usr/lib/* ${D}${libdir}

    install -d ${D}/lib/firmware
    cp -r ${S}/lib/firmware/* ${D}/lib/firmware
}

relocate_firmware_files () {
    install -d ${D}${nonarch_base_libdir}/firmware/
    mv ${D}/lib/* ${D}${nonarch_base_libdir}/
    rm -rf ${D}/lib/
}
do_install[postfuncs] += "relocate_firmware_files"

FILES:${PN} = "\
    /usr/lib/* \
    ${nonarch_base_libdir}/firmware/*"
FILES:${PN}-dev = ""

#Skips check for archtecture
INSANE_SKIP:${PN}-dbg = "arch"
INSANE_SKIP:${PN} = "arch"
# The modules require .so to be dynamicaly loaded
INSANE_SKIP:${PN} += "dev-so"
INSANE_SKIP:${PN} += "file-rdeps"
# Prebuilt libraries are already stripped
INSANE_SKIP:${PN} += "already-stripped"

