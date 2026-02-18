PLATFORM = "lemans"
PBT_BUILD_DATE = "260211"

require common.inc

SRC_URI[camxlib.sha256sum]     = "5d5c907a7aecfaa2658ed79a3f3c426bcde5b24fed5e8294cef14d9570337e05"
SRC_URI[camx.sha256sum]        = "f871cca822e76e9b39bec65726038af0a45384c83404ffe6183cf41d68ed8c58"
SRC_URI[chicdk.sha256sum]      = "a7eb5b2161ccfc091fac513f40966a3ca13cb952eb206c08035607a7c1a2035f"
SRC_URI[camxcommon.sha256sum]  = "2a78ffe1a6d475d77640d0e70ed730e6a88ea19337cb8e31f6dc9c7e8083385a"

do_install:append() {
    install -d ${D}${sysconfdir}/camera/test/NHX/

    cp -r ${S}/etc/camera/test/NHX/*.json ${D}${sysconfdir}/camera/test/NHX/
}

RPROVIDES:${PN} = "camxlib-monaco"
PACKAGE_BEFORE_PN += "camx-nhx"

FILES:camx-nhx = "\
    ${bindir}/nhx.sh \
    ${sysconfdir}/camera/test/NHX/ \
"
