DESCRIPTION = "Qualcomm Camera driver (CAMX)"
HOMEPAGE = "https://github.com/qualcomm-linux/camera-driver"
LICENSE = "GPL-2.0-only"

LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263"
 
PV = "0.0+git"
 
SRC_URI = "git://github.com/qualcomm-linux/camera-driver.git;protocol=https;branch=camera-kernel.qclinux.0.0"

SRCREV = "3de16d9361d35cfc99bad2bbae4d38777c24100f"

inherit module

MAKE_TARGETS = "modules"
MODULES_INSTALL_TARGET = "modules_install"
