# Temporary override for Nord SoC bring-up.
# Nord kernel support is available in the 'qcom-next' branch with
# tag: qcom-next-7.3-rc2-20260921, but we can't move to this tag
# globally yet due to test failures on other SoCs.
# To work around this, only use this tag for Nord. Drop this file once
# all SoCs are functional against a single 'qcom-next' tag.

# tag: qcom-next-7.3-rc2-20260921
LINUX_VERSION:nord = "7.2+7.3-rc2+nord"
SRCREV:nord = "8f69c5810aa30bfa58bf58256fed38e666b231c2"
