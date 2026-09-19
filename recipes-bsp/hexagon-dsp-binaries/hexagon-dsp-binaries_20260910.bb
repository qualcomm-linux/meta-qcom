require hexagon-dsp-binaries.inc

LIC_FILES_CHKSUM = "\
    file://LICENSE.qcom;md5=56e86b6c508490dadc343f39468b5f5e \
    file://LICENSE.qcom-2;md5=165287851294f2fb8ac8cbc5e24b02b0 \
    file://WHENCE;md5=b2fd488228ec207288e54d0714985a6a \
    file://conf.d/hexagon-dsp-binaries-qualcomm-iq9075-evk.yaml;endline=2;md5=077232564320a8fce4ea446daad3d726 \
"

SRCREV = "bbb0e6b59143e052dbf26b371ee239ab4b0881b0"

# FIXME:
# Please set FILES such that these items are packaged. Alternatively if they are unneeded, avoid installing them or delete them within do_install.
# hexagon-dsp-binaries: 30 installed and not shipped files. [installed-vs-shipped]
do_install:append() {
     rm -r ${D}${datadir}/qcom/eliza ${D}${datadir}/qcom/mahua
}
