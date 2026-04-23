FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI:append:qcom-distro = "file://0001-shared-rap-Introduce-Channel-Sounding-HCI-raw-int.patch \
                              file://0002-main.conf-Add-Channel-Sounding-config-parsing-sup.patch \
                              file://0003-profiles-ranging-Add-HCI-LE-Event-Handling-in-Ref.patch \
"
