#
# Copyright OpenEmbedded Contributors
# Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
#
# SPDX-License-Identifier: MIT
#

# The Qualcomm SPL FIT needs uboot-sign options that oe-core doesn't have yet.
# This class adds them on top of uboot-sign, overriding its SPL ITS functions
# with the ones from the oe-core series below. When bumping oe-core, diff these
# functions against uboot-sign, and drop the class once the series is merged:
# https://lists.openembedded.org/g/openembedded-core/message/250842

inherit uboot-sign

# Extra mkimage options, used both when creating and when signing the FIT
UBOOT_FIT_MKIMAGE_EXTRA_OPTS ?= ""

# Extra image nodes, described by varflags named after the node:
# UBOOT_FIT_LOADABLE_FILENAME and _LOADADDRESS (required), _ENTRYPOINT, _TYPE,
# _ARCH, _OS, _COMPRESSION and _DESCRIPTION
UBOOT_FIT_LOADABLES ?= ""

# UBOOT_FIT_LOADABLES images the default configuration also loads
UBOOT_FIT_CONF_LOADABLES ?= ""

# Extra configuration nodes, described by varflags named after the node:
# UBOOT_FIT_CONFIGURATION_LOADABLES (required), _FIRMWARE and _DESCRIPTION
UBOOT_FIT_CONFIGURATIONS ?= ""

# Hash and signature nodes of an image node
uboot_fitimage_image_signing() {
	if [ "${SPL_SIGN_ENABLE}" = "1" ] && [ "${SPL_SIGN_CONF}" = "1" ] ; then
		cat << EOF >> ${UBOOT_ITS}
            hash-1 {
                algo = "${UBOOT_FIT_HASH_ALG}";
            };
EOF
	fi

	if [ "${SPL_SIGN_ENABLE}" = "1" ] && [ "${SPL_SIGN_INDIVIDUAL}" = "1" ] ; then
		cat << EOF >> ${UBOOT_ITS}
            signature {
                algo = "${UBOOT_FIT_HASH_ALG},${UBOOT_FIT_SIGN_ALG}";
                key-name-hint = "${SPL_SIGN_KEYNAME}";
            };
EOF
	fi
}

# Create a ITS file for the atf
uboot_fitimage_atf() {
	cat << EOF >> ${UBOOT_ITS}
        atf {
            description = "ARM Trusted Firmware";
            data = /incbin/("${UBOOT_FIT_ARM_TRUSTED_FIRMWARE_IMAGE}");
            type = "firmware";
            arch = "${UBOOT_ARCH}";
            os = "arm-trusted-firmware";
            load = <${UBOOT_FIT_ARM_TRUSTED_FIRMWARE_LOADADDRESS}>;
            entry = <${UBOOT_FIT_ARM_TRUSTED_FIRMWARE_ENTRYPOINT}>;
            compression = "none";
EOF
	uboot_fitimage_image_signing

	cat << EOF >> ${UBOOT_ITS}
        };
EOF
}

# Create a ITS file for the tee
uboot_fitimage_tee() {
	cat << EOF >> ${UBOOT_ITS}
        tee {
            description = "Trusted Execution Environment";
            data = /incbin/("${UBOOT_FIT_TEE_IMAGE}");
            type = "tee";
            arch = "${UBOOT_ARCH}";
            os = "tee";
            load = <${UBOOT_FIT_TEE_LOADADDRESS}>;
            entry = <${UBOOT_FIT_TEE_ENTRYPOINT}>;
            compression = "none";
EOF
	uboot_fitimage_image_signing

	cat << EOF >> ${UBOOT_ITS}
        };
EOF
}

