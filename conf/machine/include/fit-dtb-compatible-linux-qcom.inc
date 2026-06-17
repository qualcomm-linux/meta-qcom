# FIT_DTB_COMPATIBLE overrides for linux-qcom kernel variants.
#
# Extends fit-dtb-compatible.inc with entries whose combos include overlays
# from LINUX_QCOM_KERNEL_DEVICETREE (overlays not available in linux-yocto).
# Loaded automatically for linux-qcom* kernels via LINUX_QCOM_FIT_DTB_COMPATIBLE
# in dtb-fit-image.bbclass; the base file is not loaded separately.
#
# A key that also exists in the base file will be overridden here because
# BitBake applies last-write-wins for flag variables, and this file is
# included after fit-dtb-compatible.inc.
#
# Syntax:
# FIT_DTB_COMPATIBLE[<compatible-encoded>] = "<dtb-stem> [<overlay-stem> [<overlay-stem>...]]"
#
# <compatible-encoded> is the DT compatible string with every comma replaced
# by an underscore (BitBake flag names cannot contain commas).

require conf/machine/include/fit-dtb-compatible.inc

# ---------- hamoa  ----------
FIT_DTB_COMPATIBLE[qcom_hamoa-evk-camx] = "hamoa-iot-evk hamoa-evk-camx"
FIT_DTB_COMPATIBLE[qcom_hamoa-evk-camx-staging] = "hamoa-iot-evk hamoa-evk-camx hamoa-staging"

# ---------- purwa -----------
FIT_DTB_COMPATIBLE[qcom_purwa-evk-camx] = "purwa-iot-evk purwa-evk-camx"
FIT_DTB_COMPATIBLE[qcom_purwa-evk-camx-el2kvm] = "purwa-iot-evk purwa-evk-camx x1-el2 purwa-camx-el2"

# ---------- lemans ----------

# Add entries for combinations whose overlays are available only in
# LINUX_QCOM_KERNEL_DEVICETREE. Move these to fit-dtb-compatible.inc once
# the overlays are available in linux-yocto.

FIT_DTB_COMPATIBLE[qcom_qcs9075-iot] = \
    "lemans-evk lemans-evk-camera-csi1-imx577 lemans-el2 lemans-evk-sd-card"
FIT_DTB_COMPATIBLE[qcom_qcs9075-iot-staging] = \
    "lemans-evk lemans-evk-camera-csi1-imx577 lemans-staging lemans-el2 lemans-evk-sd-card"
FIT_DTB_COMPATIBLE[qcom_qcs9075-iot-el2gh-staging] = \
    "lemans-evk lemans-evk-camera-csi1-imx577 lemans-staging lemans-evk-sd-card"
FIT_DTB_COMPATIBLE[qcom_qcs9075-iot-camx] = \
    "lemans-evk lemans-evk-camx lemans-el2 lemans-camx-el2 lemans-evk-sd-card"
FIT_DTB_COMPATIBLE[qcom_qcs9075-iot-camx-staging] = \
    "lemans-evk lemans-evk-camx lemans-el2 lemans-camx-el2 lemans-staging lemans-evk-sd-card"
FIT_DTB_COMPATIBLE[qcom_qcs9075-iot-el2gh-camx] = \
    "lemans-evk lemans-evk-camx lemans-evk-sd-card"
FIT_DTB_COMPATIBLE[qcom_qcs9075-iot-el2gh-camx-staging] = \
    "lemans-evk lemans-evk-camx lemans-staging lemans-evk-sd-card"
FIT_DTB_COMPATIBLE[qcom_qcs9075-iot-subtype1] = \
    "lemans-evk lemans-evk-ifp-mezzanine lemans-evk-sd-card"
FIT_DTB_COMPATIBLE[qcom_qcs9075-iot-subtype1-staging] = \
    "lemans-evk lemans-evk-ifp-mezzanine lemans-staging lemans-evk-staging lemans-evk-sd-card"
FIT_DTB_COMPATIBLE[qcom_qcs9075-iot-subtype1-emmc] = \
    "lemans-evk lemans-evk-ifp-mezzanine lemans-evk-emmc"
FIT_DTB_COMPATIBLE[qcom_qcs9075-iot-subtype1-staging-emmc] = \
    "lemans-evk lemans-evk-ifp-mezzanine lemans-staging lemans-evk-staging lemans-evk-emmc"

FIT_DTB_COMPATIBLE[qcom_qcs9100-qam-el2gh-staging] = \
    "qcs9100-ride lemans-staging"
FIT_DTB_COMPATIBLE[qcom_qcs9100-qam] = \
    "qcs9100-ride lemans-el2"
