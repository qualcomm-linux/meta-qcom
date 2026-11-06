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
UBOOT_FIT_UBOOT_LOADADDRESS ?= "0x9F400000"
UBOOT_FIT_UBOOT_ENTRYPOINT ?= "0x9F400000"

# Recipes providing BL31, OP-TEE and the XBL-sec ELF for the SPL FIT boot flow.
QCOM_UBOOT_SPL_FIT_ATF ?= "trusted-firmware-a-qcom-shikra"
QCOM_UBOOT_SPL_FIT_TEE ?= "optee-os-qcom-shikra"
QCOM_UBOOT_SPL_FIT_XBL_SEC ?= "firmware-qcom-boot-shikra"

# PMIC, DCB, SHRM, QcLib, ECC and RPM blobs are provided by
# firmware-qcom-boot-shikra. All but RPM are loaded by the SPL at the pre-ddr
# stage; RPM goes with BL31/OP-TEE/U-Boot at post-ddr.
QCOM_UBOOT_SPL_FIT_EXTRA_LOADABLES ?= "\
    firmware-qcom-boot-shikra:Pmic_Cocos.bin \
    firmware-qcom-boot-shikra:Pmic_Wailua.bin \
    firmware-qcom-boot-shikra:900B_7_0100_0_dcb.bin \
    firmware-qcom-boot-shikra:900B_7_0100_1_dcb.bin \
    firmware-qcom-boot-shikra:shrm.elf \
    firmware-qcom-boot-shikra:QcLib.elf \
    firmware-qcom-boot-shikra:Shikra_LCP.bin \
    firmware-qcom-boot-shikra:rpm.mbn \
"

# SHRM, QcLib and RPM are split per ELF segment, so their load and entry
# addresses come from the ELF program headers.
QCOM_UBOOT_SPL_FIT_ELFS ?= "\
    shrm:shrm.elf:first_only:riscv \
    qclib:QcLib.elf:first_only:${UBOOT_ARCH}:elf \
    rpm:rpm.mbn:own_load:${UBOOT_ARCH} \
"

UBOOT_FIT_PMIC_COCOS_LOADADDRESS ?= "0x0C294800"
UBOOT_FIT_PMIC_COCOS_ENTRYPOINT ?= "0x0C294800"
UBOOT_FIT_PMIC_WAILUA_LOADADDRESS ?= "0x0C2A0400"
UBOOT_FIT_PMIC_WAILUA_ENTRYPOINT ?= "0x0C2A0400"
UBOOT_FIT_DCB_LP4_LOADADDRESS ?= "0x0C2A6400"
UBOOT_FIT_DCB_LP4_ENTRYPOINT ?= "0x0C2A6400"
UBOOT_FIT_DCB_LP5_LOADADDRESS ?= "0x0C2AAC00"
UBOOT_FIT_DCB_LP5_ENTRYPOINT ?= "0x0C2AAC00"
UBOOT_FIT_ECC_LOADADDRESS ?= "0x0C2AF400"
UBOOT_FIT_ECC_ENTRYPOINT ?= "0x0C2AF400"

UBOOT_FIT_USER_SETTINGS ?= "\
        pmic_cocos {\n\
            description = \"PMIC Cocos Config\";\n\
            data = /incbin/(\"Pmic_Cocos.bin\");\n\
            type = \"firmware\";\n\
            arch = \"${UBOOT_ARCH}\";\n\
            os = \"elf\";\n\
            load = <${UBOOT_FIT_PMIC_COCOS_LOADADDRESS}>;\n\
            entry = <${UBOOT_FIT_PMIC_COCOS_ENTRYPOINT}>;\n\
            compression = \"none\";\n\
${QCOM_UBOOT_SPL_FIT_IMAGE_SIGN}\
        };\n\
        pmic_wailua {\n\
            description = \"PMIC Wailua Config\";\n\
            data = /incbin/(\"Pmic_Wailua.bin\");\n\
            type = \"firmware\";\n\
            arch = \"${UBOOT_ARCH}\";\n\
            load = <${UBOOT_FIT_PMIC_WAILUA_LOADADDRESS}>;\n\
            entry = <${UBOOT_FIT_PMIC_WAILUA_ENTRYPOINT}>;\n\
            compression = \"none\";\n\
${QCOM_UBOOT_SPL_FIT_IMAGE_SIGN}\
        };\n\
        dcb_lp4 {\n\
            description = \"DDR Config Block (LP4)\";\n\
            data = /incbin/(\"900B_7_0100_1_dcb.bin\");\n\
            type = \"firmware\";\n\
            arch = \"${UBOOT_ARCH}\";\n\
            load = <${UBOOT_FIT_DCB_LP4_LOADADDRESS}>;\n\
            entry = <${UBOOT_FIT_DCB_LP4_ENTRYPOINT}>;\n\
            compression = \"none\";\n\
${QCOM_UBOOT_SPL_FIT_IMAGE_SIGN}\
        };\n\
        dcb_lp5 {\n\
            description = \"DDR Config Block (LP5)\";\n\
            data = /incbin/(\"900B_7_0100_0_dcb.bin\");\n\
            type = \"firmware\";\n\
            arch = \"${UBOOT_ARCH}\";\n\
            load = <${UBOOT_FIT_DCB_LP5_LOADADDRESS}>;\n\
            entry = <${UBOOT_FIT_DCB_LP5_ENTRYPOINT}>;\n\
            compression = \"none\";\n\
${QCOM_UBOOT_SPL_FIT_IMAGE_SIGN}\
        };\n\
        ecc {\n\
            description = \"ECC Config\";\n\
            data = /incbin/(\"Shikra_LCP.bin\");\n\
            type = \"firmware\";\n\
            arch = \"${UBOOT_ARCH}\";\n\
            load = <${UBOOT_FIT_ECC_LOADADDRESS}>;\n\
            entry = <${UBOOT_FIT_ECC_ENTRYPOINT}>;\n\
            compression = \"none\";\n\
${QCOM_UBOOT_SPL_FIT_IMAGE_SIGN}\
        };\n\
$(cat fit-elf/shrm.dtsi)\n\
$(cat fit-elf/qclib.dtsi)\n\
$(cat fit-elf/rpm.dtsi)\n\
"

UBOOT_FIT_CONF_USER_LOADABLES ?= ", $(cat fit-elf/rpm.loadables)"

# The SPL picks the configuration by description, "pre-ddr" ahead of DDR
# init and "post-ddr" (UBOOT_FIT_CONF_DESC) after it.
UBOOT_FIT_USER_CONFIGURATIONS ?= "\
        pre-ddr {\n\
            description = \"pre-ddr\";\n\
            loadables = \"pmic_cocos\", \"pmic_wailua\", \"dcb_lp4\", \"dcb_lp5\", \"ecc\",\n\
                        $(cat fit-elf/shrm.loadables), $(cat fit-elf/qclib.loadables);\n\
${QCOM_UBOOT_SPL_FIT_CONF_SIGN}\
        };\n\
"

# Name the post-ddr configuration after its description, like pre-ddr.
UBOOT_FIT_CONF_NAME ?= "post-ddr"
