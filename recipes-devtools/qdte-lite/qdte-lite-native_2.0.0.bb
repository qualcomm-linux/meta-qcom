SUMMARY = "Device Tree Editor Lite (qdte-lite)"
DESCRIPTION = "Lightweight fork of the Qualcomm Device Tree Editor"
HOMEPAGE = "https://github.com/qualcomm/qdte-lite"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=57272fa9cc740c745feb331231cca6f2"

SRC_URI = "git://github.com/qualcomm/qdte-lite.git;protocol=https;branch=main;tag=v${PV} \
           file://0001-fix-emit-trees-whose-size-lands-on-an-FdtSw-growth-bo.patch \
           "
SRCREV = "21db12fa954d009b6e501d8bd12d22c8e6e7aaa1"

inherit python_setuptools_build_meta native

DEPENDS += "python3-dtc-native"
