SUMMARY = "qdl -- Qualcomm Download Tool"
DESCRIPTION = "Userspace tool to flash images to Qualcomm SoCs over the \
EDL (Emergency Download) USB protocol"

HOMEPAGE = "https://github.com/linux-msm/qdl"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=da6bfde9cb5bc5120a51775381f6edf1"

SRC_URI = "git://github.com/linux-msm/qdl.git;protocol=https;branch=master;tag=v${PV}"
SRCREV = "f540e59dbeb462242239778ae0993549304f3abb"

# qdl links against libusb-1.0, libxml-2.0 and libzip; all three are
# packaged as -native in OE-core.  meson/ninja and pkgconfig provided
# by the meson + pkgconfig classes.
DEPENDS = "libusb1-native libxml2-native libzip-native"

inherit native meson pkgconfig