FIT_DTB_COMPATIBLE[qcom_qcs9100-qam-staging] = \
    "qcs9100-ride lemans-el2 lemans-staging"
FIT_DTB_COMPATIBLE[qcom_qcs9100-qam-camx] = \
    "qcs9100-ride sa8775p-ride-camx"
FIT_DTB_COMPATIBLE[qcom_qcs9100-qam-camx-staging] = \
    "qcs9100-ride sa8775p-ride-camx lemans-staging"
FIT_DTB_COMPATIBLE[qcom_qcs9100-qam-r2.0-el2gh-staging] = \
    "qcs9100-ride-r3 lemans-staging"
FIT_DTB_COMPATIBLE[qcom_qcs9100-qam-r2.0] = \
    "qcs9100-ride-r3 lemans-el2"
FIT_DTB_COMPATIBLE[qcom_qcs9100-qam-r2.0-staging] = \
    "qcs9100-ride-r3 lemans-el2 lemans-staging"
FIT_DTB_COMPATIBLE[qcom_qcs9100-qam-r2.0-camx] = \
    "qcs9100-ride-r3 sa8775p-ride-camx"
FIT_DTB_COMPATIBLE[qcom_qcs9100-qam-r2.0-camx-staging] = \
    "qcs9100-ride-r3 sa8775p-ride-camx lemans-staging"

FIT_DTB_COMPATIBLE[qcom_sa8775p-qam-el2gh-staging] = \
    "sa8775p-ride lemans-staging"
FIT_DTB_COMPATIBLE[qcom_sa8775p-qam] = \
    "sa8775p-ride lemans-el2"
FIT_DTB_COMPATIBLE[qcom_sa8775p-qam-staging] = \
    "sa8775p-ride lemans-el2 lemans-staging"
FIT_DTB_COMPATIBLE[qcom_sa8775p-qam-el2gh-camx] = \
    "sa8775p-ride sa8775p-ride-camx"
FIT_DTB_COMPATIBLE[qcom_sa8775p-qam-camx-el2gh-staging] = \
    "sa8775p-ride sa8775p-ride-camx lemans-staging"
FIT_DTB_COMPATIBLE[qcom_sa8775p-qam-camx] = \
    "sa8775p-ride sa8775p-ride-camx lemans-el2 lemans-camx-el2"
FIT_DTB_COMPATIBLE[qcom_sa8775p-qam-camx-staging] = \
    "sa8775p-ride sa8775p-ride-camx lemans-el2 lemans-camx-el2 lemans-staging"
FIT_DTB_COMPATIBLE[qcom_sa8775p-qam-r2.0-el2gh-staging] = \
    "sa8775p-ride-r3 lemans-staging"
FIT_DTB_COMPATIBLE[qcom_sa8775p-qam-r2.0] = \
    "sa8775p-ride-r3 lemans-el2"
FIT_DTB_COMPATIBLE[qcom_sa8775p-qam-r2.0-staging] = \
    "sa8775p-ride-r3 lemans-el2 lemans-staging"
FIT_DTB_COMPATIBLE[qcom_sa8775p-qam-r2.0-el2gh-camx] = \
    "sa8775p-ride-r3 sa8775p-ride-camx"
FIT_DTB_COMPATIBLE[qcom_sa8775p-qam-r2.0-el2gh-camx-staging] = \
    "sa8775p-ride-r3 sa8775p-ride-camx lemans-staging"
FIT_DTB_COMPATIBLE[qcom_sa8775p-qam-r2.0-camx] = \
    "sa8775p-ride-r3 sa8775p-ride-camx lemans-el2 lemans-camx-el2"
FIT_DTB_COMPATIBLE[qcom_sa8775p-qam-r2.0-camx-staging] = \
    "sa8775p-ride-r3 sa8775p-ride-camx lemans-el2 lemans-camx-el2 lemans-staging"

# ---------- monaco ----------
FIT_DTB_COMPATIBLE[qcom_qcs8275-iot-el2gh-staging] = \
    "monaco-evk monaco-evk-camera-imx577 monaco-staging"
FIT_DTB_COMPATIBLE[qcom_qcs8275-iot] = \
    "monaco-evk monaco-evk-camera-imx577 monaco-el2"
FIT_DTB_COMPATIBLE[qcom_qcs8275-iot-staging] = \
    "monaco-evk monaco-evk-camera-imx577 monaco-el2 monaco-staging"
FIT_DTB_COMPATIBLE[qcom_qcs8275-iot-el2gh-camx] = "monaco-evk monaco-evk-camx"
FIT_DTB_COMPATIBLE[qcom_qcs8275-iot-el2gh-camx-staging] = \
    "monaco-evk monaco-evk-camx monaco-staging"
