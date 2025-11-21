SUMMARY = "Mink IDL compiler (prebuilt binary)"
DESCRIPTION = " \
Mink IDL is used to describe programming interfaces that can be used to communicate across security domain boundaries. \
Once an interface is described in an IDL source file, the Mink IDL compiler can generate target language header files. \
"
HOMEPAGE = "https://github.com/quic/mink-idl-compiler.git"
SECTION = "devel"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=acff25b0ff46523fa016b260dbf64945"

SRC_URI += "git://github.com/quic/mink-idl-compiler.git;branch=main;protocol=https"
SRCREV = "1a52cd017ae21750c23e01851fdffd143f9c85d0"
require minkidlc-crates.inc

inherit cargo cargo-update-recipe-crates

DEPENDS += "rust-native cargo-native"

BBCLASSEXTEND += "native"
