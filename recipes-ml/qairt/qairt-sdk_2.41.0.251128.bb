SUMMARY = "Qualcomm AI Runtime SDK"
DESCRIPTION = " \
Qualcomm's AI Runtime SDK provides the tools, runtimes, and model‑execution engines needed \
to run Machine Learning models efficiently on Qualcomm devices. \
It integrates the components to enable efficient inference on CPU, GPU, and NPU/HTP accelerators, \
while also supporting the development of ML applications. \
"
HOMEPAGE = "https://docs.qualcomm.com/doc/80-63442-10/topic/general_overview.html"

LICENSE = "qcom-ai-stack"
LIC_FILES_CHKSUM = "file://LICENSE.pdf;md5=878b885995f453e328edbcd5a1302306"
NO_GENERIC_LICENSE[qcom-ai-stack] = "LICENSE.pdf"

SRC_URI = "https://softwarecenter.qualcomm.com/api/download/software/sdks/Qualcomm_AI_Runtime_Community/All/${PV}/v${PV}.zip"
SRC_URI[sha256sum] = "c6306f7d70457e4d8736416bb406d8670f5186fb648dd6808db24e9b2176c28d"

S = "${UNPACKDIR}/qairt/${PV}"


# The SDK ships multiple toolchain-specific lib directories with names
# like "aarch64-oe-linux-gcc8.2", "aarch64-oe-linux-gcc9.3", etc.
# This helper picks the directory whose GCC major version best matches the
# build compiler, falling back to the nearest lower available version.
def platform_dir(d):
    sdk_lib_dir = d.getVar("S", True) + "/lib/"
    if os.path.exists(sdk_lib_dir) and os.path.isdir(sdk_lib_dir):
        dir_prefix = "aarch64-oe-linux-gcc"
        gccversion = d.getVar("GCCVERSION", True).strip('%').split('.')[0]
        gccversion = int(gccversion)
        MIN_GCC_VERSION = 8

        for version in range (gccversion, MIN_GCC_VERSION - 1, -1):
            version = str(version)
            pf_dir = dir_prefix + version
            for folder in os.listdir(sdk_lib_dir):
                if folder.startswith(pf_dir):
                    pf_dir += "*"
                    return pf_dir

PLATFORM_DIR = "${@platform_dir(d)}"

do_compile[noexec] = "1"

# We currently install and test it only on ARMv8 (aarch64) machines.
# PLATFORM_DIR resolution also assumes aarch64.
# Therefore, builds for other architectures are excluded for now.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"

do_install() {
    install -d ${D}${includedir}
    install -d ${D}${libdir}
    install -d ${D}${datadir}/qcom/qcm6490/Thundercomm/RB3gen2/dsp/cdsp
    install -d ${D}${datadir}/qcom/sa8775p/Qualcomm/SA8775P-RIDE/dsp/cdsp
    install -d ${D}${datadir}/qcom/qcs8300/Qualcomm/QCS8300-RIDE/dsp/cdsp
    install -d ${D}${bindir}

    cp -r ${S}/include/* ${D}${includedir}
    cp -r ${S}/lib/${PLATFORM_DIR}/* ${D}${libdir}

    # These installation paths for the Hexagon libraries were decided based on
    # the recommendations from FastRPC team and taking FastCV as a reference.
    # They may change later, so keep the PACKAGES entries generic.
    cp -r ${S}/lib/hexagon-v68/unsigned/* ${D}${datadir}/qcom/qcm6490/Thundercomm/RB3gen2/dsp/cdsp
    cp -r ${S}/lib/hexagon-v73/unsigned/* ${D}${datadir}/qcom/sa8775p/Qualcomm/SA8775P-RIDE/dsp/cdsp
    cp -r ${S}/lib/hexagon-v75/unsigned/* ${D}${datadir}/qcom/qcs8300/Qualcomm/QCS8300-RIDE/dsp/cdsp

    cp -r ${S}/bin/${PLATFORM_DIR}/* ${D}${bindir}
}

# Skip file-rdeps due to prebuilt libhta_hexagon_runtime_snpe with mismatched SONAME.
INSANE_SKIP:${PN} += "file-rdeps"

# SDK ships already-stripped proprietary binaries
# which need not be re-striped or split into debug symbols.
# So, disable strip, debug split and mute 'already-stripped' warnings.
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INSANE_SKIP:${PN} += "already-stripped"

# The SDK ships unversioned .so files, so keep them in the main package
# and treat plain .so as the library (avoid -dev splitting).
SOLIBS = ".so"
FILES_SOLIBSDEV = ""

PACKAGES += "\
    ${PN}-hexagon-v68 \
    ${PN}-hexagon-v73 \
    ${PN}-hexagon-v75 \
"

FILES:${PN}-hexagon-v68 += "${datadir}/qcom/qcm6490/Thundercomm/RB3gen2/dsp/cdsp"
FILES:${PN}-hexagon-v73 += "${datadir}/qcom/sa8775p/Qualcomm/SA8775P-RIDE/dsp/cdsp"
FILES:${PN}-hexagon-v75 += "${datadir}/qcom/qcs8300/Qualcomm/QCS8300-RIDE/dsp/cdsp"

RDEPENDS:${PN} += "fastrpc"
RRECOMMENDS:${PN} += "${PN}-hexagon-v68 ${PN}-hexagon-v73 ${PN}-hexagon-v75"

# File based runtime depends are not applicable for Hexagon libraries.
SKIP_FILEDEPS:${PN}-hexagon-v68 = "1"
SKIP_FILEDEPS:${PN}-hexagon-v73 = "1"
SKIP_FILEDEPS:${PN}-hexagon-v75 = "1"

# Skip QA checks that don’t apply to prebuilt Hexagon DSP/HTP libraries.
INSANE_SKIP:${PN}-hexagon-v68 += "arch libdir ldflags file-rdeps"
INSANE_SKIP:${PN}-hexagon-v73 += "arch libdir ldflags file-rdeps"
INSANE_SKIP:${PN}-hexagon-v75 += "arch libdir ldflags file-rdeps"
