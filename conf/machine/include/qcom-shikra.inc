# Configurations and variables for Shikra SoC family.

SOC_FAMILY = "shikra"
require conf/machine/include/qcom-base.inc
require conf/machine/include/qcom-common.inc

DEFAULTTUNE = "armv8-2a-crypto"
require conf/machine/include/arm/arch-armv8-2a.inc

MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS += " \
    packagegroup-qcom-boot-essential \
    packagegroup-machine-essential-qcom-shikra-soc \
"

MACHINE_EXTRA_RRECOMMENDS += " \
    packagegroup-qcom-boot-additional \
"

# Memory layout needed by the SPL FIT boot flow.
QCOM_UBOOT_SPL_ENTRY ?= "0x0C219000"
UBOOT_FIT_ARM_TRUSTED_FIRMWARE_LOADADDRESS ?= "0xA1300000"
UBOOT_FIT_ARM_TRUSTED_FIRMWARE_ENTRYPOINT ?= "0xA1300000"
UBOOT_FIT_TEE_LOADADDRESS ?= "0xA1400000"
UBOOT_FIT_TEE_ENTRYPOINT ?= "0xA1400000"
UBOOT_FIT_UBOOT_LOADADDRESS ?= "0x87980000"
UBOOT_FIT_UBOOT_ENTRYPOINT ?= "0x87980000"

# Recipes providing BL31, OP-TEE and the XBL-sec ELF for the SPL FIT boot flow.
QCOM_UBOOT_SPL_FIT_ATF ?= "trusted-firmware-a-qcom-shikra"
QCOM_UBOOT_SPL_FIT_TEE ?= "optee-os-qcom-shikra"
QCOM_UBOOT_SPL_FIT_XBL_SEC ?= "firmware-qcom-boot-shikra"

# PMIC, DCB, SHRM, QcLib and ECC configs are provided by
# firmware-qcom-boot-shikra and loaded by the SPL at the pre-ddr stage, ahead
# of BL31/OP-TEE/U-Boot (post-ddr). dpr rides the same staging mechanism on
# other machines but is unused here.
QCOM_UBOOT_SPL_FIT_EXTRA_LOADABLES ?= "\
    firmware-qcom-boot-shikra:Pmic_Cocos.bin \
    firmware-qcom-boot-shikra:Pmic_Wailua.bin \
    firmware-qcom-boot-shikra:900B_7_0100_0_dcb.bin \
    firmware-qcom-boot-shikra:900B_7_0100_1_dcb.bin \
    firmware-qcom-boot-shikra:shrm.elf \
    firmware-qcom-boot-shikra:QcLib.elf \
    firmware-qcom-boot-shikra:Shikra_LCP.bin \
"

QCOM_UBOOT_SPL_FIT_PRE_DDR_LOADABLES ?= "pmic_cocos pmic_wailua dcb_lp4 dcb_lp5 shrm qclib ecc"

UBOOT_FIT_PMIC_COCOS_LOADADDRESS ?= "0x0C294800"
UBOOT_FIT_PMIC_COCOS_ENTRYPOINT ?= "0x0C294800"
UBOOT_FIT_PMIC_WAILUA_LOADADDRESS ?= "0x0C2A0400"
UBOOT_FIT_PMIC_WAILUA_ENTRYPOINT ?= "0x0C2A0400"
UBOOT_FIT_DCB_LP4_LOADADDRESS ?= "0x0C2A6400"
UBOOT_FIT_DCB_LP4_ENTRYPOINT ?= "0x0C2A6400"
UBOOT_FIT_DCB_LP5_LOADADDRESS ?= "0x0C2AAC00"
UBOOT_FIT_DCB_LP5_ENTRYPOINT ?= "0x0C2AAC00"
UBOOT_FIT_SHRM_LOADADDRESS ?= "0xC00000"
UBOOT_FIT_SHRM_ENTRYPOINT ?= "0xC00000"
UBOOT_FIT_QCLIB_LOADADDRESS ?= "0x0C2C3800"
UBOOT_FIT_QCLIB_ENTRYPOINT ?= "0x0C2C5270"
UBOOT_FIT_ECC_LOADADDRESS ?= "0x0C2AF400"
UBOOT_FIT_ECC_ENTRYPOINT ?= "0x0C2AF400"

