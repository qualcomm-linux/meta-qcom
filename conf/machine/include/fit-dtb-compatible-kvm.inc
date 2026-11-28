# FIT_DTB_COMPATIBLE overrides for machines with the kvm machine feature, where
# Linux runs at EL2.
#
# For a board that boots Linux at EL1 by default, the UEFI firmware looks up a
# compatible with a suffix such as "-el2kvm" when Linux runs at EL2, so the EL2
# overlay is listed under its own compatible. U-Boot looks up the plain board
# compatible, so the EL2 overlay has to be part of the entry for the plain
# compatible itself. These entries conflict with the EL1 ones, so they live
# here, and a machine loads this file after the default one when kvm is set:
#
#   LINUX_QCOM_FIT_DTB_COMPATIBLE:append = "${@bb.utils.contains('MACHINE_FEATURES', 'kvm', ' conf/machine/include/fit-dtb-compatible-kvm.inc', '', d)}"
#
# Syntax:
# FIT_DTB_COMPATIBLE[<compatible-encoded>] = "<dtb-stem> [<overlay-stem> [<overlay-stem>...]]"
#
# <compatible-encoded> is the DT compatible string with every comma replaced
# by an underscore (BitBake flag names cannot contain commas).

# ---------- nord ----------
FIT_DTB_COMPATIBLE[qcom_qam8x97p-qam] = "nord-ride-embedded nord-embedded-el2"
FIT_DTB_COMPATIBLE[qcom_nord-qam] = "nord-rrd nord-embedded-el2"
FIT_DTB_COMPATIBLE[qcom_qam8x97p-qam-camx] = "nord-ride-embedded nord-embedded-el2 nord-ride-embedded-camx nord-camx-el2"
FIT_DTB_COMPATIBLE[qcom_nord-qam-camx] = "nord-rrd nord-embedded-el2 nord-rrd-camx nord-camx-el2"
FIT_DTB_COMPATIBLE[qcom_iq10800md-qam] = "nord-rrd nord-embedded-el2"
FIT_DTB_COMPATIBLE[qcom_iq10800md-qam-camx] = "nord-rrd nord-embedded-el2 nord-rrd-camx nord-camx-el2"
