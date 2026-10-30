FILESEXTRAPATHS:prepend:qcom := "${THISDIR}/optee-client:"

SRC_URI:append:qcom = " file://persist.conf"

do_install:append:qcom() {
    install -Dm 0644 ${UNPACKDIR}/persist.conf \
            ${D}${systemd_system_unitdir}/tee-supplicant@.service.d/persist.conf
}

FILES:${PN}:append:qcom = " ${systemd_system_unitdir}/tee-supplicant@.service.d/persist.conf"
