#!/bin/bash

# Local deploy script for the web front-end
# This file is responsible for preprocessing all TypeScript files, making sure
# all dependencies are up-to-date, and copying all necessary files into a
# local web deploy directory, and starting a web server

# This is the resource folder where Maven expects to find our files
TARGETFOLDER=../../backend/src/main/resources
# This is the folder that we used when configuring Javalin in Javalin.create()
WEBFOLDERNAME=public

# Step 1: Make sure we have someplace to put everything.
# Delete the old folder tree, and then make it from scratch.
echo "Deleting $TARGETFOLDER and creating an empty $TARGETFOLDER/$WEBFOLDERNAME"
rm -rf $TARGETFOLDER
mkdir -p $TARGETFOLDER/$WEBFOLDERNAME

# Step 2: Ensure npm dependencies are up-to-date (optional, uncomment if needed)
# echo "Updating node dependencies"
# npm update

# Step 3: Install npm dependencies (if not already installed)
if [ ! -d "node_modules" ]; then
  echo "Installing npm dependencies"
  npm install
fi

# Step 4: Build the React app
echo "Building React app"
npm run build || { echo "Build failed! Exiting."; exit 1; }

# Step 5: Copy static build files to the target folder
echo "Copying static build files"
cp -r ./build/* $TARGETFOLDER/$WEBFOLDERNAME

# Step 6: Launch the server (optional, uncomment if needed)
# echo "Starting local web server at $TARGETFOLDER/$WEBFOLDERNAME"
# npm start $TARGETFOLDER/$WEBFOLDERNAME