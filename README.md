# UW-Plant-Map

[![Jakky27](https://circleci.com/gh/Jakky27/UW-Plant-Map.svg?style=svg)](https://circleci.com/gh/Jakky27/UW-Plant-Map)

UW Plant Map is developed for the University of Washington's CS 403 class. It is an Android app on which users can upload plant details of plants on the campus. The app's code is located in the `frontend/` directory, and the server hosted on Heroku is located in the `backend/` directory.

## Downloading the app

You can get a pre-assembled version of the app from [the GitHub releases page](https://github.com/Jakky27/UW-Plant-Map/releases/tag/v1.0-beta). This app contains a testing API key for the Google Maps SDK, so if you're getting a gray map you should obtain your own API key and build the app from scratch using the instructions below.

## Building the app from scratch

In order to build the app you must install the Android SDK, and run `./gradlew assembleDebug` inside the `frontend/` folder. Make sure to insert your Google Maps API key in `frontend/app/manifest/AndroidManifest.xml`. You can obtain the generated APK file in `frontend/app/build/outputs/apk/debug`.

## Running the server locally

You can run the server locally by first building it with `./gradlew stage`, setting the `DB_HOST`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`. environment variables, and then running:

`java -cp backend/build/libs/* edu.uw.cs403.plantmap.backend.Application`

## Deploying to Heroku

You can also deploy the backend server to Heroku from scratch.

- If you haven't already, create the Heroku app by running `heroku create <app-name>`. If you've already created the app, use `heroku git:remote -a <app-name>` instead.
- You must use `heroku config:set <var>=<value>` for `DB_HOST`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`.
- Then run `git subtree push --prefix backend/ heroku master` from the root directory to deploy the project.

Once it is done the backend should be available from `https://<app-name>.herokuapp.com/`.

## Heroku auto-deploy

Our C/I infrastructure is set up to automatically deploy the server to `https://plantmap.herokuapp.com/` if all tests pass when a commit is pushed to `master`. This means small changes to the backend should be made on other branches and then merged into `master` when ready for deployment.
