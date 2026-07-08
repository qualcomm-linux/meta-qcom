
DESCRIPTION = "Qualcomm thermald configuration"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append:qcom = " file://thermal-conf.xml"

do_install:append:qcom() {
    install -d ${D}${sysconfdir}/thermald
    install -m 0644 ${UNPACKDIR}/thermal-conf.xml \
        ${D}${sysconfdir}/thermald/thermal-conf.xml
}

FILES:${PN} += "${sysconfdir}/thermald/thermal-conf.xml"

RDEPENDS:${PN} += "thermald"
