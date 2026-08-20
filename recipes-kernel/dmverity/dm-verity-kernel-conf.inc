FILESEXTRAPATHS:prepend := "${THISDIR}/linux:"

# Additional kernel configs.
SRC_URI += " \
        file://dm-verity.cfg \
"
