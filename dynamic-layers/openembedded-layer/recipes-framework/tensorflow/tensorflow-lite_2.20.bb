DESCRIPTION = "TensorFlow C/C++ Libraries"
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
inherit cmake pkgconfig

PV = "2.20"
# v2.20.0 
SRCREV = "72fbba3d20f4616d7312b5e2b7f79daf6e82f2fa"
SRC_URI = "git://github.com/tensorflow/tensorflow.git;branch=r2.20;protocol=https \
           file://0001-FIXES.patch;patch=1 \
           file://0002-softmax1x1-take-reported-max-threads-into-account.patch;patch=1 \
           file://0003-OpenCL-wrapper-try-loading-libOpenCL.so.1-if-libOpen.patch;patch=1 \
           file://0004-work_group_picking-max_z_size-cannot-exceed-max-wg-s.patch;patch=1 \
           file://0005-cmake-lite-tools-benchmark-require-protobug-through-.patch;patch=1 \
           file://0006-cmake-lite-examples-label_image-fix-protobuf-library.patch;patch=1 \
           file://0007-label_image.lite-tweak-default-model-location.patch;patch=1 \
           file://0008-tensorflow-lite-Major-version-dlopen-for-OpenCL-libs.patch;patch=1 \
          "
PATCHTOOL = "git"

SRC_URI += "https://storage.googleapis.com/download.tensorflow.org/models/inception_v3_2016_08_28_frozen.pb.tar.gz;name=model-inv3"
SRC_URI[model-inv3.md5sum] = "a904ddf15593d03c7dd786d552e22d73"
SRC_URI[model-inv3.sha256sum] = "7045b72a954af4dce36346f478610acdccbf149168fa25c78e54e32f0c723d6d"

SRC_URI += "https://storage.googleapis.com/download.tensorflow.org/models/tflite/mobilenet_v1_1.0_224_quant_and_labels.zip;name=model-mobv1"
SRC_URI[model-mobv1.md5sum] = "38ac0c626947875bd311ef96c8baab62"
SRC_URI[model-mobv1.sha256sum] = "2f8054076cf655e1a73778a49bd8fd0306d32b290b7e576dda9574f00f186c0f"

PACKAGECONFIG ?= "gpu"
# OpenCL support
PACKAGECONFIG[gpu] = " -DTFLITE_ENABLE_GPU=ON, -DTFLITE_ENABLE_GPU=OFF, virtual/libopencl1 vulkan-headers,"

OECMAKE_SOURCEPATH = "${S}/tensorflow/lite/c"

# This should probably be under PACKAGECONFIG control
OECMAKE_TARGET_COMPILE += "\
    benchmark_model \
    label_image \
    "
# NNAPI, XNNPACK, RUY and friends should also be a PACKAGE_CONFIG

EXTRA_OECMAKE += "\
    -DCMAKE_POLICY_VERSION_MINIMUM=3.5 \
    -DCMAKE_SYSTEM_NAME=Linux \
    -DTFLITE_HOST_TOOLS_DIR=${STAGING_BINDIR_NATIVE} \
    -DCMAKE_FIND_PACKAGE_PREFER_CONFIG=ON \
    -DProtobuf_PROTOC_EXECUTABLE=${STAGING_BINDIR_NATIVE}/protoc \
    -DTFLITE_ENABLE_XNNPACK=ON \
    -DTFLITE_ENABLE_NNAPI=OFF \
    -DTFLITE_ENABLE_RUY=ON \
"

# FIXME: parse PV from TF_XXXX_VERSION
# Or fix the header/include it
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

TF_TARGET_EXTRA ??= ""

do_configure[network] = "1"
do_configure:prepend() {
    # OE runs cmake in off-line mode by default, but we need it to fetch dependencies that aren't in OE, like farmhash
    # Run cmake manually to have it fetch stuff from the interwebs, then remove the generate files
    mkdir -p ${B} ; cd ${B}
    cmake \
	-DCMAKE_POLICY_VERSION_MINIMUM=3.5 \
	-DCMAKE_FIND_PACKAGE_PREFER_CONFIG=ON \
	-DTFLITE_ENABLE_GPU=ON \
	${S}/tensorflow/lite/c
    find ${WORKDIR}/build -name Makefile -exec rm -r {} \;
    find ${WORKDIR}/build -name cmake_install.cmake -exec rm -r {} \;
    find ${WORKDIR}/build -name CMakeCache.txt -exec rm -r {} \;
    find ${WORKDIR}/build -name CMakeFiles -exec rm -rf {} +
    # The regular do_configure() method will run after things
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

    install -d ${D}${datadir}/label_image
    install -m 644 ${UNPACKDIR}/imagenet_slim_labels.txt ${D}${datadir}/label_image
    install -m 644 ${UNPACKDIR}/inception_v3_2016_08_28_frozen.pb ${D}${datadir}/label_image
    install -m 644 ${S}/tensorflow/examples/label_image/data/grace_hopper.jpg ${D}${datadir}/label_image

    install -m 644 ${UNPACKDIR}/labels_mobilenet_quant_v1_224.txt ${D}${datadir}/label_image
    install -m 644 ${UNPACKDIR}/mobilenet_v1_1.0_224_quant.tflite  ${D}${datadir}/label_image
    install -m 644 ${S}/tensorflow/lite/examples/label_image/testdata/grace_hopper.bmp ${D}${datadir}/label_image
}

FILES:${PN} += "${libdir} ${bindir} ${datadir}/*"
INSANE_SKIP:${PN} += "dev-so \
                     "

SOLIBS = ".so"
FILES_SOLIBSDEV = ""

PACKAGE_BEFORE_PN += "libtensorflow-c label-image"

FILES:libtensorflow-c = "${libdir}/libtensorflowlite_c${SOLIBS}"
FILES:label-image = "${bindir}/label_image ${datadir}/label_image"

inherit siteinfo
python __anonymous() {
    if d.getVar("SITEINFO_ENDIANNESS") == 'be':
        msg =  "\nIt failed to use pre-build model to do predict/inference on big-endian platform"
        msg += "\n(such as qemumips), since upstream does not support big-endian very well."
        msg += "\nDetails: https://github.com/tensorflow/tensorflow/issues/16364"
        bb.warn(msg)
}

COMPATIBLE_HOST:arm = "null"
COMPATIBLE_HOST:x86 = "null"
