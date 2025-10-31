SUMMARY = "Qualcomm camera User space libraries"

DESCRIPTION = "Collection of prebuilt libraries to support camera downstream functionality."

LICENSE = "CLOSED"

# no top-level dir in the archive, unpack to subdir to prevent UNPACKDIR pollution
SRC_URI = "https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/camx.qclinux.0.0/251030/prebuilt_yocto/${BPN}_${PV}_armv8-2a.tar.gz;subdir=${BPN}-${PV}"

SRC_URI[sha256sum] = "536ec920bbdef60dadf04eb5b7c8c7c1d66ff2e51eaa4400ea16d2ff4518c6f5" 

DEPENDS += "camxcommon fastrpc protobuf-native protobuf libxml2"

# Install lib file at right location

do_install() {
    install -d ${D}${libdir}
    cp -r ${S}/usr/lib/* ${D}${libdir}

    install -d ${D}/lib/firmware
    cp -r ${S}/lib/firmware/* ${D}/lib/firmware
}

# Install firmware file at right location
relocate_firmware_files () {
    install -d ${D}${nonarch_base_libdir}/firmware/
    mv ${D}/lib/* ${D}${nonarch_base_libdir}/
    rm -rf ${D}/lib/
}
do_install[postfuncs] += "relocate_firmware_files"

FILES:${PN} = "\
    /usr/lib/* \
    ${nonarch_base_libdir}/firmware/*"

#Skips check for archtecture
INSANE_SKIP:${PN} = "arch"
# The modules require .so to be dynamicaly loaded
INSANE_SKIP:${PN} += "dev-so"
# Prebuilt libraries are already stripped
INSANE_SKIP:${PN} += "already-stripped"
