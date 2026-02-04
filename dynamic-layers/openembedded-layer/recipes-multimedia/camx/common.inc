SUMMARY = "CamX camera module with core interfaces, image processing libraries"
DESCRIPTION = "This recipe introduces the Qualcomm CamX camera module which creates \
three components. \
camxlib: Includes common utilities, image processing algorithms \
and hardware support libraries extending CamX functionality for platform-specific \
enhancements. \
camx: Core CamX engine that manages camera pipelines, mediates \
between camera clients and hardware, and exposes structured interfaces to \
higher-level frameworks. \
chicdk: Camera hardware interface development kit delivering a configurable \
mechanism for use case selection and camera pipeline topology creation. \
Includes configurable sensor and tuning binaries, which are essential for enabling \
full camera functionality."

DEPENDS += "glib-2.0 fastrpc protobuf-camx libxml2"

# This package is currently only used and tested on ARMv8 (aarch64) machines.
# Therefore, builds for other architectures are not necessary and are explicitly excluded.
COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:aarch64 = "(.*)"

# Disable configure and compile steps since this recipe uses prebuilt binaries.
do_configure[noexec] = "1"
do_compile[noexec] = "1"
