# Shikra settings for the U-Boot SPL FIT boot flow: the memory layout, the
# recipes providing the images and the contents of the FIT. Only the machines
# using that flow need them, so they require this file along with
# conf/machine/include/qcom-uboot-spl-fit.inc.

# Memory layout needed by the SPL FIT boot flow.
QCOM_UBOOT_SPL_ENTRY ?= "0x0c219000"
UBOOT_FIT_ARM_TRUSTED_FIRMWARE_LOADADDRESS ?= "0xa1300000"
UBOOT_FIT_ARM_TRUSTED_FIRMWARE_ENTRYPOINT ?= "0xa1300000"
UBOOT_FIT_TEE_LOADADDRESS ?= "0xa1400000"
UBOOT_FIT_TEE_ENTRYPOINT ?= "0xa1400000"
UBOOT_FIT_UBOOT_LOADADDRESS ?= "0x9f400000"
UBOOT_FIT_UBOOT_ENTRYPOINT ?= "0x9f400000"

# Recipes providing BL31, OP-TEE and the boot firmware for the SPL FIT boot
# flow, and the XBL-sec ELF taken from the boot firmware.
QCOM_UBOOT_SPL_FIT_ATF ?= "trusted-firmware-a-qcom-shikra"
QCOM_UBOOT_SPL_FIT_TEE ?= "optee-os-qcom-shikra"
QCOM_UBOOT_SPL_FIT_FIRMWARE ?= "firmware-qcom-boot-shikra"
QCOM_UBOOT_SPL_FIT_XBL_SEC ?= "qc_sec.mbn"

# The Shikra SPL loads the images from data stored after the FIT.
UBOOT_FIT_MKIMAGE_EXTRA_OPTS ?= "-E"

# SHRM, QcLib and RPM come split per ELF segment, with their load and entry
# addresses in the release's fit-elf/manifest.json.
QCOM_UBOOT_SPL_FIT_SEGMENTS ?= "fit-elf"

# The PMIC, DCB, SHRM, QcLib, ECC and RPM blobs, all from
# QCOM_UBOOT_SPL_FIT_FIRMWARE. All but RPM are loaded by the SPL at the
# pre-ddr stage; RPM goes with BL31/OP-TEE/U-Boot at post-ddr.
UBOOT_FIT_LOADABLES ?= "\
    pmic_cocos pmic_wailua dcb_lp4 dcb_lp5 ecc \
    shrm_1 shrm_2 qclib_1 qclib_2 rpm_1 rpm_2 \
"

UBOOT_FIT_LOADABLE_DESCRIPTION[pmic_cocos] ?= "PMIC Cocos Config"
UBOOT_FIT_LOADABLE_FILENAME[pmic_cocos] ?= "Pmic_Cocos.bin"
UBOOT_FIT_LOADABLE_LOADADDRESS[pmic_cocos] ?= "0x0c294800"
UBOOT_FIT_LOADABLE_ENTRYPOINT[pmic_cocos] ?= "0x0c294800"
UBOOT_FIT_LOADABLE_OS[pmic_cocos] ?= "elf"

UBOOT_FIT_LOADABLE_DESCRIPTION[pmic_wailua] ?= "PMIC Wailua Config"
UBOOT_FIT_LOADABLE_FILENAME[pmic_wailua] ?= "Pmic_Wailua.bin"
UBOOT_FIT_LOADABLE_LOADADDRESS[pmic_wailua] ?= "0x0c2a0400"
UBOOT_FIT_LOADABLE_ENTRYPOINT[pmic_wailua] ?= "0x0c2a0400"

UBOOT_FIT_LOADABLE_DESCRIPTION[dcb_lp4] ?= "DDR Config Block (LP4)"
UBOOT_FIT_LOADABLE_FILENAME[dcb_lp4] ?= "900B_7_0100_1_dcb.bin"
UBOOT_FIT_LOADABLE_LOADADDRESS[dcb_lp4] ?= "0x0c2a6400"
UBOOT_FIT_LOADABLE_ENTRYPOINT[dcb_lp4] ?= "0x0c2a6400"

UBOOT_FIT_LOADABLE_DESCRIPTION[dcb_lp5] ?= "DDR Config Block (LP5)"
UBOOT_FIT_LOADABLE_FILENAME[dcb_lp5] ?= "900B_7_0100_0_dcb.bin"
UBOOT_FIT_LOADABLE_LOADADDRESS[dcb_lp5] ?= "0x0c2abc00"
UBOOT_FIT_LOADABLE_ENTRYPOINT[dcb_lp5] ?= "0x0c2abc00"

UBOOT_FIT_LOADABLE_DESCRIPTION[ecc] ?= "ECC Config"
UBOOT_FIT_LOADABLE_FILENAME[ecc] ?= "Shikra_LCP.bin"
UBOOT_FIT_LOADABLE_LOADADDRESS[ecc] ?= "0x0c2b1400"
UBOOT_FIT_LOADABLE_ENTRYPOINT[ecc] ?= "0x0c2b1400"

# The segments take their file, addresses, arch and os from the manifest.
UBOOT_FIT_LOADABLE_DESCRIPTION[shrm_1] ?= "SHRM segment 1"
UBOOT_FIT_LOADABLE_DESCRIPTION[shrm_2] ?= "SHRM segment 2"
UBOOT_FIT_LOADABLE_DESCRIPTION[qclib_1] ?= "QcLib segment 1"
UBOOT_FIT_LOADABLE_DESCRIPTION[qclib_2] ?= "QcLib segment 2"
UBOOT_FIT_LOADABLE_DESCRIPTION[rpm_1] ?= "RPM segment 1"
UBOOT_FIT_LOADABLE_DESCRIPTION[rpm_2] ?= "RPM segment 2"

UBOOT_FIT_CONF_LOADABLES ?= "rpm_1 rpm_2"

# The SPL picks the configuration by description, "pre-ddr" ahead of DDR
# init and "post-ddr" (set in qcom-uboot-spl-fit.inc) after it.
UBOOT_FIT_CONFIGURATIONS ?= "pre-ddr"
UBOOT_FIT_CONFIGURATION_DESCRIPTION[pre-ddr] ?= "pre-ddr"
UBOOT_FIT_CONFIGURATION_LOADABLES[pre-ddr] ?= "\
    pmic_cocos pmic_wailua dcb_lp4 dcb_lp5 ecc \
    shrm_1 shrm_2 qclib_1 qclib_2 \
"
