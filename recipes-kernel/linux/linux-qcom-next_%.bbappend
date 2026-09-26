# Temporary override for Nord SoC bring-up.
# Nord kernel support is available in the 'qcom-next' branch with
# tag: qcom-next-7.3-rc2-20260921, but we can't move to this tag
# globally yet due to test failures on other SoCs.
# To work around this, only use this tag for Nord. Drop this file once
# all SoCs are functional against a single 'qcom-next' tag.

# tag: qcom-next-7.3-rc2-20260921
LINUX_VERSION:nord = "7.2+7.3-rc2+nord"
SRCREV:nord = "8f69c5810aa30bfa58bf58256fed38e666b231c2"

# DTB files specified in the nord compatibles only exist in
# the 'qcom-next' tag used by Nord, not in the default SRCREV that
# yocto-check-layer builds against.  Backport the two board DTS 
# files, so the default kernel source tree has them too.
SRC_URI:append = " \
    file://0001-FROMLIST-arm64-dts-qcom-Add-initial-support-for-Nord.patch \
    file://0002-FROMLIST-arm64-dts-qcom-Add-device-tree-for-Nord-RRD.patch \
"
# Drop the above backports for Nord itself as Nord SRCREV/tag above already
# carries these two commits, so re-applying them would fail.
SRC_URI:remove:nord = "file://0001-FROMLIST-arm64-dts-qcom-Add-initial-support-for-Nord.patch"
SRC_URI:remove:nord = "file://0002-FROMLIST-arm64-dts-qcom-Add-device-tree-for-Nord-RRD.patch"
