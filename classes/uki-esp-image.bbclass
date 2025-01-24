# Create an ESP image that has a type #2 EFI UKI and systemd-boot

# Optional subfolder, dependant on where the ESP partition gets mounted
# intended to only have a leading slash, no trailing slash e.g. '/EFI', or just empty, ''
ESPFOLDER ?= "/EFI"

do_ukiesp() {
	mkdir -p ${IMAGE_ROOTFS}${ESPFOLDER}/loader/entries
	mkdir -p ${IMAGE_ROOTFS}${ESPFOLDER}/ESP/Linux

	# Copy over files from deploy into the rootfs
	install -m 0755 ${DEPLOY_DIR_IMAGE}/${UKI_FILENAME} ${IMAGE_ROOTFS}${ESPFOLDER}/ESP/Linux

	# Add global settings to systemd-boot config
	echo "timeout 5" > ${IMAGE_ROOTFS}${ESPFOLDER}/loader/loader.conf

}

addtask ukiesp after do_deploy uki before do_image_complete do_image_wic


