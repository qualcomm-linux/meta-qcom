inherit cmake pkgconfig

SUMMARY = "TensorFlow Lite C++ Library"
DESCRIPTION = "TensorFlow Lite C++ Library for embedded systems"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=4158a261ca7f2525513e31ba9c50ae98"

DEPENDS = " \
    protobuf-native \
    protobuf \
    flatbuffers-native \
    flatbuffers \
    util-linux-native \
    patchelf-native \
    jpeg \
    libeigen \
"

SRCREV = "076bc74dd6d4ec1bae0791e22ffb318723c66d38"
BRANCH = "r${@'.'.join(d.getVar('PV').split('.')[0:2])}"

SRC_URI = "git://github.com/tensorflow/tensorflow.git;branch=${BRANCH};protocol=https \
           file://0001-fix-gpu-adjust-work-group-size-and-remove-Adreno-spe.patch \
           file://0002-softmax1x1-take-reported-max-threads-into-account.patch \
           file://0003-work_group_picking-max_z_size-cannot-exceed-max-wg-s.patch \
           file://0004-cmake-lite-tools-benchmark-require-protobuf-through-.patch \
           file://0005-cmake-lite-examples-label_image-fix-protobuf-library.patch \
           file://0006-feat-tflite-Add-dynamic-OpenCL-library-loading-suppo.patch \
           file://0007-feat-tflite-Improve-shared-library-linking-and-build.patch \
           file://0008-CMake-add-project-version-and-set-VERSION-SOVERSION-.patch \
           file://tensorflow-lite.pc.in \
          "

PATCHTOOL = "git"

PACKAGECONFIG ?= "gpu"
# OpenCL support
PACKAGECONFIG[gpu] = " -DTFLITE_ENABLE_GPU=ON, -DTFLITE_ENABLE_GPU=OFF, virtual/libopencl1 vulkan-headers,"

OECMAKE_SOURCEPATH = "${S}/tensorflow/lite/c"

# This should probably be under PACKAGECONFIG control
OECMAKE_TARGET_COMPILE += "\
    benchmark_model \
    label_image \
    "

EXTRA_OECMAKE += "\
    -DFETCHCONTENT_FULLY_DISCONNECTED=OFF \
    -DCMAKE_POLICY_VERSION_MINIMUM=3.5 \
    -DCMAKE_SYSTEM_NAME=Linux \
    -DTFLITE_HOST_TOOLS_DIR=${STAGING_BINDIR_NATIVE} \
    -DCMAKE_FIND_PACKAGE_PREFER_CONFIG=ON \
    -DProtobuf_PROTOC_EXECUTABLE=${STAGING_BINDIR_NATIVE}/protoc \
    -DTFLITE_ENABLE_XNNPACK=ON \
    -DTFLITE_ENABLE_NNAPI=OFF \
    -DTFLITE_ENABLE_RUY=ON \
    -DTFLITE_ENABLE_GPU=ON \
"

CXXFLAGS:append = " \
    -DTF_MAJOR_VERSION=2 \
    -DTF_MINOR_VERSION=20 \
    -DTF_PATCH_VERSION=0 \
    -DTF_VERSION_SUFFIX= \
"

COMPILER = "${@bb.utils.contains('TUNE_FEATURES', 'clang', 'clang', 'gcc', d)}"
python () {
    if d.getVar('COMPILER') == 'clang':
        d.appendVar('EXTRA_OECMAKE', ' -DXNNPACK_ENABLE_ARM_BF16=OFF')
}

# Tensorflow lacks a proper install method, so manually specify which headers we want
TFLITE_HEADERS = "tensorflow/lite tensorflow/core/public tensorflow/core/platform tensorflow/core/lib tensorflow/lite/examples/label_image"

do_install:append() {
    for HPATH in ${TFLITE_HEADERS};
    do
        install -d ${D}${includedir}/${HPATH}
        cd ${S}/${HPATH}
        cp --parents $(find . \( ! -name "*hexagon*" -name "*.h*" \)) ${D}${includedir}/${HPATH}
    done

    install -d ${D}${libdir}
    install -m 644 ${B}/libtensorflowlite_c.so ${D}${libdir}

    install -d ${D}${bindir}
    install -m 0755 ${B}/tensorflow-lite/examples/label_image/label_image ${D}${bindir}
    install -m 0755 ${B}/tensorflow-lite/tools/benchmark/benchmark_model ${D}${bindir}

    install -d ${D}${libdir}/pkgconfig
    install -m 0644 ${WORKDIR}/sources/tensorflow-lite.pc.in ${D}${libdir}/pkgconfig/tensorflow-lite.pc
    sed -i 's:@version@:${PV}:g
        s:@libdir@:${libdir}:g
        s:@includedir@:${includedir}:g' ${D}${libdir}/pkgconfig/tensorflow-lite.pc

}

FILES:${PN} += "${libdir} ${bindir}"
INSANE_SKIP:${PN} += "dev-so \
                     "
SOLIBS = ".so*"
FILES_SOLIBSDEV = ""

PACKAGE_BEFORE_PN += "libtensorflow-c label-image"

FILES:libtensorflow-c = "${libdir}/libtensorflowlite_c${SOLIBS}"
FILES:label-image = "${bindir}/label_image"

COMPATIBLE_HOST:arm = "null"
COMPATIBLE_HOST:x86 = "null"
