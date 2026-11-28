+++
title = "Boot firmware"
paths = ["recipes-bsp/firmware-boot/", "recipes-bsp/partition/", "recipes-bsp/u-boot/", "recipes-bsp/lk/"]
symbols = ["QCOM_BOOT_FIRMWARE", "QCOM_XBL_CONFIG", "QCOM_UEFI_DTB", "QCOM_CDT_FILE"]
+++

Review notes for boot firmware, partition data and boot loaders. These
changes can build cleanly and leave a board that does not boot or cannot be
flashed.

- CI boots only the machines that have a test job. For every other machine
  the change affects, look for boot evidence in the pull request and ask
  for it when there is none.
- Compare the old and new firmware archives by file hash, not by file name.
  A renamed file changes which image is flashed even when the recipe is
  untouched.
- Check that every file the partition data and the flashing step refer to
  exists in the new archive under the same name. `image_types_qcom` skips a
  missing file without failing the build.
- Compare image sizes with the partition sizes. An image that no longer
  fits fails when flashing, not when building.
- A firmware recipe is usually shared. Check each machine that sets it in
  `QCOM_BOOT_FIRMWARE`, not only the one in the title.
- For hypervisor selection, follow the whole chain: `QCOM_XBL_CONFIG` and
  `QCOM_UEFI_DTB` are chosen from `MACHINE_FEATURES` in
  `conf/machine/include/qcom-common.inc`, and must agree with the device
  tree the kernel is given. Check a build without a distro layer too, since
  a setting keyed on distro features does not apply there.
- When a version is bumped, read the version strings inside the images
  rather than trusting the archive name.
