# FIT_DTB_COMPATIBLE overrides for machines booting through U-Boot with open
# boot firmware.
#
# For machines that don't use Linux EL2 DTBO by default, on the UEFI boot path
# the firmware appends a suffix such as "-el2kvm" to the compatible it looks up
# when Linux runs at EL2, so the EL2 overlay is listed under its own compatible.
# As U-Boot can look up the plain board compatible, so on the open boot firmware
# path the EL2 overlay has to be part of the entry for the plain compatible itself.
# These entries conflict with the UEFI ones, so they live here, and only machines that
# boot through U-Boot load this file by appending it to LINUX_QCOM_FIT_DTB_COMPATIBLE:
#
#   LINUX_QCOM_FIT_DTB_COMPATIBLE:append = " conf/machine/include/fit-dtb-compatible-open-fw.inc"
#
# Syntax:
# FIT_DTB_COMPATIBLE[<compatible-encoded>] = "<dtb-stem> [<overlay-stem> [<overlay-stem>...]]"
#
# <compatible-encoded> is the DT compatible string with every comma replaced
# by an underscore (BitBake flag names cannot contain commas).

# ---------- nord ----------
FIT_DTB_COMPATIBLE[qcom_qam8x97p-qam] = "nord-ride-embedded nord-embedded-el2"
FIT_DTB_COMPATIBLE[qcom_nord-qam] = "nord-rrd nord-embedded-el2"
FIT_DTB_COMPATIBLE[qcom_qam8x97p-qam-camx] = "nord-ride-embedded nord-embedded-el2 nord-ride-embedded-camx"
FIT_DTB_COMPATIBLE[qcom_nord-qam-camx] = "nord-rrd nord-embedded-el2 nord-rrd-camx"
