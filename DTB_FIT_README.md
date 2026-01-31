This document explains the DTB-only FIT image flow used in meta-qcom, and 
the role of the variables `KERNEL_DEVICETREE` and `KERNEL_DEVICETREE_OVERLAYS` 
in generating FIT images.

The DTBs and overlays included in the FIT image are controlled primarily by 
the following variables:

- `KERNEL_DEVICETREE`
- `KERNEL_DEVICETREE_OVERLAYS`

Base DTBs must be added to the `KERNEL_DEVICETREE` variable, which is defined in the machine
configuration file (`meta-qcom/conf/machine/<target>.conf`) .

Device Tree Overlays (DTBOs) that need to be applied on top of a base DTB must be added to
the `KERNEL_DEVICETREE_OVERLAYS` variable, also defined in the machine configuration file.
These overlays are applied to the corresponding base DTBs listed in KERNEL_DEVICETREE.

For both standalone DTBs and DTB + DTBO combinations, the appropriate compatible strings must be
defined using the `FIT_DTB_COMPATIBLE` variable.
These compatible strings should be added in the following file:
`meta-qcom/conf/machine/include/fit-dtb-compatible.inc`