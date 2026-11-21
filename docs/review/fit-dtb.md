+++
title = "FIT images and device trees"
paths = ["classes-recipe/dtb-fit-image.bbclass", "conf/machine/include/fit-dtb-compatible.inc", "conf/machine/include/fit-dtb-compatible-linux-qcom.inc"]
symbols = ["FIT_DTB_COMPATIBLE", "KERNEL_DEVICETREE"]
+++

Review notes for the multi-DTB FIT image and the mapping from compatible
strings to device trees. A wrong entry is invisible to the build and to CI
on most boards.

- FIT generation skips any entry whose device tree or overlay is not in
  `KERNEL_DEVICETREE`, without a warning. After a change, compare the list
  of configurations in the generated image with the one before it.
- For an entry with overlays, check that the base device tree and the
  overlays apply together and give the compatible string the entry is keyed
  on.
- Entries that need device trees only the `linux-qcom` kernels carry belong
  in `fit-dtb-compatible-linux-qcom.inc`, so a build with another kernel
  keeps working from the base file.
- Check the entry against every kernel recipe in the layer. A device tree
  that exists in one kernel and not another drops that configuration
  silently there.
- Run `oe-selftest` for `qcom_fitimage` when the class or the mapping
  changes.
- A configuration that is generated but never booted by a test job is
  unverified. Say so.