FIT_DTB_COMPATIBLE[qcom_qcs8275-iot-camx] = \
    "monaco-evk monaco-evk-camx monaco-el2 monaco-camx-el2"
FIT_DTB_COMPATIBLE[qcom_qcs8275-iot-camx-staging] = \
    "monaco-evk monaco-evk-camx monaco-el2 monaco-camx-el2 monaco-staging"
FIT_DTB_COMPATIBLE[qcom_qcs8275-iot-subtype0] = "monaco-ac-evk monaco-evk-camera-imx577"
FIT_DTB_COMPATIBLE[qcom_qcs8275-iot-subtype1] = "monaco-ac-evk monaco-evk-ifp-mezzanine"
FIT_DTB_COMPATIBLE[qcom_qcs8275-iot-subtype3] = "monaco-evk monaco-evk-ifp-mezzanine"
FIT_DTB_COMPATIBLE[qcom_qcs8275-iot-subtype3-staging] = \
    "monaco-evk monaco-evk-ifp-mezzanine monaco-staging monaco-evk-staging"

FIT_DTB_COMPATIBLE[qcom_qcs8300-adp-el2gh-staging] = "qcs8300-ride monaco-staging"
FIT_DTB_COMPATIBLE[qcom_qcs8300-adp] = "qcs8300-ride monaco-el2"
FIT_DTB_COMPATIBLE[qcom_qcs8300-adp-staging] = \
    "qcs8300-ride monaco-el2 monaco-staging"
FIT_DTB_COMPATIBLE[qcom_qcs8300-adp-el2gh-camx] = "qcs8300-ride qcs8300-ride-camx"
FIT_DTB_COMPATIBLE[qcom_qcs8300-adp-el2gh-camx-staging] = \
    "qcs8300-ride qcs8300-ride-camx monaco-staging"
FIT_DTB_COMPATIBLE[qcom_qcs8300-adp-camx] = \
    "qcs8300-ride qcs8300-ride-camx monaco-el2 monaco-camx-el2"
FIT_DTB_COMPATIBLE[qcom_qcs8300-adp-camx-staging] = \
    "qcs8300-ride qcs8300-ride-camx monaco-el2 monaco-camx-el2 monaco-staging"

# ---------- talos ----------
FIT_DTB_COMPATIBLE[qcom_qcs615-adp-el2gh-staging] = \
    "qcs615-ride talos-staging"
FIT_DTB_COMPATIBLE[qcom_qcs615-adp] = \
    "qcs615-ride talos-el2"
FIT_DTB_COMPATIBLE[qcom_qcs615-adp-staging] = \
    "qcs615-ride talos-el2 talos-staging"
FIT_DTB_COMPATIBLE[qcom_qcs615-iot-el2gh-staging] = \
    "talos-evk talos-evk-camera-imx577 talos-staging"
FIT_DTB_COMPATIBLE[qcom_qcs615-iot] = \
    "talos-evk talos-evk-camera-imx577 talos-el2"
FIT_DTB_COMPATIBLE[qcom_qcs615-iot-staging] = \
    "talos-evk talos-evk-camera-imx577 talos-el2 talos-staging talos-evk-staging"
FIT_DTB_COMPATIBLE[qcom_qcs615-iot-el2gh-camx] = "talos-evk talos-evk-camx"
FIT_DTB_COMPATIBLE[qcom_qcs615-iot-camx-staging] = \
    "talos-evk talos-evk-camx talos-staging"
FIT_DTB_COMPATIBLE[qcom_qcs615-iot-camx] = \
    "talos-evk talos-evk-camx talos-el2"
FIT_DTB_COMPATIBLE[qcom_qcs615-iot-el2gh-camx-staging] = \
    "talos-evk talos-evk-camx talos-el2 talos-staging"
# Compatible string "qcom,talos-evk-lvds-auo,g133han01-staging" encodes as
# "qcom_talos-evk-lvds-auo_g133han01-staging" (both commas become underscores).
FIT_DTB_COMPATIBLE[qcom_talos-evk-lvds-auo_g133han01-el2gh-staging] = \
    "talos-evk talos-evk-lvds-auo_g133han01 talos-staging"
FIT_DTB_COMPATIBLE[qcom_talos-evk-lvds-auo_g133han01] = \
    "talos-evk talos-evk-lvds-auo_g133han01 talos-el2"
FIT_DTB_COMPATIBLE[qcom_talos-evk-lvds-auo_g133han01-staging] = \
    "talos-evk talos-evk-lvds-auo_g133han01 talos-el2 talos-staging"

