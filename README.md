# UW-Plant-Map

[![Jakky27](https://circleci.com/gh/Jakky27/UW-Plant-Map.svg?style=svg)](https://circleci.com/gh/Jakky27/UW-Plant-Map)

UW Plant Map is developed for the University of Washington's CS 403 class. It is an Android app on which users can upload plant details of plants on the campus. The app's code is located in the `frontend/` directory, and the server hosted on Heroku is located in the `backend/` directory.

## Deploying to Heroku

These are instructions on how to deploy the backend server to Heroku from scratch.

- If you haven't already, create the Heroku app by running `heroku create <app-name>`.
- Then run `git subtree push --prefix backend/ heroku master` from the root directory to deploy the project.
- Once finished, the backend should be available from `https://<app-name>.herokuapp.com/`.