SUMMARY = "Qualcomm Linux Embedded Camera Service"
DESCRIPTION = "Qualcomm Linux Embedded Camera Service with client and server modules."
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=2998c54c288b081076c9af987bdf4838"

HOMEPAGE = "https://github.com/qualcomm/camera-service"

SRC_URI = "\
    git://github.com/qualcomm/camera-service.git;branch=main;protocol=https \
"

SRCREV = "47fe2cb12f283c46b865733a9673987bc22ad5e5"

PV = "0.0+git"

# Limit this recipe to ARMv8 (aarch64) only, because it depends
# on camxcommon-headers which is explicitly restricted to ARMv8 (aarch64).
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"

inherit cmake pkgconfig systemd

DEPENDS += "\
    camxcommon-headers \
    gtest \
    protobuf-camx \
    protobuf-camx-native \
"

SYSTEMD_SERVICE:${PN} = "cam-server.service"

# Split the recipe output into logical subpackages.
PACKAGE_BEFORE_PN = " \
    ${PN}-common \
    ${PN}-client \
    ${PN}-server-lib \
    ${PN}-server-lib-kodiak \
"

# Common libraries used by server and client
FILES:${PN}-common = " \
    ${libdir}/libqmmf_camera_metadata.so.* \
    ${libdir}/libqmmf_config.so.* \
    ${libdir}/libqmmf_proto.so.* \
    ${libdir}/libqmmf_utils.so.* \
"

# Client libraries.
FILES:${PN}-client += " \
    ${libdir}/libqmmf_recorder_client.so.* \
"

# Server libraries (generic variant).
FILES:${PN}-server-lib = " \
    ${libdir}/libqmmf_camera_adaptor.so.* \
    ${libdir}/libqmmf_memory_interface.so.* \
    ${libdir}/libqmmf_recorder_service.so.* \
"

# Server libraries for the "kodiak" variant.
FILES:${PN}-server-lib-kodiak = " \
    ${libdir}/libqmmf_camera_adaptor_kodiak.so.* \
    ${libdir}/libqmmf_memory_interface_kodiak.so.* \
    ${libdir}/libqmmf_recorder_service_kodiak.so.* \
"

# Add runtime deps since the server dlopens the recorder service library
RDEPENDS:${PN} += " \
    ${PN}-server-lib \
    ${PN}-server-lib-kodiak \
"

# Add camx libraries as runtime deps since they are dlopened
RDEPENDS:${PN}-server-lib += "camxlib-lemans"
RDEPENDS:${PN}-server-lib-kodiak += "camxlib-kodiak"

# Preserve ${PN} naming to avoid ambiguity in package identification.
DEBIAN_NOAUTONAME:${PN}-client = "1"
