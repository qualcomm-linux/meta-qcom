#!/bin/sh
#
# Copyright (c) 2021-2024 Linaro
#
# SPDX-License-Identifier: MIT
#

set -e

modprobe socinfo || true

if [ -r /sys/firmware/devicetree/base/model ] ; then
	MACHINE=`cat /sys/firmware/devicetree/base/model`
	case "$MACHINE" in
		*DB820c*)
			DSP_LIBRARY_PATH=/usr/share/qcom/apq8096/Qualcomm/db820c/dsp
			;;
		*"Dragonboard 845c"*)
			DSP_LIBRARY_PATH=/usr/share/qcom/sdm845/Thundercomm/db845c/dsp
			;;
		*"Robotics RB1"*)
			DSP_LIBRARY_PATH=/usr/share/qcom/qcm2290/Thundercomm/RB1/dsp
			;;
		*"QRB4210 RB2"*)
			DSP_LIBRARY_PATH=/usr/share/qcom/qrb4210/Thundercomm/RB2/dsp
			;;
		*"Robotics RB5"*)
			DSP_LIBRARY_PATH=/usr/share/qcom/sm8250/Thundercomm/RB5/dsp
			;;
	esac
fi

if [ -z "$DSP_LIBRARY_PATH" -o ! -d "$DSP_LIBRARY_PATH" ] ; then
	if [ -n "`ls /usr/share/qcom`" ] ; then
		echo "DSP directory present, but didn't detect support for this platform, bailing out" 1>&2
		exit 1
	fi

	echo "DSP directory not present, trying DSP partitions"

	if [ -h /dev/disk/by-partlabel/dsp_a ] ; then
		mount /dev/disk/by-partlabel/dsp_a /usr/share/qcom -o bind
	else
		mount /dev/disk/by-partlabel/dsp /usr/share/qcom -o bind
	fi
	DSP_LIBRARY_PATH=/usr/share/qcom
fi

export DSP_LIBRARY_PATH

# compatibility
ADSP_LIBRARY_PATH=$DSP_LIBRARY_PATH
export ADSP_LIBRARY_PATH
