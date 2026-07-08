HOMEPAGE = "https://github.com/qualcomm-linux/qcom-ptool"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b0a8acd90d872086b279ead88af03369"

SRC_URI = " \
           git://github.com/qualcomm-linux/qcom-ptool.git;branch=main;protocol=https \
	   file://0001-platforms-add-qcs615-talos-evk-emmc-partition-profile.patch \
	   "
SRCREV = "0ea6db50edc5d4edd3887b593bed080d71d63a08"
