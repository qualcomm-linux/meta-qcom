SUMMARY = "ONNX Runtime QNN Execution Provider Plugin"
DESCRIPTION = "Standalone ABI-stable plugin execution provider that brings Qualcomm \
hardware acceleration to ONNX Runtime via the Qualcomm AI Runtime SDK (QAIRT)."
HOMEPAGE = "https://github.com/onnxruntime/onnxruntime-qnn"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=89126ed2f39aa3ae6e826e484e3f6f3c"

DEPENDS = " \
    flatbuffers-tflite \
    microsoft-gsl \
    nlohmann-json \
    onnx \
    protobuf \
    protobuf-native \
    qairt-sdk \
"

SRC_URI = "git://github.com/onnxruntime/onnxruntime-qnn.git;protocol=https;nobranch=1;name=ort-qnn \
    git://github.com/dcleblanc/SafeInt.git;protocol=https;nobranch=1;name=safeint;tag=3.0.28;destsuffix=safeint \
    https://github.com/microsoft/onnxruntime/releases/download/v1.26.0/onnxruntime-linux-aarch64-1.26.0.tgz;name=ort_prebuilt;subdir=ort_prebuilt \
"

SRCREV_FORMAT = "ort-qnn_safeint"
SRCREV_ort-qnn = "7005d98369fff0edaeae74760d6ebc3131dc1c26"
SRCREV_safeint = "4cafc9196c4da9c817992b20f5253ef967685bf8"

SRC_URI[ort_prebuilt.sha256sum] = "34ff1c2d0f12e2cf3d33a0c5f82e39792e1d581fbd6968fd7c30d173654be01a"

# Since qairt-sdk is installed only on ARMv8 (aarch64) machines and QNN EP uses
# qairt sdk for hw acceleration. Therefore, builds for other architectures are
# excluded for now.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"

# Fix buildpaths QA issue: remap TMPDIR references in both debug info and
# string literals embedded in the compiled libraries.
CFLAGS:append   = " -ffile-prefix-map=${WORKDIR}=. -ffile-prefix-map=${S}=. -ffile-prefix-map=${B}=."
CXXFLAGS:append = " -ffile-prefix-map=${WORKDIR}=. -ffile-prefix-map=${S}=. -ffile-prefix-map=${B}=."

inherit cmake

OECMAKE_SOURCEPATH = "${S}/cmake"

EXTRA_OECMAKE = " \
    -DCMAKE_BUILD_TYPE=RelWithDebInfo \
    -DCMAKE_FIND_ROOT_PATH=${STAGING_DIR_TARGET} \
    -DFETCHCONTENT_FULLY_DISCONNECTED=ON \
    -DFETCHCONTENT_SOURCE_DIR_SAFEINT=${WORKDIR}/sources/safeint \
    -DONNX_CUSTOM_PROTOC_EXECUTABLE=${STAGING_BINDIR_NATIVE}/protoc \
    -Donnxruntime_BUILD_SHARED_LIB=ON \
    -Donnxruntime_BUILD_UNIT_TESTS=OFF \
    -Donnxruntime_DISABLE_RTTI=OFF \
    -Donnxruntime_ORT_HOME=${WORKDIR}/sources/ort_prebuilt/onnxruntime-linux-aarch64-1.26.0 \
    -Donnxruntime_USE_QNN=ON \
    -Donnxruntime_QNN_HOME=${RECIPE_SYSROOT}/usr \
    -Donnxruntime_RUN_ONNX_TESTS=OFF \
"

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
# stub. Clean SOLIBSDEV and assign the file to the main package.
FILES_SOLIBSDEV = ""
FILES:${PN} += "${libdir}/libonnxruntime_providers_qnn.so"
