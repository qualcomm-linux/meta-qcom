FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI:append:qcom = " file://0001-tests-amdgpu-amd_basic-use-a-portable-spin-wait-CPU-.patch"
