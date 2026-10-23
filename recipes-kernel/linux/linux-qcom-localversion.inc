# Append the source revision to the kernel release: -g<sha> of the revision
# the fetcher resolved, plus -p<count> when SRC_URI applies patches on top
# of it. Consumers set
#   KERNEL_LOCALVERSION ?= "${@qcom_kernel_localversion(d)}"

def qcom_kernel_localversion(d):
    import re
    import oe.patch
    try:
        fetcher = bb.fetch.Fetch(d.getVar('SRC_URI').split(), d)
    except bb.fetch.BBFetchException as e:
        bb.warn('Cannot resolve the kernel source revision: %s' % e)
        return ''
    scms = [ud for ud in fetcher.ud.values() if ud.method.supports_srcrev()]
    # The task signature code expands this with an empty SRC_URI
    # (BB_HASH_CODEPARSER_VALS), which has nothing to resolve
    if not scms:
        return ''
    rev = (scms[0].revision or '').lower()
    if not re.fullmatch('[0-9a-f]{12,}', rev):
        bb.warn('Kernel source revision "%s" is not a commit id' % rev)
        return ''
    ver = '-g' + rev[:12]
    patches = len(oe.patch.src_patches(d))
    if patches:
        ver += '-p%d' % patches
    return ver
