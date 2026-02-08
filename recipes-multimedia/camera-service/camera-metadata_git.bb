SUMMARY = "Android Camera metadata headers"
DESCRIPTION = "Installs the AOSP camera metadata headers"
HOMEPAGE = "https://source.android.com/docs/core/camera"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://NOTICE;md5=3b83ef96387f14655fc854ddc3c6bd57"

inherit cmake

SRC_URI = "\
    git://android.googlesource.com/platform/system/media;protocol=https;branch=main \
    file://0001-camera-metadata-install-metadata-header.patch \
"

SRCREV = "d0c3ff3c62e3fdb873dc1f8cc66c0a9f68ffc7b0"
