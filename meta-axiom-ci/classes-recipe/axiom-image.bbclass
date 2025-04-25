# Copyright (c) 2023-2024 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: MIT

# Class to add the dependencies needed for the current Axiom test framework

CORE_IMAGE_EXTRA_INSTALL:append:qcom-axiom-ci = " \
	android-tools-adbd \
        networkmanager-nmcli \
        kernel-modules \
"

enable_adbd_at_boot () {
	touch ${IMAGE_ROOTFS}/etc/usb-debugging-enabled
}

ROOTFS_POSTPROCESS_COMMAND += "enable_adbd_at_boot"
