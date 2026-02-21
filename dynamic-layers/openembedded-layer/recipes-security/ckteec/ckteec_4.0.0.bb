SUMMARY = "QTEE PKCS#11 client library"
DESCRIPTION = "Provides prebuilt binaries and headers for libckteec, the QTEE PKCS#11 client-side library used by host applications to interact with QTEE's PKCS#11 TA."

LICENSE = "LICENSE.qcom-2"
LIC_FILES_CHKSUM = "file://NO.LOGIN.BINARY.LICENSE.QTI.pdf;md5=4ceffe94cb40cdce6d2f4fb93cc063d1 \
                    file://NOTICE;md5=70c0f008eb9c6c6e34b35f7605f542a2"

# PBT_BUILD_DATE = "260112.1"
SRC_URI = "file:///local/mnt/workspace/prebuilds/libckteec/${BPN}_${PV}_armv8-2a.tar.gz"
SRC_URI[sha256sum] = "c318552c2a0ff4227271427fa72671f5ebd4569ea32eeb60ed252d27c2d49e45"

S = "${UNPACKDIR}"

# Dependencies.
DEPENDS += "minkipc"

# This package is currently only used and tested on ARMv8 (aarch64) machines.
# Therefore, builds for other architectures are not necessary and are explicitly excluded.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"

do_install() {
    install -d ${D}${includedir}
    install -d ${D}${libdir}

    # Install headers
    cp -r ${S}/usr/include/* ${D}${includedir}/

    # Install libs
    cp -r ${S}/usr/lib/* ${D}${libdir}/
}

# Prebuilt binaries are stripped; silence that specific QA if needed
INSANE_SKIP:${PN} += "already-stripped"

# Prevent dynamic renaming of packages
AUTO_LIBNAME_PKGS = ""