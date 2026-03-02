SUMMARY = "QPS615 PCIe Ethernet kernel module and firmware"
DESCRIPTION = "Kernel module and firmware for the QPS615 PCIe Ethernet \
bridge chip. Builds the host driver plus Qualcomm specific platform logic \
and installs the firmware binary for Qualcomm platforms."

LICENSE = "GPL-2.0-only & MIT"
LIC_FILES_CHKSUM = "\
	file://${S}/drivers/net/ethernet/toshiba/tc956x/LICENSE;md5=5cecbcf0c040b635e20026c75b838d63 \
	file://${UNPACKDIR}/firmware/LICENSE;md5=e8623ee428d33d107c80c8991f828bb7 \
"

PV = "6.0.0+git"

inherit module

SRCREV_source = "9faf7ea02e804f3ab86b21ea0e5d41ac40d37f91"
SRCREV_firmware = "f9b0e1bc0f7c3dfc74ad1a46a87efa56885b9288"
SRCREV_FORMAT = "source_firmware"

SRC_URI = "\
	git://github.com/TC956X/TC9564_Host_Driver.git;protocol=https;branch=industrial_limited_tested;name=source;destsuffix=source \
	file://0001-net-ethernet-tc956x-Add-Qualcomm-platform-driver-sup.patch \
	file://0002-net-ethernet-tc956x-Add-persistent-MAC-address-suppo.patch \
	file://0003-net-ethernet-tc956x-Fix-API-changes-for-kernel-6.18-.patch \
	file://0004-net-ethernet-tc956x-add-missing-check-for-CONFIG_PCI.patch \	
	git://github.com/TC956X/TC9564_Firmware.git;protocol=https;branch=industrial_limited_tested;name=firmware;destsuffix=firmware \
"

S = "${UNPACKDIR}/source"
B = "${S}/drivers/net/ethernet/toshiba/tc956x"

EXTRA_OEMAKE += "KCFLAGS='-DTC956X -DCONFIG_TC956X_PLATFORM_SUPPORT -DTC956X_SRIOV_PF' pf=1"

do_install:append() {
	install -d ${D}${nonarch_base_libdir}/firmware/
	install -m 0644 ${UNPACKDIR}/firmware/Bin/TC956X_Firmware_PCIeBridge.bin ${D}${nonarch_base_libdir}/firmware/
}

KERNEL_MODULE_AUTOLOAD += "tc956x_pcie_eth"
FILES:${PN} += "${nonarch_base_libdir}/firmware/TC956X_Firmware_PCIeBridge.bin"
