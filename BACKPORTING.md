# Backporting to meta-qcom (wrynose LTS)

`wrynose` is the **LTS Stable branch**. It is not the primary development
branch: `master` is. Every change is expected to land on `master` first and
then be **backported** here, unless it is specific to `wrynose` (e.g. it does
not apply to `master`, or `master` has diverged in a way that makes the change
meaningless there).

1. Land the change on **master** first, following the master contribution
   workflow (fork, topic branch, rebase on latest upstream `master`, open a
   PR).
2. Once it is merged on `master`, backport the merged commit(s) to `wrynose`
   using **`git cherry-pick -x`** so the backport records the original commit
   hash in the message (`(cherry picked from commit <sha>)`):

   ```sh
   git fetch origin
   git checkout -b backport/<PR-or-topic>-to-wrynose origin/wrynose
   git cherry-pick -x <sha> [<sha> ...]      # -x records the source commit
   # resolve conflicts if any, keeping the change minimal and equivalent
   ```

3. Run the CI-equivalent checks (see below).
4. When the user asks for the pull request, open it against **wrynose**,
   following the `[Backport wrynose]` subject convention (see [Commit
   messages for backports](#commit-messages-for-backports)).
5. Use PR discussion for review iteration.

## Before opening/updating a backport PR

Run the CI-equivalent checks in this order:

```sh
ci/kas-container-shell-helper.sh ci/yocto-patchreview.sh
ci/kas-container-shell-helper.sh ci/yocto-check-layer.sh
ci/kas-container-shell-helper.sh ci/oe-selftest.sh
```

## Commit messages for backports

For backports, preserve the original master commit message and metadata, and
keep the `cherry-pick -x` trailer intact. The branch convention is to prefix
the subject with `[Backport wrynose]`, as seen in recent history:

- `[Backport wrynose] iq-9075-evk: build sdcc feature DTs as overlays (#2463)`
- `[Backport wrynose] kgsl-dlkm: Update to v1.0.4 (#2409)`
- `[Backport wrynose] linux-qcom-6.18 : update to tag qcom-6.18.y-20260611 (#2447)`

The backported body should retain the original explanation and end with the
cherry-pick trailer that `git cherry-pick -x` adds:

```text
(cherry picked from commit <sha>)
```

`git cherry-pick` preserves the original author's `Signed-off-by`; add your own
with `git cherry-pick -s` if your workflow requires it.
