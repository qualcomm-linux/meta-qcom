# Mapping from a DTB (and optional overlays on top) to its
# Device Tree "compatible" string(s).
#
# Syntax:
# FIT_DTB_COMPATIBLE[<dtb-name>[+<overlay>[+<overlay>...]]] = "<compatible>[ <compatible>[ <compatible>...]]"

FIT_DTB_COMPATIBLE[apq8016-sbc] = "qcom,apq8016-sbc"
FIT_DTB_COMPATIBLE[apq8096-db820c] = "qcom,apq8096-sbc"
FIT_DTB_COMPATIBLE[glymur-crd] = "qcom,glymur-crd"
FIT_DTB_COMPATIBLE[hamoa-iot-evk+hamoa-iot-evk-camera-imx577] = " \
    qcom,hamoa-evk \
"
FIT_DTB_COMPATIBLE[hamoa-iot-evk+hamoa-evk-camx] = " \
    qcom,hamoa-evk-camx \
"
FIT_DTB_COMPATIBLE[hamoa-iot-evk+hamoa-evk-camx+x1-el2+hamoa-camx-el2] = " \
    qcom,hamoa-evk-camx-el2kvm \
"
FIT_DTB_COMPATIBLE[kaanapali-mtp] = "qcom,kaanapali-mtp"
FIT_DTB_COMPATIBLE[lemans-evk+lemans-evk-camera-csi1-imx577] = " \
    qcom,qcs9075-iot \
"
FIT_DTB_COMPATIBLE[lemans-evk+lemans-evk-camera-csi1-imx577+lemans-staging] = " \
    qcom,qcs9075-iot-staging \
"
FIT_DTB_COMPATIBLE[lemans-evk+lemans-evk-camx] = " \
    qcom,qcs9075-iot-camx \
"
FIT_DTB_COMPATIBLE[lemans-evk+lemans-evk-camx+lemans-staging] = " \
    qcom,qcs9075-iot-camx-staging \
"
FIT_DTB_COMPATIBLE[lemans-evk+lemans-evk-camera-csi1-imx577+lemans-el2] = " \
    qcom,qcs9075-iot-el2kvm \
"
FIT_DTB_COMPATIBLE[lemans-evk+lemans-evk-camera-csi1-imx577+lemans-el2+lemans-staging] = " \
    qcom,qcs9075-iot-el2kvm-staging \
"
FIT_DTB_COMPATIBLE[lemans-evk+lemans-evk-camx+lemans-el2+lemans-camx-el2] = " \
    qcom,qcs9075-iot-camx-el2kvm \
"
FIT_DTB_COMPATIBLE[lemans-evk+lemans-evk-camx+lemans-el2+lemans-camx-el2+lemans-staging] = " \
    qcom,qcs9075-iot-camx-el2kvm-staging \
"
FIT_DTB_COMPATIBLE[lemans-evk+lemans-evk-ifp-mezzanine] = " \
    qcom,qcs9075-iot-subtype1 \
"
FIT_DTB_COMPATIBLE[lemans-evk+lemans-evk-ifp-mezzanine+lemans-staging+lemans-evk-staging] = " \
    qcom,qcs9075-iot-subtype1-staging \
"
FIT_DTB_COMPATIBLE[monaco-evk+monaco-evk-camera-imx577+monaco-el2] = "qcom,qcs8275-iot-el2kvm"
FIT_DTB_COMPATIBLE[monaco-evk+monaco-evk-camera-imx577+monaco-el2+monaco-staging] = "qcom,qcs8275-iot-el2kvm-staging"
FIT_DTB_COMPATIBLE[monaco-evk+monaco-evk-camera-imx577] = "qcom,qcs8275-iot"
FIT_DTB_COMPATIBLE[monaco-evk+monaco-evk-camera-imx577+monaco-staging] = "qcom,qcs8275-iot-staging"
FIT_DTB_COMPATIBLE[monaco-evk+monaco-evk-camx] = "qcom,qcs8275-iot-camx"
FIT_DTB_COMPATIBLE[monaco-evk+monaco-evk-camx+monaco-staging] = "qcom,qcs8275-iot-camx-staging"
FIT_DTB_COMPATIBLE[monaco-evk+monaco-evk-camx+monaco-el2+monaco-camx-el2] = "qcom,qcs8275-iot-camx-el2kvm"
FIT_DTB_COMPATIBLE[monaco-evk+monaco-evk-camx+monaco-el2+monaco-camx-el2+monaco-staging] = "qcom,qcs8275-iot-camx-el2kvm-staging"
FIT_DTB_COMPATIBLE[monaco-evk+monaco-evk-ifp-mezzanine] = "qcom,qcs8275-iot-subtype3"
FIT_DTB_COMPATIBLE[monaco-evk+monaco-evk-ifp-mezzanine+monaco-staging+monaco-evk-staging] = "qcom,qcs8275-iot-subtype3-staging"
FIT_DTB_COMPATIBLE[purwa-iot-evk] = "qcom,purwa-evk"
FIT_DTB_COMPATIBLE[purwa-iot-evk+purwa-evk-camx] = "qcom,purwa-evk-camx"
FIT_DTB_COMPATIBLE[purwa-iot-evk+purwa-evk-camx+x1-el2+purwa-camx-el2] = "qcom,purwa-evk-camx-el2kvm"
FIT_DTB_COMPATIBLE[qcm6490-idp] = "qcom,qcm6490-idp"
FIT_DTB_COMPATIBLE[qcm6490-idp+kodiak-staging] = "qcom,qcm6490-idp-staging"
FIT_DTB_COMPATIBLE[qcm6490-idp+kodiak-el2] = " \
    qcom,qcm6490-idp-el2kvm \
"
FIT_DTB_COMPATIBLE[qcom-apq8064-asus-nexus7-flo] = "qcom,apq8064"
FIT_DTB_COMPATIBLE[qcom-apq8064-ifc6410] = "qcom,apq8064-ifc6410"
FIT_DTB_COMPATIBLE[qcom-apq8074-dragonboard] = "qcom,apq8074-dragonboard"
FIT_DTB_COMPATIBLE[qcom-apq8084-ifc6540] = "qcom,apq8084-sbc"
FIT_DTB_COMPATIBLE[qcom-msm8974-lge-nexus5-hammerhead] = "qcom,msm8974"
FIT_DTB_COMPATIBLE[qcs404-evb-4000] = "qcom,qcs404-evb-4000"
FIT_DTB_COMPATIBLE[qcs615-ride] = " \
    qcom,qcs615-adp \
"
FIT_DTB_COMPATIBLE[qcs615-ride+talos-staging] = " \
    qcom,qcs615-adp-staging \
"
FIT_DTB_COMPATIBLE[qcs615-ride+talos-el2] = " \
    qcom,qcs615-adp-el2kvm \
"
FIT_DTB_COMPATIBLE[qcs615-ride+talos-el2+talos-staging] = " \
    qcom,qcs615-adp-el2kvm-staging \
"
FIT_DTB_COMPATIBLE[qcs6490-rb3gen2] = " \
    qcom,qcs5430-iot \
    qcom,qcs6490-iot \
"
FIT_DTB_COMPATIBLE[qcs6490-rb3gen2+kodiak-el2] = " \
    qcom,qcs5430-iot-el2kvm \
    qcom,qcs6490-iot-el2kvm \
"
FIT_DTB_COMPATIBLE[qcs6490-rb3gen2+kodiak-staging+qcs6490-rb3gen2-staging] = " \
    qcom,qcs5430-iot-staging \
    qcom,qcs6490-iot-staging \
"
FIT_DTB_COMPATIBLE[qcs6490-rb3gen2+qcs6490-rb3gen2-industrial-mezzanine] = " \
    qcom,qcs5430-iot-subtype9 \
    qcom,qcs6490-iot-subtype9 \
"
FIT_DTB_COMPATIBLE[qcs6490-rb3gen2+qcs6490-rb3gen2-industrial-mezzanine+kodiak-el2] = " \
    qcom,qcs5430-iot-subtype9-el2kvm \
    qcom,qcs6490-iot-subtype9-el2kvm \
"
FIT_DTB_COMPATIBLE[qcs6490-rb3gen2+qcs6490-rb3gen2-industrial-mezzanine+kodiak-staging+qcs6490-rb3gen2-industrial-mezzanine-staging] = " \
    qcom,qcs5430-iot-subtype9-staging \
    qcom,qcs6490-iot-subtype9-staging \
"
FIT_DTB_COMPATIBLE[qcs6490-rb3gen2+qcs6490-rb3gen2-vision-mezzanine] = " \
    qcom,qcs5430-iot-subtype2 \
    qcom,qcs6490-iot-subtype2 \
"
FIT_DTB_COMPATIBLE[qcs6490-rb3gen2+qcs6490-rb3gen2-vision-mezzanine+kodiak-el2] = " \
    qcom,qcs5430-iot-subtype2-el2kvm \
    qcom,qcs6490-iot-subtype2-el2kvm \
"
FIT_DTB_COMPATIBLE[qcs6490-rb3gen2+qcs6490-rb3gen2-vision-mezzanine+kodiak-staging] = " \
    qcom,qcs5430-iot-subtype2-staging \
    qcom,qcs6490-iot-subtype2-staging \
"
FIT_DTB_COMPATIBLE[qcs6490-rb3gen2+qcs6490-rb3gen2-vision-mezzanine-camx] = " \
    qcom,qcs5430-iot-camx \
    qcom,qcs5430-iot-subtype2-camx \
    qcom,qcs6490-iot-camx \
    qcom,qcs6490-iot-subtype2-camx \
"
FIT_DTB_COMPATIBLE[qcs6490-rb3gen2+qcs6490-rb3gen2-vision-mezzanine-camx+kodiak-staging] = " \
    qcom,qcs5430-iot-camx-staging \
    qcom,qcs5430-iot-subtype2-camx-staging \
    qcom,qcs6490-iot-camx-staging \
    qcom,qcs6490-iot-subtype2-camx-staging \
"
FIT_DTB_COMPATIBLE[qcs8300-ride] = "qcom,qcs8300-adp"
FIT_DTB_COMPATIBLE[qcs8300-ride+monaco-staging] = "qcom,qcs8300-adp-staging"
FIT_DTB_COMPATIBLE[qcs8300-ride+monaco-el2] = "qcom,qcs8300-adp-el2kvm"
FIT_DTB_COMPATIBLE[qcs8300-ride+monaco-el2+monaco-staging] = "qcom,qcs8300-adp-el2kvm-staging"
FIT_DTB_COMPATIBLE[qcs8300-ride+qcs8300-ride-camx] = "qcom,qcs8300-adp-camx"
FIT_DTB_COMPATIBLE[qcs8300-ride+qcs8300-ride-camx+monaco-staging] = "qcom,qcs8300-adp-camx-staging"
FIT_DTB_COMPATIBLE[qcs8300-ride+qcs8300-ride-camx+monaco-el2+monaco-camx-el2] = "qcom,qcs8300-adp-camx-el2kvm"
FIT_DTB_COMPATIBLE[qcs8300-ride+qcs8300-ride-camx+monaco-el2+monaco-camx-el2+monaco-staging] = "qcom,qcs8300-adp-camx-el2kvm-staging"
FIT_DTB_COMPATIBLE[qcs9100-ride] = " \
    qcom,qcs9100-qam \
"
FIT_DTB_COMPATIBLE[qcs9100-ride+lemans-staging] = " \
    qcom,qcs9100-qam-staging \
"
FIT_DTB_COMPATIBLE[qcs9100-ride+sa8775p-ride-camx] = " \
    qcom,qcs9100-qam-camx \
"
FIT_DTB_COMPATIBLE[qcs9100-ride+sa8775p-ride-camx+lemans-staging] = " \
    qcom,qcs9100-qam-camx-staging \
"
FIT_DTB_COMPATIBLE[qcs9100-ride+lemans-el2] = " \
    qcom,qcs9100-qam-el2kvm \
"
FIT_DTB_COMPATIBLE[qcs9100-ride+lemans-el2+lemans-staging] = " \
    qcom,qcs9100-qam-el2kvm-staging \
"
FIT_DTB_COMPATIBLE[qcs9100-ride-r3] = " \
    qcom,qcs9100-qam-r2.0 \
"
FIT_DTB_COMPATIBLE[qcs9100-ride-r3+lemans-staging] = " \
    qcom,qcs9100-qam-r2.0-staging \
"
FIT_DTB_COMPATIBLE[qcs9100-ride-r3+sa8775p-ride-camx] = " \
    qcom,qcs9100-qam-r2.0-camx \
"
FIT_DTB_COMPATIBLE[qcs9100-ride-r3+sa8775p-ride-camx+lemans-staging] = " \
    qcom,qcs9100-qam-r2.0-camx-staging \
"
FIT_DTB_COMPATIBLE[qcs9100-ride-r3+lemans-el2] = " \
    qcom,qcs9100-qam-r2.0-el2kvm \
"
FIT_DTB_COMPATIBLE[qcs9100-ride-r3+lemans-el2+lemans-staging] = " \
    qcom,qcs9100-qam-r2.0-el2kvm-staging \
"
FIT_DTB_COMPATIBLE[qrb2210-rb1] = "qcom,qrb2210-rb1"
FIT_DTB_COMPATIBLE[qrb4210-rb2] = "qcom,qrb4210-rb2"
FIT_DTB_COMPATIBLE[qrb5165-rb5] = "qcom,qrb5165-rb5"
FIT_DTB_COMPATIBLE[sa8775p-ride] = " \
    qcom,sa8775p-qam \
"
FIT_DTB_COMPATIBLE[sa8775p-ride+lemans-staging] = " \
    qcom,sa8775p-qam-staging \
"
FIT_DTB_COMPATIBLE[sa8775p-ride+lemans-el2] = " \
    qcom,sa8775p-qam-el2kvm \
"
FIT_DTB_COMPATIBLE[sa8775p-ride+lemans-el2+lemans-staging] = " \
    qcom,sa8775p-qam-el2kvm-staging \
"
FIT_DTB_COMPATIBLE[sa8775p-ride+sa8775p-ride-camx] = " \
    qcom,sa8775p-qam-camx \
"
FIT_DTB_COMPATIBLE[sa8775p-ride+sa8775p-ride-camx+lemans-staging] = " \
    qcom,sa8775p-qam-camx-staging \
"
FIT_DTB_COMPATIBLE[sa8775p-ride+sa8775p-ride-camx+lemans-el2+lemans-camx-el2] = " \
    qcom,sa8775p-qam-camx-el2kvm \
"
FIT_DTB_COMPATIBLE[sa8775p-ride+sa8775p-ride-camx+lemans-el2+lemans-camx-el2+lemans-staging] = " \
    qcom,sa8775p-qam-camx-el2kvm-staging \
"
FIT_DTB_COMPATIBLE[sa8775p-ride-r3] = " \
    qcom,sa8775p-qam-r2.0 \
"
FIT_DTB_COMPATIBLE[sa8775p-ride-r3+lemans-staging] = " \
    qcom,sa8775p-qam-r2.0-staging \
"
FIT_DTB_COMPATIBLE[sa8775p-ride-r3+lemans-el2] = " \
    qcom,sa8775p-qam-r2.0-el2kvm \
"
FIT_DTB_COMPATIBLE[sa8775p-ride-r3+lemans-el2+lemans-staging] = " \
    qcom,sa8775p-qam-r2.0-el2kvm-staging \
"
FIT_DTB_COMPATIBLE[sa8775p-ride-r3+sa8775p-ride-camx] = " \
    qcom,sa8775p-qam-r2.0-camx \
"
FIT_DTB_COMPATIBLE[sa8775p-ride-r3+sa8775p-ride-camx+lemans-staging] = " \
    qcom,sa8775p-qam-r2.0-camx-staging \
"
FIT_DTB_COMPATIBLE[sa8775p-ride-r3+sa8775p-ride-camx+lemans-el2+lemans-camx-el2] = " \
    qcom,sa8775p-qam-r2.0-camx-el2kvm \
"
FIT_DTB_COMPATIBLE[sa8775p-ride-r3+sa8775p-ride-camx+lemans-el2+lemans-camx-el2+lemans-staging] = " \
    qcom,sa8775p-qam-r2.0-camx-el2kvm-staging \
"
FIT_DTB_COMPATIBLE[sdm845-db845c] = "qcom,sdm845"
FIT_DTB_COMPATIBLE[sm8450-hdk] = "qcom,sm8450-hdk"
FIT_DTB_COMPATIBLE[sm8750-mtp] = "qcom,sm8750-mtp"
FIT_DTB_COMPATIBLE[talos-evk+talos-evk-camera-imx577] = "qcom,qcs615-iot"
FIT_DTB_COMPATIBLE[talos-evk+talos-evk-camera-imx577+talos-staging] = "qcom,qcs615-iot-staging"
FIT_DTB_COMPATIBLE[talos-evk+talos-evk-camera-imx577+talos-el2] = "qcom,qcs615-iot-el2kvm"
FIT_DTB_COMPATIBLE[talos-evk+talos-evk-camera-imx577+talos-el2+talos-staging] = "qcom,qcs615-iot-el2kvm-staging"
FIT_DTB_COMPATIBLE[talos-evk+talos-evk-camx] = "qcom,qcs615-iot-camx"
FIT_DTB_COMPATIBLE[talos-evk+talos-evk-camx+talos-staging] = "qcom,qcs615-iot-camx-staging"
FIT_DTB_COMPATIBLE[talos-evk+talos-evk-camx+talos-el2] = "qcom,qcs615-iot-camx-el2kvm"
FIT_DTB_COMPATIBLE[talos-evk+talos-evk-camx+talos-el2+talos-staging] = "qcom,qcs615-iot-camx-el2kvm-staging"
FIT_DTB_COMPATIBLE[talos-evk+talos-evk-lvds-auo_g133han01] = "qcom,talos-evk-lvds-auo,g133han01"
FIT_DTB_COMPATIBLE[talos-evk+talos-evk-lvds-auo_g133han01+talos-staging] = "qcom,talos-evk-lvds-auo,g133han01-staging"
FIT_DTB_COMPATIBLE[talos-evk+talos-evk-lvds-auo_g133han01+talos-el2] = "qcom,talos-evk-lvds-auo,g133han01-el2kvm"
FIT_DTB_COMPATIBLE[talos-evk+talos-evk-lvds-auo_g133han01+talos-el2+talos-staging] = "qcom,talos-evk-lvds-auo,g133han01-el2kvm-staging"
