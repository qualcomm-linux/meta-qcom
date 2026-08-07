# This file specifies the necessary versions for the binary recipes.

VERSION_abseil-cpp ?= ""
PREFERRED_VERSION_abseil-cpp = "${VERSION_abseil-cpp}"
PREFERRED_VERSION_abseil-cpp-native = "${VERSION_abseil-cpp}"
PREFERRED_VERSION_nativesdk-abseil-cpp = "${VERSION_abseil-cpp}"

VERSION_protobuf ?= ""
PREFERRED_VERSION_protobuf = "${VERSION_protobuf}"
PREFERRED_VERSION_protobuf-native = "${VERSION_protobuf}"
PREFERRED_VERSION_nativesdk-protobuf = "${VERSION_protobuf}"

# FIXME: temporay until qcom distro is fixed
TOOLCHAIN_HOST_TASK:remove:pn-qcom-multimedia-proprietary-image = "nativesdk-protobuf-camx-compiler"
TOOLCHAIN_HOST_TASK:append:pn-qcom-multimedia-proprietary-image = " nativesdk-protobuf-compiler"
TOOLCHAIN_HOST_TASK:remove:pn-qcom-container-orchestration-image = "nativesdk-protobuf-camx-compiler"
TOOLCHAIN_HOST_TASK:append:pn-qcom-container-orchestration-image = " nativesdk-protobuf-compiler"
