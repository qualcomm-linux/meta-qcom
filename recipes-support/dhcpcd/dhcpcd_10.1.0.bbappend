# SECCOMP is very dependant on libc vs kernel,
# Disable seccomp to prevent dhcpcd break
EXTRA_OECONF:append = " --disable-seccomp"
