# Qualcomm DTB-only FIT Image

This document explains the DTB-only FIT image flow used in meta-qcom, and 
the role of the variables `KERNEL_DEVICETREE` and `KERNEL_DEVICETREE_OVERLAYS` 
in generating FIT images.
The FIT image generation described here is intended for QCOM platforms 
that use **DTB-only FIT images** (no kernel image included), following the 
Qualcomm DTB metadata specification.

## Overview

A DTB-only FIT image bundles one or more Device Tree Blobs (DTBs), optional 
Device Tree Overlays (DTBOs), and Qualcomm DTB metadata into a single FIT image. 
At boot time, the bootloader selects the appropriate DTB (and overlays, if applicable) 
based on the `compatible` strings (refer `meta-qcom/conf/machine/include/fit-dtb-compatible.inc`)
defined in the FIT configuration.

The DTBs and overlays included in the FIT image are controlled primarily by 
the following variables:

- `KERNEL_DEVICETREE`
- `KERNEL_DEVICETREE_OVERLAYS`

## KERNEL_DEVICETREE

### Purpose

`KERNEL_DEVICETREE` , defined in the `meta-qcom/conf/machine/<target>.conf`, specifies 
the **base Device Tree Blobs (DTBs)** that describe the hardware platforms supported by 
a given machine configuration.
Each DTB listed here represents a complete, standalone hardware description that can be 
selected by the bootloader.

### Example

```
KERNEL_DEVICETREE ?= " \
                      qcom/qcs6490-rb3gen2.dtb \
                      "
```

## KERNEL_DEVICETREE_OVERLAYS

### Purpose

`KERNEL_DEVICETREE_OVERLAYS`, defined in the `meta-qcom/conf/machine/<target>.conf`, specifies 
**Device Tree Overlays (DTBOs)** that can be applied on top of one or more base DTBs 
listed in `KERNEL_DEVICETREE`.
Overlays allow optional or variant hardware (such as camera, display) to be supported without 
duplicating entire base DTBs.

### Example

```
KERNEL_DEVICETREE_OVERLAYS ?= " \
                               qcom/qcs6490-rb3gen2-vision-mezzanine-camx.dtbo \
                               "
```