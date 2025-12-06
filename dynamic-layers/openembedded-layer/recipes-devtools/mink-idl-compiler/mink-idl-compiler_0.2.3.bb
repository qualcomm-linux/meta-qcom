SUMMARY = "Mink IDL compiler"
DESCRIPTION = " \
Mink IDL is used to describe programming interfaces that can be used to communicate across security domain boundaries. \
Once an interface is described in an IDL source file, the Mink IDL compiler can generate target language header files. \
"
HOMEPAGE = "https://github.com/quic/mink-idl-compiler.git"
SECTION = "devel"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=acff25b0ff46523fa016b260dbf64945"

SRC_URI = "git://github.com/quic/mink-idl-compiler.git;branch=main;protocol=https;tag=v${PV}"
SRCREV = "b308d896f37e76b2a9b28b837a9d3db5de99ab3d"

require mink-idl-compiler-crates.inc

inherit cargo cargo-update-recipe-crates

BBCLASSEXTEND = "native"
