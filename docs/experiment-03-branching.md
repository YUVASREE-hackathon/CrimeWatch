# Experiment 3 — Branching, Merge, and Conflict Resolution

## Aim

Demonstrate a practical feature-branch strategy and resolve one safe, intentional text conflict.

## Requirements

Experiment 2 completed and the working tree clean.

## Commands

PowerShell:

```powershell
git switch -c develop
git push -u origin develop
git switch -c feature/reporting
# Make and commit a real reporting change.
git add frontend backend
git commit -m "feat: improve incident reporting workflow"
git switch develop
git merge --no-ff feature/reporting
```

Safe conflict exercise using `docs/conflict-demo.txt`:

```powershell
git switch -c feature/conflict-demo
# Edit the single demonstration line and commit it.
git switch develop
# Edit the same line differently and commit it.
git merge feature/conflict-demo
git status
# Resolve markers in the text file.
git add docs/conflict-demo.txt
git commit -m "docs: resolve branching exercise conflict"
```

## Configuration

Use `main` for stable integrated work, `develop` for integration, and `feature/*` for focused changes. Never manufacture conflicts in application or secret files.

## Execution

Capture the conflict markers, chosen resolution, final merge commit, and `git log --graph`.

## Expected output

Git pauses the merge with one `both modified` file and completes after the resolved file is staged and committed.

## Actual result

The branch commands are intentionally not executed automatically because they alter the student's repository history and remote. The exact safe exercise is prepared above.

## Screenshots

See Experiment 3 in the manifest.

## Result

Complete when the graph shows both feature history and the resolved merge commit.

