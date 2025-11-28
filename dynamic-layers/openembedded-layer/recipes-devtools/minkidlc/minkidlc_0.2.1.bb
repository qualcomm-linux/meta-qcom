SUMMARY = "Mink IDL compiler"
DESCRIPTION = " \
Mink IDL is used to describe programming interfaces that can be used to communicate across security domain boundaries. \
Once an interface is described in an IDL source file, the Mink IDL compiler can generate target language header files. \
"
HOMEPAGE = "https://github.com/quic/mink-idl-compiler.git"
SECTION = "devel"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=acff25b0ff46523fa016b260dbf64945"

SRC_URI = "git://github.com/quic/mink-idl-compiler.git;branch=main;protocol=https"
SRCREV = "2b12abb0fda64ba6144dc39b7d18029e1e994796"

require minkidlc-crates.inc

inherit cargo cargo-update-recipe-crates

DEPENDS += "rust-native"

BBCLASSEXTEND = "native"