# Create a ITS image node for a UBOOT_FIT_LOADABLES entry
uboot_fitimage_loadable() {
	if [ -z "$2" ] || [ -z "$3" ] ; then
		bbfatal "UBOOT_FIT_LOADABLES: $1 needs UBOOT_FIT_LOADABLE_FILENAME[$1] and UBOOT_FIT_LOADABLE_LOADADDRESS[$1]"
	fi

	cat << EOF >> ${UBOOT_ITS}
        $1 {
            description = "$9";
            data = /incbin/("$2");
            type = "$5";
            arch = "$6";
EOF
	if [ -n "$7" ] ; then
		echo "            os = \"$7\";" >> ${UBOOT_ITS}
	fi
	echo "            load = <$3>;" >> ${UBOOT_ITS}
	if [ -n "$4" ] ; then
		echo "            entry = <$4>;" >> ${UBOOT_ITS}
	fi
	echo "            compression = \"$8\";" >> ${UBOOT_ITS}

	uboot_fitimage_image_signing

	cat << EOF >> ${UBOOT_ITS}
        };
EOF
}

# The varflags are read as the task runs, so a prefunc can still set them
def uboot_fitimage_loadables(d):
    import shlex
    cmds = []
    for name in (d.getVar('UBOOT_FIT_LOADABLES') or '').split():
        def flag(var, default=''):
            return d.getVarFlag(var, name) or default
        args = [name,
                flag('UBOOT_FIT_LOADABLE_FILENAME'),
                flag('UBOOT_FIT_LOADABLE_LOADADDRESS'),
                flag('UBOOT_FIT_LOADABLE_ENTRYPOINT'),
                flag('UBOOT_FIT_LOADABLE_TYPE', 'firmware'),
                flag('UBOOT_FIT_LOADABLE_ARCH', d.getVar('UBOOT_ARCH')),
                flag('UBOOT_FIT_LOADABLE_OS'),
                flag('UBOOT_FIT_LOADABLE_COMPRESSION', 'none'),
                flag('UBOOT_FIT_LOADABLE_DESCRIPTION', '%s loadable' % name)]
        cmds.append('uboot_fitimage_loadable ' + ' '.join(shlex.quote(a) for a in args))
    return '\n'.join(cmds)

# Create a ITS configuration node
uboot_fitimage_configuration() {
	conf_firmware=""
	conf_fdt=""
	conf_sign_images="\"loadables\""

	if [ -n "$3" ] ; then
		conf_firmware="firmware = \"$3\";"
		conf_sign_images="\"firmware\", ${conf_sign_images}"
	fi

	if [ -n "$5" ] ; then
		conf_fdt="fdt = \"$5\";"
		conf_sign_images="${conf_sign_images}, \"fdt\""
	fi

	cat << EOF >> ${UBOOT_ITS}
        $1 {
            description = "$2";
            ${conf_firmware}
            loadables = $4;
            ${conf_fdt}
EOF

	if [ "${SPL_SIGN_ENABLE}" = "1" ] && [ "${SPL_SIGN_CONF}" = "1" ] ; then
		cat << EOF >> ${UBOOT_ITS}
            signature {
                algo = "${UBOOT_FIT_HASH_ALG},${UBOOT_FIT_SIGN_ALG}";
                key-name-hint = "${SPL_SIGN_KEYNAME}";
                sign-images = ${conf_sign_images};
            };
EOF
	fi

	cat << EOF >> ${UBOOT_ITS}
        };
EOF
}

# Create a ITS configuration node for a UBOOT_FIT_CONFIGURATIONS entry
uboot_fitimage_user_configuration() {
	images="uboot fdt ${UBOOT_FIT_LOADABLES}"
	if [ "${UBOOT_FIT_TEE}" = "1" ] ; then
		images="$images tee"
	fi
	if [ "${UBOOT_FIT_ARM_TRUSTED_FIRMWARE}" = "1" ] ; then
		images="$images atf"
	fi

	if [ -z "$4" ] ; then
		bbfatal "UBOOT_FIT_CONFIGURATIONS: $1 needs UBOOT_FIT_CONFIGURATION_LOADABLES[$1]"
	fi

	loadables=""
	for image in $3 $4 ; do
		case " $images " in
		*" $image "*)
			;;
		*)
			bbfatal "UBOOT_FIT_CONFIGURATIONS: $1 has no $image image to load"
			;;
		esac
	done
	for image in $4 ; do
		loadables="${loadables}${loadables:+, }\"$image\""
	done

	uboot_fitimage_configuration "$1" "$2" "$3" "$loadables" ""
}

