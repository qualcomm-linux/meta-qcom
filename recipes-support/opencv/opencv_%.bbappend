######### Qcom overrides ##########
SUMMARY = "Opencv : The Open Computer Vision Library, Qualcomm Fork"

# Adding FASTCV HAL and EXTENSION patches
FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI += "file://0001-PENDING-Removed-cluster-euclidean-in-fastcv-ext.patch;patchdir=${S}/contrib/ \
            file://0001-FROMLIST-Using-fastcv-static-lib-for-compilation.patch"

EXTRA_OECMAKE += "-DOPENCV_ALLOW_DOWNLOADS=ON "
EXTRA_OECMAKE += " -DWITH_FASTCV=ON "
RM_WORK_EXCLUDE += "${PN}"

COMPATIBLE_MACHINE = "(qcm6490-idp|qcs6490-rb3gen2-vision-kit|qcs6490-rb3gen2-core-kit|qcs6490-rb3gen2-industrial-kit|qcs9100-ride-sx|qcs8300-ride-sx|qcs9075-ride-sx|qcs9075-rb8-core-kit)"
