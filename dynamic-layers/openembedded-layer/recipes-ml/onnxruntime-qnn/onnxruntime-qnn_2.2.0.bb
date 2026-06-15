SUMMARY = "ONNX Runtime QNN Execution Provider Plugin"
DESCRIPTION = "Standalone ABI-stable plugin execution provider that brings Qualcomm \
hardware acceleration to ONNX Runtime via the Qualcomm AI Runtime SDK (QAIRT)."
HOMEPAGE = "https://github.com/onnxruntime/onnxruntime-qnn"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0f7e3b1308cb5c00b372a6e78835732d"

DEPENDS = " \
    zlib \
    protobuf \
    protobuf-native \
    re2 \
    abseil-cpp \
    nlohmann-json \
    microsoft-gsl \
"

SRC_URI = "gitsm://github.com/onnxruntime/onnxruntime-qnn.git;protocol=https;nobranch=1;name=ort-qnn \
    git://github.com/HowardHinnant/date.git;protocol=https;nobranch=1;name=date;tag=v3.0.4;destsuffix=date \
    git://github.com/boostorg/mp11.git;protocol=https;nobranch=1;name=mp11;tag=boost-1.91.0;destsuffix=mp11 \
    git://github.com/pytorch/cpuinfo.git;protocol=https;nobranch=1;name=pytorch_cpuinfo;destsuffix=pytorch_cpuinfo \
    git://github.com/dcleblanc/SafeInt.git;protocol=https;nobranch=1;name=safeint;tag=3.0.28;destsuffix=safeint \
    git://github.com/google/flatbuffers.git;protocol=https;nobranch=1;name=flatbuffers;tag=v23.5.26;destsuffix=flatbuffers \
    git://github.com/onnx/onnx.git;protocol=https;nobranch=1;name=onnx;tag=v1.20.1;destsuffix=onnx \
    git://github.com/eigen-mirror/eigen.git;protocol=https;nobranch=1;name=eigen3;destsuffix=eigen3 \
    git://github.com/microsoft/onnxruntime.git;protocol=https;nobranch=1;name=ort_core;destsuffix=ort_core \
    https://softwarecenter.qualcomm.com/api/download/software/sdks/Qualcomm_AI_Runtime_Community/All/2.46.0.260424/v2.46.0.260424.zip;name=qairt \
    file://0001-ort_core-drop-abseil-version-constraint-in-FetchConte.patch;patchdir=${WORKDIR}/sources/ort_core \
    file://0002-ort-qnn-forward-ONNX_CUSTOM_PROTOC_EXECUTABLE.patch;patchdir=${WORKDIR}/sources/onnxruntime-qnn-2.2.0 \
"

SRCREV_FORMAT = "ort-qnn_date_mp11_pytorch_cpuinfo_safeint_flatbuffers_onnx_eigen3_ort_core"
SRCREV_ort-qnn              = "83110054b7b588a3b94931fe0bb8d94789c0a2f5"
SRCREV_date                 = "f94b8f36c6180be0021876c4a397a054fe50c6f2"
SRCREV_mp11                 = "b94b089d4ec83cd397f20958f34edf25bc3e06f4"
SRCREV_pytorch_cpuinfo      = "ea6b9f1bb6e1001d8b21574d5bc78ddef62e499d"
SRCREV_safeint              = "4cafc9196c4da9c817992b20f5253ef967685bf8"
SRCREV_flatbuffers          = "c20d64b8de759423af61e072fcabf916c1f7bf9f"
SRCREV_onnx                 = "d3f6b795aedb48eaecc881bf5e8f5dd6efbe25b3"
SRCREV_eigen3               = "549bf8c75b6aae071cde2f28aa48f16ee3ae60b0"
SRCREV_ort_core             = "2d924974ef147392ced8409d36bd6d2e7fcc8a74"
SRC_URI[qairt.sha256sum]    = "2db536eac7556368f4e89663bd26aa0af189cf16360d74ae48324a5281f9a4e1"

# Fix buildpaths QA issue: remap TMPDIR references in both debug info and
# string literals embedded in the compiled libraries.
CFLAGS:append   = " -ffile-prefix-map=${WORKDIR}=. -ffile-prefix-map=${S}=. -ffile-prefix-map=${B}=."
CXXFLAGS:append = " -ffile-prefix-map=${WORKDIR}=. -ffile-prefix-map=${S}=. -ffile-prefix-map=${B}=."

