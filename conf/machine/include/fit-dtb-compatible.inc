# Mapping from a DTB (and optional overlays on top) to its
# Device Tree "compatible" string(s).
#
# Syntax:
# FIT_DTB_COMPATIBLE[<dtb-name>[+<overlay>[+<overlay>...]]] = "<compatible>[ <compatible>[ <compatible>...]]"

FIT_DTB_COMPATIBLE[apq8016-sbc] = "qcom,apq8016-sbc"
FIT_DTB_COMPATIBLE[apq8096-db820c] = "qcom,apq8096-sbc"
FIT_DTB_COMPATIBLE[hamoa-iot-evk] = " \
    qcom,hamoa-evk \
    qcom,hamoav2.1-evk \
"
FIT_DTB_COMPATIBLE[purwa-iot-evk] = "qcom,purwa-evk"
FIT_DTB_COMPATIBLE[kaanapali-mtp] = "qcom,kaanapali-mtp"
FIT_DTB_COMPATIBLE[lemans-evk+lemans-evk-camera-csi1-imx577] = " \
    qcom,qcs9075-iot \
    qcom,qcs9075v2-iot \
"
FIT_DTB_COMPATIBLE[lemans-evk+lemans-evk-camx] = " \
    qcom,qcs9075-iot-camx \
    qcom,qcs9075v2-iot-camx \
"
FIT_DTB_COMPATIBLE[lemans-evk+lemans-el2] = " \
    qcom,qcs9075-iot-el2kvm \
    qcom,qcs9075v2-iot-el2kvm \
"
FIT_DTB_COMPATIBLE[lemans-evk+lemans-evk-camx+lemans-el2] = " \
    qcom,qcs9075-iot-camx-el2kvm \
    qcom,qcs9075v2-iot-camx-el2kvm \
"
FIT_DTB_COMPATIBLE[monaco-evk+monaco-evk-camera-imx577] = "qcom,qcs8275-iot"
FIT_DTB_COMPATIBLE[monaco-evk+monaco-evk-camx] = "qcom,qcs8275-iot-camx"
FIT_DTB_COMPATIBLE[qcm6490-idp] = "qcom,qcm6490-idp"
FIT_DTB_COMPATIBLE[qcom-apq8064-asus-nexus7-flo] = "qcom,apq8064"
FIT_DTB_COMPATIBLE[qcom-apq8064-ifc6410] = "qcom,apq8064-ifc6410"
FIT_DTB_COMPATIBLE[qcom-apq8074-dragonboard] = "qcom,apq8074-dragonboard"
FIT_DTB_COMPATIBLE[qcom-apq8084-ifc6540] = "qcom,apq8084-sbc"
FIT_DTB_COMPATIBLE[qcom-msm8974-lge-nexus5-hammerhead] = "qcom,msm8974"
FIT_DTB_COMPATIBLE[qcs404-evb-4000] = "qcom,qcs404-evb-4000"
FIT_DTB_COMPATIBLE[qcs615-ride] = " \
    qcom,qcs615-adp \
    qcom,qcs615v1.1-adp \
"
FIT_DTB_COMPATIBLE[qcs6490-rb3gen2] = " \
    qcom,qcs5430-iot \
    qcom,qcs6490-iot \
"
FIT_DTB_COMPATIBLE[qcs6490-rb3gen2+qcs6490-rb3gen2-industrial-mezzanine] = " \
    qcom,qcs5430-iot-subtype9 \
    qcom,qcs6490-iot-subtype9 \
"
FIT_DTB_COMPATIBLE[qcs6490-rb3gen2+qcs6490-rb3gen2-vision-mezzanine] = " \
    qcom,qcs5430-iot-subtype2 \
    qcom,qcs6490-iot-subtype2 \
"
FIT_DTB_COMPATIBLE[qcs6490-rb3gen2+qcs6490-rb3gen2-vision-mezzanine-camx] = " \
    qcom,qcs6490-iot-camx \
    qcom,qcs6490-iot-subtype2-camx \
"
FIT_DTB_COMPATIBLE[qcs8300-ride] = "qcom,qcs8300-adp"
FIT_DTB_COMPATIBLE[qcs8300-ride+qcs8300-ride-camx] = "qcom,qcs8300-adp-camx"
FIT_DTB_COMPATIBLE[qcs9100-ride] = " \
    qcom,qcs9100-qam \
    qcom,qcs9100v2-qam \
"
FIT_DTB_COMPATIBLE[qcs9100-ride+sa8775p-ride-camx] = " \
    qcom,qcs9100-qam-camx \
    qcom,qcs9100v2-qam-camx \
"
FIT_DTB_COMPATIBLE[qcs9100-ride+lemans-el2] = " \
    qcom,qcs9100-qam-el2kvm \
    qcom,qcs9100v2-qam-el2kvm \
"
FIT_DTB_COMPATIBLE[qcs9100-ride-r3] = " \
    qcom,qcs9100-qamr2 \
    qcom,qcs9100v2-qamr2 \
"
FIT_DTB_COMPATIBLE[qcs9100-ride-r3+sa8775p-ride-camx] = " \
    qcom,qcs9100-qamr2-camx \
    qcom,qcs9100v2-qamr2-camx \
"
FIT_DTB_COMPATIBLE[qcs9100-ride-r3+fdt-lemans-el2] = " \
    qcom,qcs9100-qamr2-el2kvm \
    qcom,qcs9100v2-qamr2-el2kvm \
"
FIT_DTB_COMPATIBLE[qrb2210-rb1] = "qcom,qrb2210-rb1"
FIT_DTB_COMPATIBLE[qrb4210-rb2] = "qcom,qrb4210-rb2"
FIT_DTB_COMPATIBLE[qrb5165-rb5] = "qcom,qrb5165-rb5"
FIT_DTB_COMPATIBLE[sa8775p-ride] = " \
    qcom,sa8775p-qam \
    qcom,sa8775pv2-qam \
"
FIT_DTB_COMPATIBLE[sa8775p-ride+sa8775p-ride-camx] = " \
    qcom,sa8775p-qam-camx \
    qcom,sa8775pv2-qam-camx \
"
FIT_DTB_COMPATIBLE[sa8775p-ride-camx] = "qcom,sa8775p-qam-camx"
FIT_DTB_COMPATIBLE[sa8775p-ride-r3] = " \
    qcom,sa8775p-qamr2 \
    qcom,sa8775pv2-qamr2 \
"
FIT_DTB_COMPATIBLE[sa8775p-ride-r3+sa8775p-ride-camx] = " \
    qcom,sa8775p-qamr2-camx \
    qcom,sa8775pv2-qamr2-camx \
"
FIT_DTB_COMPATIBLE[sdm845-db845c] = "qcom,sdm845"
FIT_DTB_COMPATIBLE[sm8450-hdk] = "qcom,sm8450-hdk"
FIT_DTB_COMPATIBLE[sm8750-mtp] = "qcom,sm8750-mtp"
FIT_DTB_COMPATIBLE[talos-evk+talos-evk-camera-imx577] = "qcom,qcs615v1.1-iot"
FIT_DTB_COMPATIBLE[talos-evk+talos-evk-camx] = "qcom,qcs615v1.1-iot-camx"
FIT_DTB_COMPATIBLE[talos-evk+talos-evk-lvds-auo_g133han01] = "qcom,talos-evk-lvds-auo,g133han01"
