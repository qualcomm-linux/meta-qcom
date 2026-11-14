+++
title = "Machines"
paths = ["conf/machine/", "ci/"]
symbols = ["MACHINE_FEATURES", "DEFAULTTUNE", "MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS"]
+++

Review notes for a new or changed machine configuration.

- For a board on an SoC family the layer already supports, ask first
  whether a new machine is needed. A board that differs only by its device
  tree may be an extra FIT configuration of an existing machine.
- A new machine needs all of these to be usable: the machine file, its
  `ci/<machine>.yml`, an entry in the build matrix, and the companion
  changes in the distro and board layers that build on this one. A board
  flashed through this layer also needs a boot firmware recipe and
  partition data; for any other board, the description says how it boots.
- `MACHINE=<machine>` alone must produce a working image. A file under
  `ci/` selects what to build; it must not hold values the board needs.
- Values that describe the board belong in the machine file, not in a
  recipe guarded by a machine override.
- Check what the machine inherits from the common includes. Image types,
  boot settings and features meant for other boards apply unless the
  machine or its SoC include overrides them.
- When an SoC include sets `DEFAULTTUNE`, check which package architecture
  the tune maps to. Two machines that share a package architecture with
  different tunes give shared recipes two signatures, which
  `yocto-check-layer` reports in `test_machine_signatures`.
- `MACHINE_FEATURES` is extended with `+=` after the common include. A
  plain assignment drops the defaults.
- Check that the device tree exists in every kernel the machine can be
  built with, including the release kernel, at the revision the recipe
  pins.
- When the machine recommends kernel modules, compare them with the
  drivers its device tree needs at that kernel revision.
- A new machine is not boot-tested until the test plans define its device
  and the pinned revision includes it. Until then ask for a boot log.
