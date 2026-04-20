FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append:qcom = " file://dmaheap.conf"

do_install:append:qcom() {
    install -d ${D}${systemd_system_unitdir}/pipewire.service.d
    install -m 0644 ${UNPACKDIR}/dmaheap.conf \
        ${D}${systemd_system_unitdir}/pipewire.service.d/dmaheap.conf
}
