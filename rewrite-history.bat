@echo off
set FILTER_BRANCH_SQUELCH_WARNING=1
git filter-branch --force --env-filter " ^
    if [ \"$GIT_AUTHOR_EMAIL\" = \"duongpv@webjapan.co.jp\" ]; then ^
        export GIT_AUTHOR_NAME=\"contributor\"; ^
        export GIT_AUTHOR_EMAIL=\"contributor@example.com\"; ^
    fi; ^
    if [ \"$GIT_COMMITTER_EMAIL\" = \"duongpv@webjapan.co.jp\" ]; then ^
        export GIT_COMMITTER_NAME=\"contributor\"; ^
        export GIT_COMMITTER_EMAIL=\"contributor@example.com\"; ^
    fi" --tag-name-filter cat -- --branches --tags
