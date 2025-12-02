PACKAGECONFIG:append:qcom = " tests"

# Only on ARMv8 Qualcomm machines
PACKAGECONFIG:append:qcom:aarch64 = " fastcv"

#Ensures shlibdeps can resolve libfastcvopt.so and add RDEPENDS to avoid unresolvable shlib QA errors.
DEPENDS:append:qcom:aarch64 = " qcom-fastcv-binaries"

RDEPENDS:libopencv-core:append:qcom:aarch64 = " libfastcvopt1"
RDEPENDS:libopencv-fastcv:append:qcom:aarch64 = " libfastcvopt1"
RDEPENDS:libopencv-imgproc:append:qcom:aarch64 = " libfastcvopt1"
