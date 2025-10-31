SUMMARY = "Qualcomm camera User space libraries"

DESCRIPTION = "Collection of prebuilt libraries to support camera downstream functionality."

LICENSE = "CLOSED"

# no top-level dir in the archive, unpack to subdir to prevent UNPACKDIR pollution
SRC_URI = "https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/camx.qclinux.0.0/251030/prebuilt_yocto/${BPN}_${PV}_armv8-2a.tar.gz;subdir=${BPN}-${PV}"

SRC_URI[sha256sum] = "0cc7b668b512de857910c17605f4c10d645ec02fc53f96a21a63980ff33e868c" 

DEPENDS += "camxlib fastrpc protobuf-native protobuf "

# Install lib file at right location
do_install() {
    install -d ${D}${libdir}
    cp -r ${S}/usr/lib/* ${D}${libdir}

    install -d ${D}${includedir}
    cp -r ${S}/usr/include/* ${D}${includedir}


}


#Skips check for archtecture
#INSANE_SKIP:${PN} = "arch"
# The modules require .so to be dynamicaly loaded
#INSANE_SKIP:${PN} += "dev-so"
# Prebuilt libraries are already stripped
#INSANE_SKIP:${PN} += "already-stripped"

FILES:${PN} = "\
    /usr/lib/*"
FILES:${PN}-dev = "/usr/include/*"

#Skips check for .so symlinks
INSANE_SKIP:${PN} = "dev-so"

