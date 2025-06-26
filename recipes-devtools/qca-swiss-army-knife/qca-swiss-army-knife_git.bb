SUMMARY = "A set of utilities to help QCA driver development."
HOMEPAGE = "https://github.com/qca/qca-swiss-army-knife"
SECTION = "devel"

LICENSE = "ISC"
LIC_FILES_CHKSUM = "file://LICENSE;md5=884c3f3a874b2a0cfa283c7db0e5d604"

SRCREV = "583eed7e66c661fe240189dd21c8f1eeb666c576"
SRC_URI = " \
	git://github.com/qca/${BPN}.git;branch=master;protocol=https \
	file://ath10k-generate-board-2_json.sh \
	file://ath10k-generate-pci-board-2_json.sh \
	file://ath11k-generate-pci-board-2_json.sh \
	file://ath11k-generate-ahb-board-2_json.sh \
"

PV = "0.0+"

do_install () {
	install -d ${D}/${bindir}
	install -m 0755 tools/scripts/*/* ${D}/${bindir}
	install -m 0755 ${UNPACKDIR}/ath10k-generate-board-2_json.sh ${D}/${bindir}
	install -m 0755 ${UNPACKDIR}/ath10k-generate-pci-board-2_json.sh ${D}/${bindir}
	install -m 0755 ${UNPACKDIR}/ath11k-generate-pci-board-2_json.sh ${D}/${bindir}
	install -m 0755 ${UNPACKDIR}/ath11k-generate-ahb-board-2_json.sh ${D}/${bindir}
}

BBCLASSEXTEND = "native nativesdk"

RDEPENDS:${PN} += "perl python3-core"
