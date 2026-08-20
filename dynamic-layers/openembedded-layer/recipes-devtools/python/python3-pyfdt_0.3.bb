SUMMARY = "Python Flattened Device Tree library"
DESCRIPTION = "Pure-Python library for parsing and writing Flattened \
Device Tree (FDT) blobs. Required by QDTE (qdte-native) for headless \
xbl_config.elf DTB manipulation in the UEFI capsule build pipeline."
HOMEPAGE = "https://github.com/superna9999/pyfdt"
LICENSE = "Apache-2.0"

# The 0.3 sdist on PyPI ships no LICENSE file, so anchor the checksum
# to the Apache-2.0 header embedded at the top of pyfdt/pyfdt.py.
LIC_FILES_CHKSUM = "file://pyfdt/pyfdt.py;beginline=4;endline=18;md5=727e7a76c771b92141ef85ee99d820ff"

SRC_URI[sha256sum] = "61601c2005ff394a25a6c84c6da2088bbf888328038400d27e4eeb1b04b9f4f0"

inherit pypi setuptools3

BBCLASSEXTEND = "native nativesdk"
