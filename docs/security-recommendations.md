# Security recommendation for Production Build

## 1. Debugfs

By default debugfs is enabled to make it user friendly for debugging. Commercial products are not advised to have this enabled.
We recommend deployers to disable debugfs as part of production deployment customization.

Disable via kernel configuration:

DebugFS is controlled by the kernel configuration option `CONFIG_DEBUG_FS`.

For commercial/production builds, In meta-qcom based builds, this can be disabled by setting the following option in the kernel configuration used by the BSP
(for example, in `meta-qcom/recipes-kernel/linux/linux-qcom-next/configs/bsp-additions.cfg`):

```text
# CONFIG_DEBUG_FS is not set
```

## 2. Kernel hardening flags

Kernel security restrictions and self-protection (“kernel hardening”) can be enabled via distribution-specific configuration.

For commercial/production builds, enabling available kernel hardening features is recommended to enhance system security and reduce the attack surface.

Where supported by the selected distribution, the `hardened` feature can be appended to `DISTRO_FEATURES`.

This can be added to the distribution configuration (for example, `meta-qcom-distro/conf/distro/qcom-distro.conf` when using meta-qcom-distro).

For example:

```text
DISTRO_FEATURES:append = " hardened"
```

## 3. Logging and kernel information exposure restrictions

For commercial/production builds, it is recommended to restrict kernel logging and kernel information exposure to reduce the attack surface and prevent leakage of sensitive kernel details.

### 3.1 Kernel log rate limiting (printk)

Rate limiting for kernel log output (for example via `/dev/kmsg` or the serial console) can be configured using kernel command-line parameters.

For production builds, enabling printk rate limiting is recommended to avoid excessive kernel log exposure.

In meta-qcom based Yocto builds, this can be done by appending the parameter to the kernel command line
via the distribution configuration (for example `meta-qcom-distro/conf/distro/qcom-distro.conf`) when using meta-qcom-distro.

For example:

```text
KERNEL_CMDLINE_EXTRA:append = " printk.devkmsg=ratelimit "
```

### 3.2 Kernel pointer restriction (kptr\_restrict) and Kernel log access restriction (dmesg\_restrict)

Kernel pointer exposure can be restricted using the `kptr_restrict` sysctl.
Access to kernel logs via `dmesg` can be restricted using `dmesg_restrict` so that only privileged users are allowed to read kernel messages.
For commercial/production builds, it is recommended to set these to restrict visibility to privileged users only.

In meta-qcom based Yocto builds, this can be implemented by adding a sysctl configuration file via a bbappend to the `procps` recipe.

Example implementation:

1. Create a bbappend file:

   `meta-qcom/recipes-core/procps/procps_%.bbappend`

   ```bitbake
   FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

   SRC_URI += "file://99-security-hardening.conf"

   do_install:append() {
      install -d ${D}${sysconfdir}/sysctl.d
      install -m 0644 ${UNPACKDIR}/99-security-hardening.conf \
         ${D}${sysconfdir}/sysctl.d/
   }
   ```

2. Add sysctl configuration file:

   `meta-qcom/recipes-core/procps/procps/99-security-hardening.conf`

   Contents:

   ```text
   kernel.kptr_restrict = 2
   kernel.dmesg_restrict = 1
   ```

3. Build the image:

   ```bash
   bitbake <image-name>
   ```

4. verify in the target

   ```bash
   sysctl kernel.kptr_restrict
   sysctl kernel.dmesg_restrict   
   ```

## 4. Userspace hardening

OpenEmbedded provides a standard set of userspace security hardening compiler and linker flags via **security\_flags.inc**
(<https://git.openembedded.org/openembedded-core/tree/meta/conf/distro/include/security_flags.inc>).

These flags are already part of the build and must remain enabled for production images to ensure adequate userspace exploit mitigations.
Any deviation from these defaults should be avoided or explicitly justified.

## 5. ADB security recommendations for production

### ADB / adbd restrictions

By default `adbd` runs as root on yocto, which means `adb shell` provides root access.
Commercial/production products should not allow root access via ADB because it weakens overall device security.

### Recommendation

In production builds, `adbd` must not run as root.

Limit root ADB access to development/debug use only.

**Deployers are expected to disable root ADB access as part of production deployment customization before commercialization.**
