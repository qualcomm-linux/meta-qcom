# Copyright (c) 2026 Qualcomm Innovation Center, Inc. All rights reserved.
#
# SPDX-License-Identifier: BSD-3-Clause-Clear
#
# SPL FIT boot flow for open-firmware (TF-A + OP-TEE + U-Boot) machines.
#
# U-Boot SPL becomes the first non-Qualcomm stage (replacing the TF-A BL2/FIP
# loader). The Qualcomm XBL loads the signed SPL from the "tz_a" partition; the
# SPL then loads a FIT image (containing BL31, OP-TEE and U-Boot proper) from
# the "uefi_a" partition and jumps to BL31. The FIT is assembled by OE-core's
# meta/classes-recipe/uboot-sign.bbclass, which also signs it when
# SPL_SIGN_ENABLE is set.
#
# The machine selects a ${UBOOT_CONFIG} whose defconfig has CONFIG_SPL=y, which
# builds both the SPL and U-Boot proper. The variables below are consumed by the
# u-boot-qcom recipe and trusted-firmware-a-qcom; the load addresses and the
# other SoC specific inputs come from the SoC configuration.

# Toggle the SPL FIT flow on. The u-boot-qcom and trusted-firmware-a-qcom
# recipes branch on this marker.
QCOM_UBOOT_SPL_FIT = "1"

# Assemble the U-Boot FIT (u-boot-fitImage) via uboot-sign.bbclass.
UBOOT_FITIMAGE_ENABLE = "1"

# The FIT is left unsigned, as uboot-sign.bbclass defaults SPL_SIGN_ENABLE to 0:
# the boot images are already authenticated by the Qualcomm chain, where the XBL
# authenticates the SPL. Setting SPL_SIGN_ENABLE = "1", together with a key
# (SPL_SIGN_KEYDIR and SPL_SIGN_KEYNAME, or UBOOT_FIT_GENERATE_KEYS = "1" for a
# throwaway one), signs every image in the FIT, embeds the public key into the
# SPL device tree and, through spl-fit-signature.cfg, builds an SPL that
# enforces those signatures. The hash and signature algorithms are whatever
# uboot-sign defaults to, sha256 and rsa2048.

# BL31 (TF-A) - bl31.bin is staged into the U-Boot build dir by the recipe.
UBOOT_FIT_ARM_TRUSTED_FIRMWARE = "1"

# OP-TEE (BL32) - tee-raw.bin is staged into the U-Boot build dir by the recipe.
#
# The tee image is emitted through UBOOT_FIT_USER_SETTINGS with its loadable
# appended after "uboot" instead of via UBOOT_FIT_TEE: uboot-sign orders the
# loadables as "tee", "uboot", but the SPL only records loadables into the
# /fit-images FDT node once the U-Boot loadable has set the FDT address, so
# anything listed before "uboot" is never recorded. spl_invoke_atf() discovers
# the BL32 entry point from /fit-images (os = "tee"); with tee listed first
# BL31 would receive no BL32 entry and OP-TEE would not be started.

# Emitted into the tee node only while signing, matching what uboot-sign adds to
# the images it generates itself.
QCOM_UBOOT_FIT_TEE_SIGNATURE = '\
            signature {\n\
                algo = \"${UBOOT_FIT_HASH_ALG},${UBOOT_FIT_SIGN_ALG}\";\n\
                key-name-hint = \"${SPL_SIGN_KEYNAME}\";\n\
            };\n'

UBOOT_FIT_USER_SETTINGS = '\
        tee {\n\
            description = \"Trusted Execution Environment\";\n\
            data = /incbin/(\"tee-raw.bin\");\n\
            type = \"tee\";\n\
            arch = \"arm64\";\n\
            os = \"tee\";\n\
            load = <${UBOOT_FIT_TEE_LOADADDRESS}>;\n\
            entry = <${UBOOT_FIT_TEE_ENTRYPOINT}>;\n\
            compression = \"none\";\n\
${@bb.utils.contains('SPL_SIGN_ENABLE', '1', d.getVar('QCOM_UBOOT_FIT_TEE_SIGNATURE'), '', d)}\
        };\n'
UBOOT_FIT_CONF_USER_LOADABLES = ', \"tee\"'

# Select BL31 as the image SPL boots first (mirrors the reference .its
# "firmware = ..." property).
UBOOT_FIT_CONF_FIRMWARE = "atf"

# The qcom SPL selects the FIT configuration by matching its "description"
# property: board_fit_config_name_match() in arch/arm/mach-snapdragon/spl.c
# accepts "post-ddr" once DDR is up (which is the case when the XBL loads the
# SPL). Without the match the SPL would still fall back to the default (only)
# configuration, but the explicit description keeps the selection positive and
# matches the reference fit_spl.its shipped with U-Boot (board/qualcomm/).
UBOOT_FIT_CONF_DESC = "post-ddr"

# Final SPL binary, wrapped into an ELF by the recipe and qtestsign'd as the
# "tz.mbn" boot image. While signing this is the binary uboot-sign's
# concat_spl_dtb reassembles from the SPL and the key-carrying DTB; otherwise it
# is the one U-Boot built.
SPL_BINARY = "spl/u-boot-spl.bin"
