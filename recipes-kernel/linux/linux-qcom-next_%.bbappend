# Temporary override for Nord SoC bring-up.
# Nord kernel support is available in the 'qcom-next' branch with
# tag: qcom-next-7.3-rc4-20261007, but we can't move to this tag
# globally yet due to test failures on other SoCs.
# To work around this, only use this tag for Nord. Drop this file once
# all SoCs are functional against a single 'qcom-next' tag.

# tag: qcom-next-7.3-rc4-20261007
LINUX_VERSION:nord = "7.2+7.3-rc4+nord"
SRCREV:nord = "ba86f67908354f41f269ca1d64b22e4624766dcf"

