inherit module

DESCRIPTION = "QPS615 Driver and Firmware"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "\
	git://github.com/TC956X/TC9564_Host_Driver.git;protocol=https;branch=industrial_limited_tested;name=source;destsuffix=source \
	file://0001-drivers-qps615-Add-QCOM-platform-driver.patch \
	file://0002-drivers-qps615-Add-support-for-persistent-MAC-addres.patch \
	file://0003-drivers-qps615-Add-support-for-Kernel-6.18.patch \
	git://github.com/TC956X/TC9564_Firmware.git;branch=industrial_limited_tested;protocol=https;name=firmware;destsuffix=firmware \
"

# Separate the driver source and firmware when unpacking
SRCREV_source = "9faf7ea02e804f3ab86b21ea0e5d41ac40d37f91"
SRCREV_firmware = "f9b0e1bc0f7c3dfc74ad1a46a87efa56885b9288"
SRCREV_FORMAT   = "source_firmware"

S = "${UNPACKDIR}/source"
B = "${S}/drivers/net/ethernet/toshiba/tc956x"

FILES:${PN} += "${base_libdir}/firmware/TC956X_Firmware_PCIeBridge.bin"

# This package is designed to run exclusively on ARMv8 (aarch64) machines.
# Therefore, builds for other architectures are not necessary and are explicitly excluded.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"

EXTRA_OEMAKE += "KCFLAGS='-DTC956X -DCONFIG_TC956X_PLATFORM_SUPPORT -DTC956X_SRIOV_PF' pf=1"

# Install the firmware to /lib/firmware
do_install:append() {
	# Install firmware binary from the second repo
	install -d ${D}${base_libdir}/firmware/
	install -m 0644 ${UNPACKDIR}/firmware/Bin/TC956X_Firmware_PCIeBridge.bin ${D}${base_libdir}/firmware/	
}
