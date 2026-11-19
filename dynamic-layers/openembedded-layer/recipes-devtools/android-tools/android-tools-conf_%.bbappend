FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI:append:qcom = " \
    file://android-gadget-setup.machine \
    file://android-gadget-watch \
"

do_install:append:qcom() {
    install -m 0755 ${UNPACKDIR}/android-gadget-watch ${D}${bindir}
}
