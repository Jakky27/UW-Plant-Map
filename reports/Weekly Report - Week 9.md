
## Weekly Report
Week 9

### Plans from Last Week
* Add more features in frontend
* Handle exceptions and HTTP errors in backend


### Progress and Issues
* Add more testings for both frontend and backend
* Implementing the user case of selecting a plant from map
* Implementing the report function
* Adding dubugging tools in the backend
* Fully implemented the image function (add image in a submission and get the image)
* Fixed unknown server side bugs (which turns out to be a bug from Azure docs)


### Plans for Next Week
* Finishing touches on frontend reporting use case.
* Get more user feedbacks
* Prepare for the final release and presentation

________________


## Contributions of individual team members.
**Brian**
* Added report attribute to submission table in database and added server calls to update the new attribute and updated submission creation to initalize this value.


**Daniel**
* Implemented use case for selecting a plant from the map to view its submission
* Added uploding an image when posting a submission
* Finished front-end client to handle network requests to back-end

**Huan**
* Impelemented testing for all handler functions using Mockito
* Modified user guidance and developer document

**Jacky**
* Migrating frontend code to the backend client 

**Kouroche**
* Added debugging tools to the backend to find nasty connection to database bug
* Also fixed said bug! Apparently copying code from Microsoft Docs is a bad idea
* Fixed some bugs in the image route handler.
* Images are now fully implemented in the backend! Check it out!
https://plantmap.herokuapp.com/v1/image/1
