inherit systemd

FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

RRECOMMENDS:${PN} += " \
    glibc-gconv-utf-16 \
    glibc-gconv-utf-32 \
"

SRC_URI:append = " file://qca_set_bdaddr.service \
                   file://qca_set_bdaddr.sh \
"

#Include obex to support obex related profiles like OPP, FTP, MAP, PBAP
RDEPENDS:${PN} += "${PN}-obex"

#Include only desired tools that are conditional on READLINE in bluez
INST_TOOLS_READLINE = " \
    tools/bluetooth-player \
    tools/obexctl \
    tools/btmgmt \
"

#Remove desired tools from noinst-tools
NOINST_TOOLS:remove = " \
    ${@bb.utils.contains('PACKAGECONFIG', 'readline', '${INST_TOOLS_READLINE}', '', d)} \
"

do_install:append:qcom() {

    #Install desired tools that upstream leaves in build area
    for f in ${INST_TOOLS_READLINE} ; do
        install -m 755 ${B}/$f ${D}/${bindir}
    done

    #Create below directory which is used by obex service
    install -d ${D}${localstatedir}/bluetooth

    # Install script to set unique BDA
    install -d ${D}${sysconfdir}/initscripts
    install -m 755 ${UNPACKDIR}/qca_set_bdaddr.sh ${D}${sysconfdir}/initscripts/

    # Install service that will run qca_set_bdaddr.sh script on boot
    install -m 0644 ${UNPACKDIR}/qca_set_bdaddr.service -D ${D}${systemd_system_unitdir}/qca_set_bdaddr.service
    install -d ${D}${systemd_system_unitdir}/bluetooth.target.wants/
    ln -sf ${systemd_system_unitdir}/qca_set_bdaddr.service ${D}${systemd_system_unitdir}/bluetooth.target.wants/qca_set_bdaddr.service
}

SYSTEMD_SERVICE:${PN} += "qca_set_bdaddr.service"
FILES:${PN}:append = "${systemd_system_unitdir}/qca_set_bdaddr.service \
                           ${sysconfdir}/initscripts/qca_set_bdaddr.sh \
"
