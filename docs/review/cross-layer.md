+++
title = "Cross-layer impact"
paths = ["classes/", "classes-recipe/", "conf/", "recipes-*/", "dynamic-layers/"]
symbols = ["BBFILES_DYNAMIC", "LAYERDEPENDS", "PREFERRED_PROVIDER", "PREFERRED_VERSION"]
+++

Review notes for changes that other layers, branches or open pull requests
depend on. This layer is the base of the distro, third-party and board
layers, and their CI does not run here.

- When a path, variable, class, package name or install location is
  renamed or moved, search this layer and the layers that build on it for
  users of the old one.
- A change that needs a companion pull request in another repository says
  so, with the merge order. Check that the companion exists and which one
  has to land first.
- When two open pull requests touch the same code, say which one needs a
  rebase after the other lands.
- Check a kernel-dependent change against every kernel recipe in the layer.
  A device tree or driver present in one may be missing in another.
- For a fix, decide whether it should be backported. A fix for something
  the release branch also has needs the backport label; a change that
  would break the release branch must not get it.
- A recipe moved between this layer and another needs its whole dependency
  set to move in the same series.
