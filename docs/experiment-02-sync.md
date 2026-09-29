# Experiment 2 — Git Remote Synchronization

## Aim

Connect the local project to GitHub and demonstrate remote inspection, fetch, pull, and push.

## Requirements

A user-created empty GitHub repository and authenticated Git credential flow.

## Commands

PowerShell:

```powershell
git remote add origin https://github.com/YOUR-USER/crimewatch.git
git remote -v
git push -u origin main
git fetch origin
git log --oneline --decorate --graph --all -10
git pull --ff-only origin main
git status
```

Clone verification in a separate parent directory:

```powershell
git clone https://github.com/YOUR-USER/crimewatch.git crimewatch-clone-check
```

## Configuration

Use the repository URL GitHub displays. Never insert a personal access token into a command, screenshot, `.env`, or remote URL.

## Execution

For a visible fetch-versus-pull demonstration, create one small README edit in GitHub, run `git fetch origin`, show that the working tree did not change, inspect `git diff HEAD..origin/main`, and then run `git pull --ff-only`.

## Expected output

- `fetch` updates `origin/main` only.
- `pull` fast-forwards the local branch and updates the working files.
- `push` reports the branch is synchronized.

## Actual result

GitHub HTTPS reachability returned HTTP 200. Repository creation, authentication, remote addition, and push require the user's GitHub account and are therefore manual actions.

## Screenshots

Capture the terminal before and after fetch, the visible diff, the pull, and the GitHub repository page.

## Result

Complete when local `main` and `origin/main` point to the same commit.

