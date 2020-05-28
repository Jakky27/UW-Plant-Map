
## Weekly Report
Week 8

### Plans from Last Week

- Implementing the flows of inserting and retrieving images
- Handle exceptions and HTTP errors in controller
- Add test cases in CircleCI 


### Progress and Issues
* Reframe the structure of the whole project. Integrate the Gradle build of backend and frontend 
* Write user and developers' documentation
* Implemented the flows of POST and GET images, support both specific add/retrieving and add/retrieving images with submission
* Add mock test for backend server
* Add database unit tests


### Plans for Next Week
- Add more features in frontend
- Handle exceptions and HTTP errors in backend

________________


## Contributions of individual team members.
**Brian**
* Commented all of the server interfaces and controllers
* Implemented JUnit testing for backend server and database connection.
* Next week I hope to have JUnit tests that are more modular and use the testing server implementation.


**Daniel**
* Commented code on front-end
* Wrote unit tests for front-end client and manual tests
* Refactored existing networking code on the front-end into client classes


**Huan**
* Implement the image handlers in backend
* Add mock test server to support testing without database
* Participate in writing the documentation


**Jacky**
* Wrote documentation for live feed frontend
* Had struggles with building the project on my computers


**Kouroche**
* Wrote technical documentation for installing both the server and the app, and contributing to the project
* Fixed a critical bug in the server causing it to lose connection to the database by implementing SQL connection pooling
* Made the C/I better (less failed builds, less useless deploys)
* Cleaned up some of the backend code
* Implemented a system for us to store team secrets securely without ever risking to deploy them (`secrets.gradle`)
