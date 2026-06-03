SUMMARY = "ONNX Runtime QNN Execution Provider Plugin"
DESCRIPTION = "Standalone ABI-stable plugin execution provider that brings Qualcomm \
hardware acceleration to ONNX Runtime via the Qualcomm AI Runtime SDK (QAIRT)."
HOMEPAGE = "https://github.com/onnxruntime/onnxruntime-qnn"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=89126ed2f39aa3ae6e826e484e3f6f3c"

DEPENDS = " \
    flatbuffers-tflite \
    onnx \
    onnxruntime \
    protobuf \
    protobuf-native \
    qairt-sdk \
"

SRC_URI = "git://github.com/onnxruntime/onnxruntime-qnn.git;protocol=https;nobranch=1;tag=v${PV};name=ort-qnn \
    git://github.com/dcleblanc/SafeInt.git;protocol=https;nobranch=1;name=safeint;tag=3.0.28;destsuffix=safeint \
    file://0001-cmake-Rename-pkg-config-output-to-libonnxruntime_pr.patch \
"

SRCREV_FORMAT = "ort-qnn_safeint"
SRCREV_ort-qnn = "215ea95bd6df9ab24ba48193a6554dce8490337d"
SRCREV_safeint = "4cafc9196c4da9c817992b20f5253ef967685bf8"

# Since qairt-sdk is installed only on ARMv8 (aarch64) machines and QNN EP uses
# qairt sdk for hw acceleration. Therefore, builds for other architectures are
# excluded for now.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"

# Fix buildpaths QA issue: remap TMPDIR references in both debug info and
# string literals embedded in the compiled libraries.
CFLAGS:append   = " -ffile-prefix-map=${WORKDIR}=."
CXXFLAGS:append = " -ffile-prefix-map=${WORKDIR}=."

# onnxruntime headers are installed under usr/include/onnxruntime/ in the
# sysroot, but the onnxruntime-qnn source includes them without that prefix
# Add the subdirectory explicitly.
CFLAGS:append   = " -I${STAGING_INCDIR}/onnxruntime"
CXXFLAGS:append = " -I${STAGING_INCDIR}/onnxruntime"

inherit cmake

OECMAKE_SOURCEPATH = "${S}/cmake"

EXTRA_OECMAKE = " \
    -DCMAKE_BUILD_TYPE=RelWithDebInfo \
    -DCMAKE_FIND_ROOT_PATH=${STAGING_DIR_TARGET} \
    -DFETCHCONTENT_FULLY_DISCONNECTED=ON \
    -DFETCHCONTENT_SOURCE_DIR_SAFEINT=${UNPACKDIR}/safeint \
    -DONNX_CUSTOM_PROTOC_EXECUTABLE=${STAGING_BINDIR_NATIVE}/protoc \
    -Donnxruntime_BUILD_SHARED_LIB=ON \
    -Donnxruntime_BUILD_UNIT_TESTS=OFF \
    -Donnxruntime_DISABLE_RTTI=OFF \
    -Donnxruntime_ORT_HOME=${RECIPE_SYSROOT}/usr \
    -Donnxruntime_USE_QNN=ON \
    -Donnxruntime_QNN_HOME=${RECIPE_SYSROOT}/usr \
"

# libonnxruntime_providers_qnn.so is a runtime plugin, not a development linker
# stub. Clean SOLIBSDEV and assign the file to the main package.
FILES_SOLIBSDEV = ""
FILES:${PN} += "${libdir}/libonnxruntime_providers_qnn.so"
