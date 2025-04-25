require conf/qcom-tip.conf

def get_var(d, var):
    name = var % d.getVar('QCOM_FW_SOC').upper()
    return d.getVar(name)

PV = "${@get_var(d, 'QCOM_%s_PV')}"
SRC_URI[bootbinaries.sha256sum] = "${@get_var(d, 'QCOM_%s_BOOTBINARIES_SUM')}"
