+++
title = "Licensing"
paths = ["licenses/"]
symbols = ["LICENSE", "LIC_FILES_CHKSUM", "NO_GENERIC_LICENSE", "RDEPENDS"]
+++

Review notes for licence metadata and for changes that alter what an image
is allowed to contain.

- `LICENSE` describes everything the recipe ships. A recipe that bundles
  third-party code lists that code's licences as well.
- `LIC_FILES_CHKSUM` points at the licence text that comes with the source
  being fetched, not at a copy fetched separately.
- A licence that is not a common one must resolve to its text: a file
  under `licenses/`, or `NO_GENERIC_LICENSE` pointing at the licence file
  in the source. The name after `LicenseRef-` matches in both cases.
- Use the licence expression syntax of the branch the change targets. The
  release branch pins an older OE-Core that may not accept what `master`
  does.
- A new runtime dependency can pull a package with a different licence
  into an image that restricts licences, such as an initramfs. Compare the
  image's dependency list before and after with `bitbake -g`.
- Redistributed binaries need a licence that permits redistribution, taken
  from the artifact itself.