UBOOT_FIT_USER_SETTINGS ?= "\
        pmic_cocos {\n\
            description = \"PMIC Cocos Config\";\n\
            data = /incbin/(\"Pmic_Cocos.bin\");\n\
            type = \"firmware\";\n\
            arch = \"${UBOOT_ARCH}\";\n\
            compression = \"none\";\n\
            load = <${UBOOT_FIT_PMIC_COCOS_LOADADDRESS}>;\n\
            entry = <${UBOOT_FIT_PMIC_COCOS_ENTRYPOINT}>;\n\
        };\n\
        pmic_wailua {\n\
            description = \"PMIC Wailua Config\";\n\
            data = /incbin/(\"Pmic_Wailua.bin\");\n\
            type = \"firmware\";\n\
            arch = \"${UBOOT_ARCH}\";\n\
            compression = \"none\";\n\
            load = <${UBOOT_FIT_PMIC_WAILUA_LOADADDRESS}>;\n\
            entry = <${UBOOT_FIT_PMIC_WAILUA_ENTRYPOINT}>;\n\
        };\n\
        dcb_lp4 {\n\
            description = \"DDR Config Block (LP4)\";\n\
            data = /incbin/(\"900B_7_0100_0_dcb.bin\");\n\
            type = \"firmware\";\n\
            arch = \"${UBOOT_ARCH}\";\n\
            compression = \"none\";\n\
            load = <${UBOOT_FIT_DCB_LP4_LOADADDRESS}>;\n\
            entry = <${UBOOT_FIT_DCB_LP4_ENTRYPOINT}>;\n\
        };\n\
        dcb_lp5 {\n\
            description = \"DDR Config Block (LP5)\";\n\
            data = /incbin/(\"900B_7_0100_1_dcb.bin\");\n\
            type = \"firmware\";\n\
            arch = \"${UBOOT_ARCH}\";\n\
            compression = \"none\";\n\
            load = <${UBOOT_FIT_DCB_LP5_LOADADDRESS}>;\n\
            entry = <${UBOOT_FIT_DCB_LP5_ENTRYPOINT}>;\n\
        };\n\
        shrm {\n\
            description = \"SHRM\";\n\
            data = /incbin/(\"shrm.elf\");\n\
            type = \"firmware\";\n\
            arch = \"${UBOOT_ARCH}\";\n\
            compression = \"none\";\n\
            load = <${UBOOT_FIT_SHRM_LOADADDRESS}>;\n\
            entry = <${UBOOT_FIT_SHRM_ENTRYPOINT}>;\n\
        };\n\
        qclib {\n\
            description = \"QcLib\";\n\
            data = /incbin/(\"QcLib.elf\");\n\
            type = \"firmware\";\n\
            arch = \"${UBOOT_ARCH}\";\n\
            compression = \"none\";\n\
            load = <${UBOOT_FIT_QCLIB_LOADADDRESS}>;\n\
            entry = <${UBOOT_FIT_QCLIB_ENTRYPOINT}>;\n\
        };\n\
        ecc {\n\
            description = \"ECC Config\";\n\
            data = /incbin/(\"Shikra_LCP.bin\");\n\
            type = \"firmware\";\n\
            arch = \"${UBOOT_ARCH}\";\n\
            compression = \"none\";\n\
            load = <${UBOOT_FIT_ECC_LOADADDRESS}>;\n\
            entry = <${UBOOT_FIT_ECC_ENTRYPOINT}>;\n\
        };\n\
"

