# Append the source revision to the kernel release: -g<sha> of the revision
# the fetcher resolved, plus -p<count> when SRC_URI applies patches on top
# of it. Consumers set
#   KERNEL_LOCALVERSION ?= "${@qcom_kernel_localversion(d)}"

def qcom_kernel_localversion(d):
    import re
    import oe.patch
    try:
        revs = bb.fetch.get_hashvalue(d).split()
    except bb.fetch.BBFetchException as e:
        bb.warn('Cannot resolve the kernel source revision: %s' % e)
        return ''
    # The task signature code expands this with an empty SRC_URI
    # (BB_HASH_CODEPARSER_VALS), which has nothing to resolve
    if not revs:
        return ''
    rev = revs[0].lower()
    if not re.fullmatch('[0-9a-f]{12,}', rev):
        bb.warn('Kernel source revision "%s" is not a commit id' % rev)
        return ''
    ver = '-g' + rev[:12]
    patches = len(oe.patch.src_patches(d))
    if patches:
        ver += '-p%d' % patches
    return ver