def uboot_fitimage_user_configurations(d):
    import shlex
    cmds = []
    for name in (d.getVar('UBOOT_FIT_CONFIGURATIONS') or '').split():
        args = [name,
                d.getVarFlag('UBOOT_FIT_CONFIGURATION_DESCRIPTION', name) or name,
                d.getVarFlag('UBOOT_FIT_CONFIGURATION_FIRMWARE', name) or '',
                ' '.join((d.getVarFlag('UBOOT_FIT_CONFIGURATION_LOADABLES', name) or '').split())]
        cmds.append('uboot_fitimage_user_configuration ' + ' '.join(shlex.quote(a) for a in args))
    return '\n'.join(cmds)

# Functions and varflags used only through the expanded lists
uboot_fitimage_assemble[vardeps] += "uboot_fitimage_loadable \
    uboot_fitimage_user_configuration \
    UBOOT_FIT_LOADABLE_FILENAME UBOOT_FIT_LOADABLE_LOADADDRESS \
    UBOOT_FIT_LOADABLE_ENTRYPOINT UBOOT_FIT_LOADABLE_TYPE \
    UBOOT_FIT_LOADABLE_ARCH UBOOT_FIT_LOADABLE_OS \
    UBOOT_FIT_LOADABLE_COMPRESSION UBOOT_FIT_LOADABLE_DESCRIPTION \
    UBOOT_FIT_CONFIGURATION_LOADABLES UBOOT_FIT_CONFIGURATION_FIRMWARE \
    UBOOT_FIT_CONFIGURATION_DESCRIPTION"

