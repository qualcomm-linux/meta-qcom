# Create an ESP image that has a type #2 EFI UKI and systemd-boot
#
# Copyright (c) 2025 Qualcomm Innovation Center, Inc.
#
# SPDX-License-Identifier: MIT
#

# Optional subfolder, dependant on where the ESP partition gets mounted
# intended to only have a leading slash, no trailing slash e.g. '/EFI', or just empty, ''
ESPFOLDER ?= "/EFI"

do_ukiesp() {
	mkdir -p ${IMAGE_ROOTFS}${ESPFOLDER}/EFI/Linux

	# Copy over files from deploy into the rootfs
	install -m 0755 ${DEPLOY_DIR_IMAGE}/${UKI_FILENAME} ${IMAGE_ROOTFS}${ESPFOLDER}/EFI/Linux
}

addtask ukiesp after do_deploy uki before do_image_complete do_image_wic
