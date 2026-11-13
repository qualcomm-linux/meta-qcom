# SPDX-License-Identifier: GPL-2.0-only
# Copyright (c) 2024-2025, Qualcomm Innovation Center, Inc. All rights reserved.

SUMMARY = "Qualcomm L3 cache partition kernel module"
DESCRIPTION = "Out-of-tree Linux kernel module for software-defined L3 cache \
partitioning on QCS8300 and QCS9100. Programs CLUSTERPARTCR_EL1 and \
CLUSTER_THREAD_SID_EL1 via a ConfigFS interface at \
/sys/kernel/config/cache_partition/. Includes a cpu_pm notifier to restore \
register state after cluster power-gate, so partition assignments survive \
CPU idle transitions."

HOMEPAGE = "https://github.com/qualcomm-linux/l3-cache-partitioning"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b70c0a5153a7a19d562b59113294c9eb"

inherit module

SRC_URI = "git://github.com/qualcomm-linux/l3-cache-partitioning.git;protocol=https;branch=main"
SRCREV = "5a5314dbb4890327dc892746461f53c69e712e12"

PV = "1.0+git"

COMPATIBLE_MACHINE = "qcs8300|qcs9100"

KERNEL_MODULE_AUTOLOAD += "qcom-cache-partition"
RPROVIDES:${PN} += "kernel-module-qcom-cache-partition"
PACKAGE_ARCH = "${MACHINE_ARCH}"
