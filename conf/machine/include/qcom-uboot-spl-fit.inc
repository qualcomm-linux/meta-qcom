# Copyright (c) 2026 Qualcomm Innovation Center, Inc. All rights reserved.
#
# SPDX-License-Identifier: BSD-3-Clause-Clear
#
# SPL FIT boot flow: the Qualcomm XBL loads the signed U-Boot SPL from tz_a,
# and the SPL loads a FIT holding BL31, OP-TEE and U-Boot proper from uefi_a
# and jumps to BL31. A machine opts in by requiring this file and selecting a
# ${UBOOT_CONFIG} whose defconfig has CONFIG_SPL=y. Load addresses and the
# other SoC specific inputs come from the SoC configuration.

# Marker the u-boot-qcom and trusted-firmware-a-qcom recipes branch on.
QCOM_UBOOT_SPL_FIT = "1"

# Assemble the FIT from BL31 and OP-TEE, staged by the u-boot recipe, and
# U-Boot proper. Signing it is off by default (SPL_SIGN_ENABLE), as the boot
# images are already authenticated by the Qualcomm chain; setting it to "1"
# also builds an SPL that enforces the signatures, see spl-fit-signature.cfg.
UBOOT_FITIMAGE_ENABLE = "1"
UBOOT_FIT_ARM_TRUSTED_FIRMWARE = "1"
UBOOT_FIT_TEE = "1"

# BL31 boots first, and board_fit_config_name_match() in the qcom SPL selects
# the configuration by this description.
UBOOT_FIT_CONF_FIRMWARE = "atf"
UBOOT_FIT_CONF_DESC = "post-ddr"

# The XBL takes the SPL path only with this XBLConfig variant, which the boot
# firmware ships next to the stock one.
QCOM_XBL_CONFIG = "xbl_config_spl.elf"

# Wrapped into an ELF and signed as the tz.mbn boot image by the recipe.
SPL_BINARY = "spl/u-boot-spl.bin"
