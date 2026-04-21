SUMMARY = "Create ath12k QCC2072 firmware directory"
LICENSE = "CLOSED"

inherit allarch

do_install() {
    install -d ${D}${nonarch_base_libdir}/firmware/ath12k/QCC2072/hw1.0
}

FILES:${PN} += " \
    ${nonarch_base_libdir}/firmware/ath12k \
    ${nonarch_base_libdir}/firmware/ath12k/QCC2072 \
    ${nonarch_base_libdir}/firmware/ath12k/QCC2072/hw1.0 \
"

ALLOW_EMPTY:${PN} = "1"
