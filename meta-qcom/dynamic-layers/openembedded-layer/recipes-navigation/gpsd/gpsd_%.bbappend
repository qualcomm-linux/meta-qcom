# We can not use PV here, it will be expanded too early (to the '%' value).
# Thus use a temporal variable which substituted later. Note the difference
# between immediate expansion (to get THISDIR) and regular expansion.
#
# The order of these assignments is also important

FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}-${gpsPV}:${THISDIR}/${BPN}:"
gpsPV = "${PV}"

SRC_URI += " \
    file://0001-Introduce-Qualcomm-PDS-service-support.patch \
"
