# Copyright (c) 2023-2024 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: MIT

# Class to add the dependencies needed for the current Axiom test framework

CORE_IMAGE_EXTRA_INSTALL:append:qcom-axiom-ci = " \
	android-tools-adbd \
	bluez5 \
	networkmanager-nmcli iproute2-ip \
	usbutils \
	iperf3 \
	net-tools \
	weston \
	pipewire \
	dbus-ptest \
	busybox-ptest \
	openssh-ptest \
	openssl-ptest \
	valgrind \
	kernel-modules \
"

# Add ssh server
IMAGE_FEATURES:append:qcom-axiom-ci = " ssh-server-openssh"

enable_adbd_at_boot () {
	touch ${IMAGE_ROOTFS}/etc/usb-debugging-enabled
}

ROOTFS_POSTPROCESS_COMMAND += "enable_adbd_at_boot"
