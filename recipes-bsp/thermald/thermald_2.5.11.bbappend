# Patches required to make thermalD platform‑agnostic were not included in the 2.5.11 release, so we apply them separately.

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += " \
    file://0001-Refactor-Intel-specific-logic-into-separate-files.patch \
    file://0002-Invoke-parser_init-before-platform_match.patch \
	file://0003-Add-ARM-backend-and-enable-ARM-platform-detection.patch \
"
