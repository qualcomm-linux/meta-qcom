SUMMARY = "Android Qualcomm Image Unpacker"
HOMEPAGE = "https://github.com/anestisb/qc_image_unpacker"
SECTION = "devel"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://../LICENSE;md5=138532bb21858341808df2740a1d13bf"

SRC_URI = " \
    git://github.com/anestisb/qc_image_unpacker;protocol=https;branch=master\
    file://0004-Fail-if-an-image-can-not-be-opened.patch;patchdir=.. \
"

SRCREV = "28a783a9fc25dc87b7416b6d5b6f9ccd497d1c2e"

PV = "0.2.0+"
S = "${UNPACKDIR}/${BP}/src"

DEPENDS = "zlib"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/qc_image_unpacker ${D}${bindir} 
}

BBCLASSEXTEND = "native nativesdk"
