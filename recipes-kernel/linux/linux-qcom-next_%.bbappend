# Temporary override for Nord SoC bring-up. Nord kernel support
# isn't in 'qcom-next' yet, so build from the 'staging/nord' branch
# instead. Drop this file once Nord support merges into 'qcom-next'
# to fall back to the recipe's defaults.

# tag: nord-staging-qcom-next-7.2-rc3-20260902
LINUX_VERSION:nord = "7.1+7.2-rc3-20260902+nord"
SRCBRANCH:nord = "branch=staging/nord"
SRCREV:nord = "4ee8d36976563786de92b67e51005f858ed32eb5"
