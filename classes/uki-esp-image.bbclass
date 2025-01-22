# Create an ESP image that has an UKI, systemd-boot and the matching configs

# Optional subfolder, dependant on where the ESP partition gets mounted
# intended to only have a leading slash, no trailing slash e.g. '/EFI', or just empty, ''
ESPFOLDER ?= "/EFI"

# Optional initrd, filname inside ${DEPLOY_DIR_IMAGE}
ESPINITRD ?= ""

# Title as shown on the console
UKI_TITLE ?= "${DISTRO_NAME} ${DISTRO_VERSION} - ${MACHINE}"

# Kernel CMDLINE
UKI_CMDLINE = " "
UKI_CMDLINE[export] = "1"

do_ukiesp() {
	mkdir -p ${IMAGE_ROOTFS}${ESPFOLDER}/loader/entries

	# Copy over files from deploy into the rootfs
	install -m 0755 ${DEPLOY_DIR_IMAGE}/${UKI_FILENAME} ${IMAGE_ROOTFS}${ESPFOLDER}
	install -m 0644 ${DEPLOY_DIR_IMAGE}/${KERNEL_DEVICETREE} ${IMAGE_ROOTFS}${ESPFOLDER}

	# Add global settings to systemd-boot config
	echo "timeout 5" > ${IMAGE_ROOTFS}${ESPFOLDER}/loader/loader.conf

	# copy over kernel and optional initrd and dtb
	echo "title ${UKI_TITLE}" > ${IMAGE_ROOTFS}${ESPFOLDER}/loader/entries/boot.conf
	echo "linux /${UKI_FILENAME}" >> ${IMAGE_ROOTFS}${ESPFOLDER}/loader/entries/boot.conf

	# Assemble CMDLINE
	# FIXME: don't use QCOM_* variables
	if [ -n "${QCOM_BOOTIMG_ROOTFS}" ]; then
	    UKI_CMDLINE="$UKI_CMDLINE root=${QCOM_BOOTIMG_ROOTFS} rw rootwait"
	fi
	if [ -n "${SERIAL_CONSOLES}" ]; then
	    tmp="${SERIAL_CONSOLES}"
	    for entry in $tmp ; do
		baudrate=`echo $entry | sed 's/\;.*//'`
		tty=`echo $entry | sed -e 's/^[0-9]*\;//' -e 's/\;.*//'`
		console="$tty","$baudrate"n8
		UKI_CMDLINE="$UKI_CMDLINE console=$console"
	    done
	fi
	if [ -n "${KERNEL_CMDLINE_EXTRA}" ]; then
	    UKI_CMDLINE="$UKI_CMDLINE ${KERNEL_CMDLINE_EXTRA}"
	fi
	echo "options $UKI_CMDLINE" >> ${IMAGE_ROOTFS}${ESPFOLDER}/loader/entries/boot.conf

	if [ -n "${ESPINITRD}" ] ; then
		install -m 0644 ${DEPLOY_DIR_IMAGE}/${ESPINITRD} ${IMAGE_ROOTFS}${ESPFOLDER}
		echo "initrd /${ESPINITRD}" >> ${IMAGE_ROOTFS}${ESPFOLDER}/loader/entries/boot.conf
	fi
	echo "devicetree /${KERNEL_DEVICETREE}" >> ${IMAGE_ROOTFS}${ESPFOLDER}/loader/entries/boot.conf
}

addtask ukiesp after do_deploy uki before do_image_complete do_image_wic


