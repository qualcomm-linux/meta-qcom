# Mapping from a Device Tree compatible string to the DTB (and optional
# overlays) that implement it.
#
# Syntax:
# FIT_DTB_COMPATIBLE[<compatible-encoded>] = "<dtb-stem> [<overlay-stem> [<overlay-stem>...]]"
#
# <compatible-encoded> is the DT compatible string with every comma replaced
# by an underscore (BitBake flag names cannot contain commas).  For example,
# "qcom,apq8016-sbc" is written as "qcom_apq8016-sbc".
#
# Rules
# -----
# - linux-qcom tracks a newer kernel than linux-yocto, so it can carry extra
#   device trees -- both .dtb and .dtbo -- that have not landed in linux-yocto
#   yet.  Entries that rely on those linux-qcom-only device trees are kept in
#   fit-dtb-compatible-linux-qcom.inc.  Splitting them out lets linux-qcom add
#   device trees quickly while a build using only linux-yocto stays functional
#   from this file alone; as linux-yocto is updated and gains the same device
#   trees, the corresponding entries migrate back into this file.
# - do_generate_qcom_fitimage silently skips any combo whose files are absent
#   from KERNEL_DEVICETREE, so entries here are safe to leave even when a
#   particular machine or kernel does not include every listed file.

# ---------- Direct mapping, no overlay ----------
FIT_DTB_COMPATIBLE[qcom_apq8016-sbc] = "apq8016-sbc"
FIT_DTB_COMPATIBLE[qcom_apq8064-ifc6410] = "qcom-apq8064-ifc6410"
FIT_DTB_COMPATIBLE[qcom_apq8064] = "qcom-apq8064-asus-nexus7-flo"
FIT_DTB_COMPATIBLE[qcom_apq8074-dragonboard] = "qcom-apq8074-dragonboard"
FIT_DTB_COMPATIBLE[qcom_apq8084-sbc] = "qcom-apq8084-ifc6540"
FIT_DTB_COMPATIBLE[qcom_apq8096-sbc] = "apq8096-db820c"
FIT_DTB_COMPATIBLE[qcom_glymur-crd] = "glymur-crd"
FIT_DTB_COMPATIBLE[qcom_kaanapali-mtp] = "kaanapali-mtp"
FIT_DTB_COMPATIBLE[qcom_msm8974] = "qcom-msm8974-lge-nexus5-hammerhead"
FIT_DTB_COMPATIBLE[qcom_purwa-evk] = "purwa-iot-evk"
FIT_DTB_COMPATIBLE[qcom_qcs404-evb-4000] = "qcs404-evb-4000"
FIT_DTB_COMPATIBLE[qcom_qrb2210-rb1] = "qrb2210-rb1"
FIT_DTB_COMPATIBLE[qcom_qrb4210-rb2] = "qrb4210-rb2"
FIT_DTB_COMPATIBLE[qcom_qrb5165-rb5] = "qrb5165-rb5"
FIT_DTB_COMPATIBLE[qcom_sdm845] = "sdm845-db845c"
FIT_DTB_COMPATIBLE[qcom_sm8450-hdk] = "sm8450-hdk"
FIT_DTB_COMPATIBLE[qcom_sm8750-mtp] = "sm8750-mtp"

# ---------- hamoa ----------
FIT_DTB_COMPATIBLE[qcom_hamoa-evk] = \
    "hamoa-iot-evk hamoa-iot-evk-camera-imx577"

# ---------- purwa ----------
FIT_DTB_COMPATIBLE[qcom_purwa-evk-el2kvm] = "purwa-iot-evk x1-el2"

# ---------- lemans ----------
FIT_DTB_COMPATIBLE[qcom_qcs9075-iot] = \
    "lemans-evk lemans-evk-camera-csi1-imx577"

FIT_DTB_COMPATIBLE[qcom_qcs9100-qam] = "qcs9100-ride"
FIT_DTB_COMPATIBLE[qcom_qcs9100-qam-r2.0] = "qcs9100-ride-r3"

FIT_DTB_COMPATIBLE[qcom_sa8775p-qam] = "sa8775p-ride"
FIT_DTB_COMPATIBLE[qcom_sa8775p-qam-r2.0] = "sa8775p-ride-r3"

# ---------- monaco ----------
FIT_DTB_COMPATIBLE[qcom_qcs8275-iot] = \
    "monaco-evk monaco-evk-camera-imx577"

FIT_DTB_COMPATIBLE[qcom_qcs8300-adp] = "qcs8300-ride"

# ---------- shikra ----------
FIT_DTB_COMPATIBLE[qcom_shikracqm-itp] = "shikra-cqm-evk shikra-cqm-evk-imx577-camera"
FIT_DTB_COMPATIBLE[qcom_shikracqs-itp] = "shikra-cqs-evk shikra-cqm-evk-imx577-camera"
FIT_DTB_COMPATIBLE[qcom_shikraiqs-itp] = "shikra-iqs-evk shikra-iqs-evk-imx577-camera"

# ---------- talos ----------
FIT_DTB_COMPATIBLE[qcom_qcs615-adp] = "qcs615-ride"
FIT_DTB_COMPATIBLE[qcom_qcs615-iot] = "talos-evk talos-evk-camera-imx577"
FIT_DTB_COMPATIBLE[qcom_talos-evk-lvds-auo_g133han01] = \
    "talos-evk talos-evk-lvds-auo_g133han01"

# ---------- kodiak ----------
FIT_DTB_COMPATIBLE[qcom_qcm6490-idp] = "qcm6490-idp"

FIT_DTB_COMPATIBLE[qcom_qcs5430-iot] = "qcs6490-rb3gen2"
FIT_DTB_COMPATIBLE[qcom_qcs6490-iot] = "qcs6490-rb3gen2"

FIT_DTB_COMPATIBLE[qcom_qcs5430-iot-subtype9] = \
    "qcs6490-rb3gen2 qcs6490-rb3gen2-industrial-mezzanine"
FIT_DTB_COMPATIBLE[qcom_qcs6490-iot-subtype9] = \
    "qcs6490-rb3gen2 qcs6490-rb3gen2-industrial-mezzanine"
FIT_DTB_COMPATIBLE[qcom_qcs6490-iot-subtype13] = \
    "qcs6490-rb3gen2 qcs6490-rb3gen2-industrial-mezzanine qcs6490-rb3gen2-industrial-mezzanine-m2-cologne"

FIT_DTB_COMPATIBLE[qcom_qcs5430-iot-subtype2] = \
    "qcs6490-rb3gen2 qcs6490-rb3gen2-vision-mezzanine"
FIT_DTB_COMPATIBLE[qcom_qcs6490-iot-subtype2] = \
    "qcs6490-rb3gen2 qcs6490-rb3gen2-vision-mezzanine"
