# softwaredev

Don't change the git ignore.

Instructions:

Install git
go to git shell or open cmd
first time:
  make a folder in your documents called softwaredev and cd to it
  git clone https://github.com/Adhyun/softwaredev.git
  cd into the new subfolder

Made changes:
  in the directory navigated to as above.
  git add *
  git commit -a -m "message here"
  git push -u
  
If it says branch is not up to date or you are coming back after a while:
  git pull
  
If you want to create a new branch to make a big change without screwing the rest of us up:
  git branch newbranchname
  git checkout newbranchname
  
To switch branches:
  git checkout otherbranch
Never merge two branches, leave that for me when we have checked that there are no conflicts.
  
