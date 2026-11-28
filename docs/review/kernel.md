+++
title = "Kernel recipes"
paths = ["recipes-kernel/linux/"]
symbols = ["KERNEL_DEVICETREE", "KBUILD_DEFCONFIG", "LINUX_VERSION"]
+++

Review notes for the kernel recipes, their configuration fragments and the
patches they carry.

- A kernel fix belongs in the kernel tree the recipe builds from. A patch
  carried in this layer needs a reason in the commit message and an
  `Upstream-Status` that says where it stands, with a link when it was
  submitted.
- The `linux-qcom*` recipes live in this layer: change the recipe itself,
  not a bbappend to it. The `linux-yocto` kernels come from OE-Core and
  are adjusted through the bbappends here.
- The layer provides several kernels. For a change made to one recipe, say
  whether the others need it too, and check that a machine or feature still
  works, or is deliberately excluded, with each kernel it can be built
  with.
- Check that each carried patch applies at the revision the recipe pins,
  and read what it does as you would read any kernel patch: it ships to
  every machine that builds this kernel unless the recipe limits it.
- For a configuration fragment, check that every symbol exists at the
  pinned revision and that the fragment is applied after the ones it means
  to override.
- After a kernel bump, check that the device trees the machines list still
  exist and that the modules the machines recommend kept their names.
- Where a board has a size limit for the kernel image, ask for the size of
  the image the change produces.
