# Copyright (c) 2026 Qualcomm Innovation Center, Inc. All rights reserved.
#
# SPDX-License-Identifier: BSD-3-Clause-Clear
#

UBOOT_CONFIG ?= ""
UBOOT_CONFIG_DEFAULT ?= "${UBOOT_CONFIG}"

UBOOT_INITIAL_ENV = ""

# Don't forget to add config to qcom-armv8a.conf when adding it here
UBOOT_CONFIG[iq-615-evk] = "qcom_qcs615_defconfig"
UBOOT_CONFIG[iq-9075-evk] = "qcom_lemans_defconfig"
UBOOT_CONFIG[qcs9100-ride-sx] = "qcom_lemans_defconfig"
UBOOT_CONFIG[qcs615-ride] = "qcom_qcs615_defconfig"
UBOOT_CONFIG[qcs6490-rb3gen2] = "qcm6490_defconfig"
UBOOT_CONFIG[qrb2210-rb1] = "qcom_defconfig"
UBOOT_CONFIG[sdm845-db845c] = "qcom_defconfig"
UBOOT_CONFIG[shikra-evk] = "qcom_shikra_defconfig"

BOARD_MBN_HEADER[iq-615-evk] = "v6"
BOARD_MBN_HEADER[iq-9075-evk] = "v6"
BOARD_MBN_HEADER[qcs9100-ride-sx] = "v6"
BOARD_MBN_HEADER[qcs615-ride] = "v6"
BOARD_MBN_HEADER[qcs6490-rb3gen2] = "v6"
BOARD_MBN_HEADER[qrb2210-rb1] = "v5"
BOARD_MBN_HEADER[sdm845-db845c] = "v6"
BOARD_MBN_HEADER[shikra-evk] = "v6"

UBOOT_ENTRYPOINT:qcm6490 ?= "0xa0000000"
UBOOT_ENTRYPOINT:qcs9100 ?= "0xb0800000"
