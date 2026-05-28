SUMMARY = "Camera Metadata"
DESCRIPTION = "Camera Metadata"
HOMEPAGE = "https://android.googlesource.com/platform/system/media"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI = "git://android.googlesource.com/platform/system/media;protocol=https;branch=main \
           file://0001-Fix-camera-metadata-headers.patch \
           file://0002-Fix-camera-metadata-tags-for-color-space-profiles-ma.patch "


SRCREV = "375121a4b16141b7177d801bf9506284642a7a56"
PV = "1.0+git${SRCPV}"

inherit cmake

do_install:append() {
    # Install headers
    install -d ${D}${includedir}
    cp -r ${S}/camera/include/system ${D}${includedir}/
    cp -r ${S}/camera/include/system/*.h ${D}${includedir}/
    cp ${S}/private/camera/include/camera_metadata_hidden.h ${D}${includedir}/
}

do_package_qa[noexec] = "1"
FILES:${PN} = "${libdir}/*"
FILES:${PN}-dev = "${includedir}/*"
INSANE_SKIP = "1"
#Skips check for .so symlinks
INSANE_SKIP:${PN} = "already-stripped"
