+++
title = "Packaging and task inputs"
paths = ["recipes-*/", "classes/", "classes-recipe/", "dynamic-layers/", "conf/layer.conf"]
symbols = ["FILES", "RDEPENDS", "RRECOMMENDS", "PACKAGECONFIG", "PACKAGE_ARCH", "COMPATIBLE_MACHINE", "EXTRA_OECMAKE", "EXTRA_OEMESON"]
+++

Review notes for recipes and classes. Most of these are settled by a parse
or a single recipe build rather than by reading.

- Evaluate a changed override with `bitbake-getvar` for one machine it
  should affect and one it should not. A `:remove` with an extra override,
  or `=` where `?=` was meant, often does nothing or overrides the user's
  configuration.
- Check that a new build option exists in the upstream source at the
  pinned revision. An unknown option is usually accepted and ignored.
- Check that a new dependency changes something. A package added for a
  feature the source does not use is dead weight.
- Compare installed files with `FILES`. A file dropped from the install
  step and left in `FILES`, or a runtime dependency put on a package that
  ends up empty, passes the build.
- A recipe that is not machine-specific must not pick up machine-specific
  values. Set `PACKAGE_ARCH` before inheriting `packagegroup`, and compare
  task signatures across two machines with `bitbake -S none` when in doubt.
- A file a task reads must be among that task's inputs, and no absolute
  build path may reach a task signature.
- After new Python in a recipe or class, force a parse and compare the
  warnings with the base.
- This layer depends on OE-Core only. Anything that needs another layer
  goes under `dynamic-layers/`.
- A recipe for one architecture or SoC family sets `COMPATIBLE_MACHINE`.
- Runtime state under `/var` is created at boot, not packaged.
- For a service, prefer adapting the unit or adding a drop-in over wrapping
  the command in a shell script.
