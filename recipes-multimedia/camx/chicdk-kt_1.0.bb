SUMMARY = "Qualcomm camera User space libraries"

DESCRIPTION = "Collection of prebuilt libraries to support camera downstream functionality."

LICENSE = "CLOSED"

# no top-level dir in the archive, unpack to subdir to prevent UNPACKDIR pollution
SRC_URI = "https://qartifactory-edge.qualcomm.com/artifactory/qsc_releases/software/chip/component/camx.qclinux.0.0/251030/prebuilt_yocto/${BPN}_${PV}_armv8-2a.tar.gz;subdir=${BPN}-${PV}"

SRC_URI[sha256sum] = "75d4b4039ae1a28ad8dc6f98088f8a3531047fcdc10fb23a6c0f86f2444b20ab"

DEPENDS += "camx-kt fastrpc camxlib-kt glib-2.0 libxml2"

do_install() {
    install -d ${D}${libdir}
    cp -r ${S}/usr/lib/* ${D}${libdir}
}

FILES:${PN} = "/usr/lib/*"
FILES:${PN}-dev = ""


INSANE_SKIP = "1"
INSANE_SKIP:${PN} = "already-stripped"
#The modules require .so to be dynamicaly loaded
INSANE_SKIP:${PN} += "dev-so"
INSANE_SKIP:${PN} += "file-rdeps"
