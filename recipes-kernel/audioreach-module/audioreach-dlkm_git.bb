SUMMARY = "Qualcomm AudioReach kernel module"
DESCRIPTION = "This module is designed for integration with the AudioReach framework, allowing flexible deployment of audio algorithms and efficient hardware utilization."

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0a5a2ad232bafb6974f9a29d1ba0f488"

SRC_URI = "git://github.com/AudioReach/audioreach-kernel.git;protocol=https;branch=master"
SRCREV = "c494af74e420661591a3715f02e4f21f4944a0f2"

PV = "0.0+git"

inherit module

MAKE_TARGETS = "modules"

MODULES_MODULE_SYMVERS_LOCATION = "audioreach-driver"

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"