inherit cmake

OECMAKE_SOURCEPATH = "${S}/cmake"

EXTRA_OECMAKE = " \
    -DCMAKE_BUILD_TYPE=Release \
    -DCMAKE_FIND_ROOT_PATH=${STAGING_DIR_TARGET} \
    -Donnxruntime_BUILD_SHARED_LIB=ON \
    -DFETCHCONTENT_FULLY_DISCONNECTED=ON \
    -DFETCHCONTENT_SOURCE_DIR_DATE=${WORKDIR}/sources/date \
    -DFETCHCONTENT_SOURCE_DIR_MP11=${WORKDIR}/sources/mp11 \
    -DFETCHCONTENT_SOURCE_DIR_PYTORCH_CPUINFO=${WORKDIR}/sources/pytorch_cpuinfo \
    -DFETCHCONTENT_SOURCE_DIR_SAFEINT=${WORKDIR}/sources/safeint \
    -DFETCHCONTENT_SOURCE_DIR_FLATBUFFERS=${WORKDIR}/sources/flatbuffers \
    -DFETCHCONTENT_SOURCE_DIR_ONNX=${WORKDIR}/sources/onnx \
    -DFETCHCONTENT_SOURCE_DIR_EIGEN3=${WORKDIR}/sources/eigen3 \
    -DFETCHCONTENT_SOURCE_DIR_ORT_CORE=${WORKDIR}/sources/ort_core \
    -DONNX_CUSTOM_PROTOC_EXECUTABLE=${STAGING_BINDIR_NATIVE}/protoc \
    -Donnxruntime_BUILD_UNIT_TESTS=OFF \
    -Donnxruntime_RUN_ONNX_TESTS=OFF \
    -Donnxruntime_QNN_HOME=${WORKDIR}/sources/qairt/2.46.0.260424/ \
    -Donnxruntime_USE_QNN=ON \
    -Donnxruntime_DISABLE_RTTI=OFF \
"

# FetchContent skips PATCH_COMMAND when FETCHCONTENT_SOURCE_DIR_ORT_CORE is set,
# so apply the ort_core patches manually before cmake configure.
do_configure:prepend() {
    ORT_CORE_PATCHES="${WORKDIR}/sources/onnxruntime-qnn-2.2.0/cmake/patches/ort_core"
    ORT_CORE_SRC="${WORKDIR}/sources/ort_core"
    patch --ignore-whitespace -p1 -N -d "${ORT_CORE_SRC}" \
        < "${ORT_CORE_PATCHES}/0001-cpp-model-test-runner-uses-plugin-EP.patch" || true
    patch --ignore-whitespace -p1 -N -d "${ORT_CORE_SRC}" \
        < "${ORT_CORE_PATCHES}/0002-Add-Roialign-Op-to-HTP.patch" || true
    patch --ignore-whitespace -p1 -N -d "${ORT_CORE_SRC}" \
        < "${ORT_CORE_PATCHES}/0003-Allow-users-to-specify-arm64ReproDir-by-cmake-flag.patch" || true
}

# Onnxruntime builds optional objects using all instructionset
# extensions, which will get loaded at runtime after checking
# /proc/cpuinfo. OE forcing '-mcpu' makes that fail. This is a rare case
# of upstream doing a better job than OE, so disable forcing -mcpu in
# do_compile to work around this. Keep it in do_configure to ensure the
# default tuning is correct.
# This seems to only affect aarch64, regular arm, x86 and x86-64 just work.
TARGET_CC_ARCH:aarch64 = ""
do_compile:prepend:aarch64() {
    sed -E 's/-mcpu=[^ ]+//g' -i ${WORKDIR}/toolchain.cmake
}

# libonnxruntime_providers_qnn.so is a runtime plugin, not a development linker
# stub. Remove the lib*.so glob from -dev and assign the file to the main package.
FILES:${PN}-dev:remove = "${FILES_SOLIBSDEV}"
FILES:${PN} += "${libdir}/libonnxruntime_providers_qnn.so"
