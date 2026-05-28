SUMMARY = "Camera metadata headers and lib"
DESCRIPTION = "This recipe provides the system camera metadata headers and lib"

HOMEPAGE = "https://android.googlesource.com/platform/system/media"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://NOTICE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRCREV = "375121a4b16141b7177d801bf9506284642a7a56"
SRC_URI = "git://android.googlesource.com/platform/system/media;protocol=https;branch=main \
           file://0001-Create-CMakeListst-for-building-the-android-cam-meta.patch \
           file://0002-Fix-camera-metadata-headers-for-linux-embedded-compi.patch \
           file://0003-Revert-Update-ColorSpaceProfiles-docs-to-reflect-lim.patch \
           file://0004-CAMX-CORE-Add-support-for-YUV-420-P010.patch \
           file://0005-CAMX-IL-Add-tuning-mode-support-for-AI.patch \
           file://0006-CAMX-IL-Add-BT2020_HLG-color-space-profile-definitio.patch \
           "


inherit cmake
FILES:${PN} = "${libdir}/*.so.*"