HOMEPAGE = "https://github.com/qualcomm/userspace-resource-manager-extensions"
SUMMARY = "Plugins for extending and customizing URM functionality"
DESCRIPTION = "The URM Extensions framework provides a mechanism for customizing \
URM behaviour, for example: Add new custom resources and signals without modifying \
the core URM codebase, Override default resource handlers with custom implementations, \
Provide target-specific configurations for different hardware platforms. \
The URM Extensions framework allows for extending URM functionality through a clean plugin architecture."

LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=2998c54c288b081076c9af987bdf4838"

SRC_URI = "git://github.com/qualcomm/userspace-resource-manager-extensions.git;protocol=https;branch=main;tag=v${PV}"
SRCREV = "330295dd0a54358bee8423eccf52b7d8ba6ee548"

inherit cmake

DEPENDS += " userspace-resource-manager"
FILES:${PN}-dev += "${libdir}/urm/libUrmPlugin.so"
FILES:${PN} += " \
    ${libdir}/urm/libUrmPlugin.so* \
"
