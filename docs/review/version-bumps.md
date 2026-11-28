+++
title = "Version bumps"
symbols = ["SRCREV", "SRC_URI", "LIC_FILES_CHKSUM"]
+++

Review notes for a recipe whose revision, version or source changes. A bump
that builds can still pin the wrong commit or ship different files.

- Resolve the tag the recipe names with
  `git ls-remote <url> 'refs/tags/<tag>^{}'` and compare it with `SRCREV`.
  A tag can move, so check again on a re-review.
- Check every `SRC_URI` checksum against the artifact itself, by hashing a
  download or reading the server's checksum header. A checksum copied from a
  sibling recipe is a common mistake.
- Read the upstream history between the old and the new revision. Flag
  changes to behaviour, dependencies, installed files or library names that
  neither the recipe nor the commit message accounts for.
- Check that each carried patch still applies to the new source, and that a
  patch merged upstream is dropped.
- When `LIC_FILES_CHKSUM` changes, read the licence difference. When the
  licence file changed and the checksum did not, the recipe is not reading
  the file you think it is.
- For prebuilt binaries, compare the old and new archives: file list,
  `NEEDED` entries, library names, and the versions they require against
  what the layers pinned in `ci/base.lock.yml` provide.
- When a recipe file is renamed for a new version, check that nothing still
  refers to the old one: `PREFERRED_VERSION`, bbappends, and the layers
  that build on this one.
- The subject follows `recipe-name: upgrade X -> Y`.
