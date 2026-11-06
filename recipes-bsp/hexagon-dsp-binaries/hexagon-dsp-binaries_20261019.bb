require hexagon-dsp-binaries.inc

LIC_FILES_CHKSUM = "\
    file://LICENSE.qcom;md5=56e86b6c508490dadc343f39468b5f5e \
    file://LICENSE.qcom-2;md5=165287851294f2fb8ac8cbc5e24b02b0 \
    file://WHENCE;md5=b63a7c8353beda423fc9b669d9753cfa  \
    file://conf.d/hexagon-dsp-binaries-qualcomm-iq9075-evk.yaml;endline=2;md5=077232564320a8fce4ea446daad3d726 \
"

SRCREV = "38f254d4da12aeaa4d82b7e855979b6a5d6aee22"

# Backport the Nord HPASS.AT.1.0.1-00032-NORD-1 ADSP dynamic modules for
# SA8797P-Ride-Embedded from upstream commit 29ba5422bfd4. The binaries
# are fetched from that commit as a separate source and copied into ${S}.
NORD_HPASS_DIR = "nord/Qualcomm/SA8797P-Ride-Embedded"
NORD_HPASS_VER = "HPASS.AT.1.0.1-00032-NORD-1"

SRC_URI += "\
    git://github.com/linux-msm/dsp-binaries;protocol=https;branch=trunk;name=nord-hpass;destsuffix=dsp-binaries-nord-hpass \
    file://0001-nord-update-hpass-dynamic-modules-for-SA8797P-Ride-E.patch \
"
SRCREV_nord-hpass = "29ba5422bfd4ee595360738f8f62c3394ad1c146"
SRCREV_FORMAT = "default_nord-hpass"

do_unpack[postfuncs] += "copy_nord_hpass_modules"
copy_nord_hpass_modules () {
	for dsp in adsp adsp1; do
		cp -r ${UNPACKDIR}/dsp-binaries-nord-hpass/${NORD_HPASS_DIR}/$dsp-${NORD_HPASS_VER} \
			${S}/${NORD_HPASS_DIR}/
	done
}
