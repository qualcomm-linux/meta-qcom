HOMEPAGE = "https://libssc.dylanvanassche.be/"
SUMMARY = "a library to expose the sensors managed by the Qualcomm Sensor Core"
DESCRIPTION = "Libssc is a library to expose the sensors managed by the Qualcomm Sensor Core found in many Qualcomm System-on-Chips (SoCs) from 2018 and onwards."

LICENSE = "GPL-3.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"

SRC_URI = "git://codeberg.org/DylanVanAssche/libssc.git;protocol=https;branch=main"
SRCREV = "5e699fc97d92684c6882741fc9f71cb57a33e25b"

PV = "0.2.2+"

inherit meson pkgconfig gobject-introspection

# There is no separate meson option
GIR_MESON_OPTION = ""

DEPENDS = "glib-2.0 libqmi protobuf-c protobuf-c-native"

do_install:append() {
    # no need to ship mocking server
    rm -rf ${D}${libdir}/python*
}