# Create a ITS file for the U-boot FIT, for use when
# we want to sign it so that the SPL can verify it
uboot_fitimage_assemble() {
	conf_loadables="\"uboot\""
	rm -f ${UBOOT_ITS} ${UBOOT_FITIMAGE_BINARY}

	# First we create the ITS script
	cat << EOF >> ${UBOOT_ITS}
/dts-v1/;

/ {
    description = "${UBOOT_FIT_DESC}";
    #address-cells = <${UBOOT_FIT_ADDRESS_CELLS}>;

    images {
        uboot {
            description = "U-Boot image";
            data = /incbin/("${UBOOT_NODTB_BINARY}");
            type = "standalone";
            os = "u-boot";
            arch = "${UBOOT_ARCH}";
            compression = "none";
            load = <${UBOOT_FIT_UBOOT_LOADADDRESS}>;
            entry = <${UBOOT_FIT_UBOOT_ENTRYPOINT}>;
EOF

	uboot_fitimage_image_signing

	cat << EOF >> ${UBOOT_ITS}
        };
        fdt {
            description = "U-Boot FDT";
            data = /incbin/("${UBOOT_DTB_BINARY}");
            type = "flat_dt";
            arch = "${UBOOT_ARCH}";
            compression = "none";
EOF

	uboot_fitimage_image_signing

	cat << EOF >> ${UBOOT_ITS}
        };
EOF
	if [ "${UBOOT_FIT_TEE}" = "1" ] ; then
		# After U-Boot, the image the SPL appends the DTB to before TF-A
		conf_loadables="${conf_loadables}, \"tee\""
		uboot_fitimage_tee
	fi

	if [ "${UBOOT_FIT_ARM_TRUSTED_FIRMWARE}" = "1" ] ; then
		conf_loadables="\"atf\", ${conf_loadables}"
		uboot_fitimage_atf
	fi

	if [ -n "${UBOOT_FIT_USER_SETTINGS}" ] ; then
		printf "%b" "${UBOOT_FIT_USER_SETTINGS}" >> ${UBOOT_ITS}
	fi

	${@uboot_fitimage_loadables(d)}

	if [ -n "${UBOOT_FIT_CONF_USER_LOADABLES}" ] ; then
		conf_loadables="${conf_loadables}${UBOOT_FIT_CONF_USER_LOADABLES}"
	fi

	for image in ${UBOOT_FIT_CONF_LOADABLES} ; do
		case " ${UBOOT_FIT_LOADABLES} " in
		*" $image "*)
			conf_loadables="${conf_loadables}, \"$image\""
			;;
		*)
			bbfatal "UBOOT_FIT_CONF_LOADABLES: $image is not in UBOOT_FIT_LOADABLES"
			;;
		esac
	done

	cat << EOF >> ${UBOOT_ITS}
    };

    configurations {
        default = "conf";
EOF

	uboot_fitimage_configuration "conf" "${UBOOT_FIT_CONF_DESC}" \
		"${UBOOT_FIT_CONF_FIRMWARE}" "${conf_loadables}" "fdt"

	${@uboot_fitimage_user_configurations(d)}

	cat << EOF >> ${UBOOT_ITS}
    };
};
EOF

	#
	# Assemble the U-boot FIT image
	#
	${UBOOT_MKIMAGE} \
		${@'-D "${SPL_MKIMAGE_DTCOPTS}"' if len('${SPL_MKIMAGE_DTCOPTS}') else ''} \
		${UBOOT_FIT_MKIMAGE_EXTRA_OPTS} \
		-f ${UBOOT_ITS} \
		${UBOOT_FITIMAGE_BINARY}

	if [ "${SPL_SIGN_ENABLE}" = "1" ] ; then
		if [ "${SPL_SIGN_CONF}" != "1" ] && [ "${SPL_SIGN_INDIVIDUAL}" != "1" ] ; then
			bbfatal "SPL_SIGN_ENABLE=1 requires SPL_SIGN_CONF=1 or SPL_SIGN_INDIVIDUAL=1"
		fi

		if [ "${SPL_SIGN_CONF}" != "1" ] ; then
			bbwarn "SPL_SIGN_CONF is disabled. FIT configuration signing is recommended for SPL verified boot."
		fi

		if [ "${SPL_SIGN_INDIVIDUAL}" = "1" ] ; then
			bbwarn "SPL_SIGN_INDIVIDUAL=1 is enabled for compatibility only. It is INSECURE. Individual image signatures do not replace configuration signing."
		fi

		if [ -n "${SPL_DTB_BINARY}" ] ; then
			#
			# Sign the U-boot FIT image and add public key to SPL dtb.
			# Work on a copy of the DTB so that the compile output is
			# never modified in-place.  Without this, sequential test
			# runs that reuse the same work directory accumulate public
			# key nodes from previous runs, causing mkimage to require
			# all of them when verifying the conf signature.
			#
			cp ${SPL_DIR}/${SPL_DTB_BINARY} ${SPL_DIR}/${SPL_DTB_SIGNED}
			${UBOOT_MKIMAGE_SIGN} \
				${@'-D "${SPL_MKIMAGE_DTCOPTS}"' if len('${SPL_MKIMAGE_DTCOPTS}') else ''} \
				${UBOOT_FIT_MKIMAGE_EXTRA_OPTS} \
				-F -k "${SPL_SIGN_KEYDIR}" \
				-K "${SPL_DIR}/${SPL_DTB_SIGNED}" \
				-r ${UBOOT_FITIMAGE_BINARY} \
				${SPL_MKIMAGE_SIGN_ARGS}

			# Verify the U-boot FIT image and SPL dtb
			${UBOOT_FIT_CHECK_SIGN} \
				-k "${SPL_DIR}/${SPL_DTB_SIGNED}" \
				-f ${UBOOT_FITIMAGE_BINARY}

			# Check the extra configurations too
			for conf in ${UBOOT_FIT_CONFIGURATIONS} ; do
				${UBOOT_FIT_CHECK_SIGN} \
					-k "${SPL_DIR}/${SPL_DTB_SIGNED}" \
					-f ${UBOOT_FITIMAGE_BINARY} \
					-c "$conf"
			done
		else
			# Sign the U-boot FIT image
			${UBOOT_MKIMAGE_SIGN} \
				${@'-D "${SPL_MKIMAGE_DTCOPTS}"' if len('${SPL_MKIMAGE_DTCOPTS}') else ''} \
				${UBOOT_FIT_MKIMAGE_EXTRA_OPTS} \
				-F -k "${SPL_SIGN_KEYDIR}" \
				-r ${UBOOT_FITIMAGE_BINARY} \
				${SPL_MKIMAGE_SIGN_ARGS}
		fi
	fi
}
