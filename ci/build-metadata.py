#!/usr/bin/env python3
# Copyright (c) 2026 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: MIT

"""Collect build metadata from the image SPDX documents of a build.

oe-core inherits create-spdx by default, so every image build already writes
an SBOM recording each recipe's source URI and resolved revision. Reading the
metadata back out of it costs nothing at build time and avoids having the
build record the same facts twice.

Currently this reports the kernel: its recipe, version, release, repository
and commit.

Nothing here is specific to a kernel recipe: the anchor is the package named
by KERNEL_PACKAGE_NAME ("kernel" unless a layer overrides it), which
kernel.bbclass produces for every provider, qcom or not.

A build writes one document per image and not all of them describe a kernel:
an initramfs image installs kernel modules but no kernel package. Every
document given is therefore examined and the most complete answer wins.
"""
import json, re, sys

KNAME = "kernel"

def provenance(path):
    doc = json.load(open(path))
    graph = doc["@graph"]
    packages = [e for e in graph if e.get("type") == "software_Package"]

    kernel = next((p for p in packages if p.get("name") == KNAME), None)
    if kernel is None:
        # An image with no kernel package, such as an initramfs.
        return {}

    recipe = recipe_of(kernel)
    mine = [p for p in packages if recipe_of(p) == recipe]
    return describe(kernel, recipe, mine)


def recipe_of(entry):
    """The recipe whose SPDX document an entry belongs to."""
    m = re.search(r"/spdxdocs/(.+?)-[0-9a-f]{8}-[0-9a-f]{4}-", entry.get("spdxId", ""))
    return m.group(1) if m else None


def source_index(p):
    """Position of a source within SRC_URI.

    create-spdx numbers sources by their 1-based index in SRC_URI, so a
    recipe's own repository is source/1. linux-yocto fetches yocto-kernel-cache
    as a second git source; the ordering separates them without having to
    recognise the cache by name.
    """
    m = re.search(r"/source/(\d+)$", p.get("spdxId", ""))
    return int(m.group(1)) if m else 1 << 30


def describe(kernel, recipe, mine):
    # Patches carry the purpose "patch" rather than "source", so what is left
    # here is a real source and the lowest index is the recipe's own
    # repository. Every source is still reported, so a surprise stays visible.
    sources = sorted((p for p in mine
                      if p.get("software_primaryPurpose") == "source"),
                     key=source_index)
    primary = sources[0] if sources else None

    repo = commit = ""
    if primary:
        loc = primary.get("software_downloadLocation", "")
        if loc.startswith("git+"):
            # git+<url>@<full sha>; a tarball kernel has no revision to report.
            repo, _, commit = loc[4:].partition("@")
        else:
            repo = loc

# kernel.bbclass names one package "<KERNEL_PACKAGE_NAME>-<KERNEL_VERSION>".
# Keying off that rather than "kernel-image-<type>" keeps this independent of
# KERNEL_IMAGETYPE, which differs per machine (Image, Image.gz, zImage).
    release = ""
    for p in mine:
        m = re.fullmatch(re.escape(KNAME) + r"-(\d.*)", p.get("name") or "")
        if m:
            release = m.group(1)
            break

    return {
        "kernel-recipe": recipe or "",
        "kernel-version": kernel.get("software_packageVersion", ""),
        "kernel-release": release,
        "kernel-repo": repo,
        "kernel-commit": commit,
        **({"kernel-all-sources": [p.get("software_downloadLocation", "") for p in sources]}
           if len(sources) > 1 else {}),
    }


def completeness(result):
    """A result with a commit beats one with only a release, beats nothing."""
    return (bool(result.get("kernel-commit")), bool(result.get("kernel-release")))


best = {}
for path in sorted(sys.argv[1:]):
    try:
        result = provenance(path)
    except (OSError, ValueError, KeyError):
        continue
    if completeness(result) > completeness(best):
        best = result
    if best.get("kernel-commit"):
        # Nothing better to find; skip parsing the remaining documents.
        break

print(json.dumps(best, indent=2))
