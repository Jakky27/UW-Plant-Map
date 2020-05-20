# UW-Plant-Map

[![Jakky27](https://circleci.com/gh/Jakky27/UW-Plant-Map.svg?style=svg)](https://circleci.com/gh/Jakky27/UW-Plant-Map)

UW Plant Map is developed for the University of Washington's CS 403 class. It will be an app that stores information, including location, of plants on the University of Washington campus. The user uses the app to recognize the plant name and other information. The user can also get connected with others by reading the feeds, pinning the plant image and post on the map, and commenting on others' posts.

# How do I run it?

## Installing the app on an Android phone

You can get a pre-assembled version of the app from [the GitHub releases page](https://github.com/Jakky27/UW-Plant-Map/releases/tag/v1.0-beta). This app contains a testing API key for the Google Maps SDK, so if you're getting a gray map you should obtain your own API key and build the app from scratch using the instructions below.

Note that when sideloading an APK on your Android device for the first time it will warn you about installing applications from third-parties. Please check [here](https://www.xda-developers.com/sideload-apps-how-to/) to find instructions on how to authorize sideloading of apps on your Android device.

Unfortunately the app does not yet ask you for your permission to obtain your location and capturing pictures when you first install it. You may need to manually grant Location access and Camera access permission to the app in your phone's settings. You can find out how to do that [here](https://support.google.com/googleplay/answer/6270602?hl=en).

## Using the app

To view the plants map, go to the “Map” section on the bottom navigation. To search for a specific plant species, tap on the search bar icon and type for the plant species, then tap on the desired autocomplete popup. To view the live submissions feed, tap on the “Feed” section on the bottom navigation. To view the settings, tap on the “Settings” section on the bottom navigation.

## Missing functionality

- Registering a plant does not send the image to the server
- Live feed does not display post description or image
- Search Bar in homepage does not work
- No error handling (no crash report popup)
- No settings menu (no ability to set notifications, etc.)
- No route finding mechanic when selecting a submission post on the map

## Building the app from scratch

The app requires Gradle to run, but a wrapper for Gradle is included with the repo, so all Gradle commands can be run using the `./gradlew` script. Note that the JDK of Java 11 or above must be installed. In order to build the app you must also make sure to have the Android SDK installed. We recommend you build from the command line to avoid any Java version incompatibilities. 

Getting the Android SDK is easy when you have Android Studio installed. You can open the project in Android Studio and/or IntelliJ by importing the project from the root (and specifying that this is a Gradle project if asked).

When you first clone the source, run `./gradlew` in order to generate a `secrets.properties` file. In this file you can put your Google Maps API key. Note that the app will run fine without this API key but the map displaying the plants will be gray.

You can run `./gradlew :frontend:app:assembleDebug` from the root and this will generate the app's APK file in `frontend/app/build/outputs/apk/debug`.

## Running the server locally

You can run the server locally by first building it with `./gradlew stage`, setting the `DB_HOST`, `DB_NAME`, `DB_USER`, `DB_PASSWORD` environment variables, and then running:

`java -cp backend/build/libs/* edu.uw.cs403.plantmap.backend.Application`

When launching the app for the first time, it will detect if your database does not have the required tables and run the necessary SQL commands to create them. The server is known to work with Azure SQL Database, but probably will work with other SQL compatible databases.

## Deploying to Heroku

You can also deploy the backend server to Heroku from scratch.

- If you haven't already, create the Heroku app by running `heroku create <app-name>`. If you've already created the app, use `heroku git:remote -a <app-name>` instead.
- You must use `heroku config:set <var>=<value>` for `DB_HOST`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`.
- Then run `git push heroku master` from the root directory to deploy the project.

Once it is done the backend should be available from `https://<app-name>.herokuapp.com/`.

## Heroku auto-deploy

Our C/I infrastructure is set up to automatically deploy the server to `https://plantmap.herokuapp.com/` if all tests pass when a commit is pushed to `master`. This means small changes to the backend should be made on other branches and then merged into `master` when ready for deployment.

# How do I contribute?

All the source code is stored in this repo. You can clone this repo locally to test the code. For security reasons, we do not push our database's information to the repo. You can however use your own SQL database when testing.

## Reporting a bug

Before reporting a bug:
- Figure out the steps to reproduce a bug. If you can’t reproduce a bug, there's probably no use in reporting it, unless you provide unique information about its occurrence.
- Make sure your software is up to date.
- Try reinstall the app again to see whether the bug still exists.

What is expected to be in a good bug report:
- A clear summary: A good summary should quickly and uniquely identify a bug report. It should explain the problem, not your suggested solution. Fill in a sentence of about 10 words to describe the bug.
- What errors did you see? What problem do you meet?
- Clear reproducing steps. Describes whether you can reproduce the bug, what method you used to interact with our app, what’s the actual results and your expected results.
- Additional information:
  - Version of Android you are using.
  - Any alternative solutions you have tried so far.

## Structure of the project

There are separate directories for frontend and backend. The app's code is located in the `frontend/` directory, and the server hosted on Heroku is located in the `backend/` directory. 

For the frontend our code is organized using the standard Android project file layout. Our Kotlin code is divided up into a couple key packages:

- `ui`: Classes responsible for dictating the behavior of the android application
- `models`: Data structures for our model
- `clients`: Classes responsible for communication with the backend 

For the backend, the code is firstly divided into a source code folder and a testing folder. 

In the source code folder, `Application.java` contains the entry point that starts backend server. This is where the database connection and the API route mapping is made.

The `models` folder stores model classes. The major classes we have used are Plant and Submission. The folder also stores the data access objects (DAOs) for these two classes. You can make other DAOs for other database models by implementing their interface.

The `controllers` folder stores the HTTP handlers. We use the Spark framework to handle the HTTP requests and responses. 

## Running tests locally

Tests are automatically run on our C/I when a commit is made to master, and you can see them [there](https://app.circleci.com/pipelines/github/Jakky27/UW-Plant-Map). If you would like to run all the tests locally you can use `./gradlew :backend:test :frontent:app:testDebugUnitTest`. The backend test report is in `backend/build/reports/tests/test`, and the frontend test report is in `frontend/app/build/reports/tests/testDebugUnitTest`.

Note that for the frontend we also use manual testing to check that app use cases are met. You can find out more about our manual testing in the `docs/testing/ManualTesting.md` file.

## Adding new unit tests

This project uses JUnit for testing. For the backend, please add the new test code to `backend/src/test/java/edu/uw/cs403/plantmap/backend/<TestTarget>.java`. For the frontend, unit tests can be added to `frontend/app/src/test/java/edu/uw/cs403/plantmap/<TestTarget>.java`. Additionally, Android instrumented tests can be added to `frontend/app/src/androidTest/java/edu/uw/cs403/plantmap/<TestTarget>.java`. Make sure to add whatever package is being tested if it does not already exist there. The naming pattern for the testing class should be 'Test<Name-of-class-being-tested>.java'. Try to use existing frameworks being used in our testing suite (for example, our front end uses Mockito for mocking).
  
## Releasing a new version

To tag a commit for release, go to the Releases tab of our GitHub repo and select 'Draft a new release.' From there use `v<major>.<minor>` as the tag version and `Release <major>.<minor>` as the release title. You will also need to upload the commit's generated APK file from the frontend workflow artifacts, and all the JAR files from the backend workflow artifacts, both of which can be obtained for the current commit on out C/I. Make sure to describe all the new features and known bugs in the release description.
