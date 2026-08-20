# Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
#
# SPDX-License-Identifier: BSD-3-Clause-Clear
#
# Pre-seed VolatileVars.bin, an EBBR-style UEFI variable persistence file,
# into an EFI System Partition image so variables like VendorDtbOverlays
# are available from first boot instead of waiting for firmware-side
# generation.
#

# Empty by default; set to the desired overlay list for machines that need
# VolatileVars.bin pre-seeded (see lib/qcom/efi_volatile_vars.py).
QCOM_VENDOR_DTB_OVERLAYS ?= ""

python do_qcom_vendor_dtb_overlays() {
    dtboverlays = d.getVar('QCOM_VENDOR_DTB_OVERLAYS') or ""
    if not dtboverlays:
        return

    import os
    from qcom.efi_volatile_vars import build_vendor_dtb_volatile_vars

    data = build_vendor_dtb_volatile_vars(dtboverlays)

    out_dir = d.getVar('IMAGE_ROOTFS') + (d.getVar('ESPFOLDER') or '')
    os.makedirs(out_dir, exist_ok=True)
    with open(os.path.join(out_dir, 'VolatileVars.bin'), 'wb') as f:
        f.write(data)
}
addtask qcom_vendor_dtb_overlays after do_rootfs before do_image
do_qcom_vendor_dtb_overlays[vardeps] += "QCOM_VENDOR_DTB_OVERLAYS"
