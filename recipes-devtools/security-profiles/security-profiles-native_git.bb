SUMMARY = "Qualcomm Security Profiles"
DESCRIPTION = "Per-chipset Security Profile XML files consumed by Sectools"
HOMEPAGE = "https://github.com/qualcomm/security-profiles"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=2998c54c288b081076c9af987bdf4838"

SRC_URI = "git://github.com/qualcomm/security-profiles.git;protocol=https;branch=main"
SRCREV = "bb3e8c735d6b7fee25227395249f9ec674867464"

inherit native

# Plain XML data: nothing to build, no toolchain needed.
INHIBIT_DEFAULT_DEPS = "1"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

# Install the XML profiles into a well-known native datadir location
# so qcom-firmware-sign.bbclass can resolve them by filename via
# QCOM_FIRMWARE_SIGN_SECPROFILE.
do_install() {
    install -d "${D}${datadir}/qcom-security-profiles"
    install -m 0644 "${S}"/*_security_profile.xml \
        "${D}${datadir}/qcom-security-profiles/"
}
