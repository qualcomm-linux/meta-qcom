FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append:qcom = " \
    file://0001-Add-SELinux-policy-for-nhx.sh.patch \
    file://0002-container-Allow-access-to-etc-cdi-for-CDI-configurat.patch \
    file://0003-wayland-Add-wayland_stream_connect-interface.patch \
    file://0004-docker-Allow-dockerd-to-connect-to-pulseaudio-and-wa.patch \
"