# ---------- kodiak ----------
FIT_DTB_COMPATIBLE[qcom_qcm6490-idp-staging] = "qcm6490-idp kodiak-staging"
FIT_DTB_COMPATIBLE[qcom_qcm6490-idp-el2kvm] = "qcm6490-idp kodiak-el2"

FIT_DTB_COMPATIBLE[qcom_qcs5430-iot-el2kvm] = \
    "qcs6490-rb3gen2 kodiak-el2"
FIT_DTB_COMPATIBLE[qcom_qcs6490-iot-el2kvm] = \
    "qcs6490-rb3gen2 kodiak-el2"
FIT_DTB_COMPATIBLE[qcom_qcs5430-iot-staging] = \
    "qcs6490-rb3gen2 kodiak-staging qcs6490-rb3gen2-staging"
FIT_DTB_COMPATIBLE[qcom_qcs6490-iot-staging] = \
    "qcs6490-rb3gen2 kodiak-staging qcs6490-rb3gen2-staging"

FIT_DTB_COMPATIBLE[qcom_qcs5430-iot-subtype9-el2kvm] = \
    "qcs6490-rb3gen2 qcs6490-rb3gen2-industrial-mezzanine kodiak-el2"
FIT_DTB_COMPATIBLE[qcom_qcs6490-iot-subtype9-el2kvm] = \
    "qcs6490-rb3gen2 qcs6490-rb3gen2-industrial-mezzanine kodiak-el2"
FIT_DTB_COMPATIBLE[qcom_qcs5430-iot-subtype9-staging] = \
    "qcs6490-rb3gen2 qcs6490-rb3gen2-industrial-mezzanine kodiak-staging qcs6490-rb3gen2-industrial-mezzanine-staging"
FIT_DTB_COMPATIBLE[qcom_qcs6490-iot-subtype9-staging] = \
    "qcs6490-rb3gen2 qcs6490-rb3gen2-industrial-mezzanine kodiak-staging qcs6490-rb3gen2-industrial-mezzanine-staging"

FIT_DTB_COMPATIBLE[qcom_qcs5430-iot-subtype2-el2kvm] = \
    "qcs6490-rb3gen2 qcs6490-rb3gen2-vision-mezzanine kodiak-el2"
FIT_DTB_COMPATIBLE[qcom_qcs6490-iot-subtype2-el2kvm] = \
    "qcs6490-rb3gen2 qcs6490-rb3gen2-vision-mezzanine kodiak-el2"
FIT_DTB_COMPATIBLE[qcom_qcs5430-iot-subtype2-staging] = \
    "qcs6490-rb3gen2 qcs6490-rb3gen2-vision-mezzanine kodiak-staging"
FIT_DTB_COMPATIBLE[qcom_qcs6490-iot-subtype2-staging] = \
    "qcs6490-rb3gen2 qcs6490-rb3gen2-vision-mezzanine kodiak-staging"

FIT_DTB_COMPATIBLE[qcom_qcs5430-iot-camx] = \
    "qcs6490-rb3gen2 qcs6490-rb3gen2-vision-mezzanine-camx"
FIT_DTB_COMPATIBLE[qcom_qcs5430-iot-subtype2-camx] = \
    "qcs6490-rb3gen2 qcs6490-rb3gen2-vision-mezzanine-camx"
FIT_DTB_COMPATIBLE[qcom_qcs6490-iot-camx] = \
    "qcs6490-rb3gen2 qcs6490-rb3gen2-vision-mezzanine-camx"
FIT_DTB_COMPATIBLE[qcom_qcs6490-iot-subtype2-camx] = \
    "qcs6490-rb3gen2 qcs6490-rb3gen2-vision-mezzanine-camx"
FIT_DTB_COMPATIBLE[qcom_qcs5430-iot-camx-staging] = \
    "qcs6490-rb3gen2 qcs6490-rb3gen2-vision-mezzanine-camx kodiak-staging"
FIT_DTB_COMPATIBLE[qcom_qcs5430-iot-subtype2-camx-staging] = \
    "qcs6490-rb3gen2 qcs6490-rb3gen2-vision-mezzanine-camx kodiak-staging"
FIT_DTB_COMPATIBLE[qcom_qcs6490-iot-camx-staging] = \
    "qcs6490-rb3gen2 qcs6490-rb3gen2-vision-mezzanine-camx kodiak-staging"
FIT_DTB_COMPATIBLE[qcom_qcs6490-iot-subtype2-camx-staging] = \
    "qcs6490-rb3gen2 qcs6490-rb3gen2-vision-mezzanine-camx kodiak-staging"
# ---------- glymur ----------
FIT_DTB_COMPATIBLE[qcom_glymur-crd-staging] = "glymur-crd glymur-staging"
