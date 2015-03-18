Collaborative ToDo App for Android using PubNub
================================================

#INTRODUCTION
This is a sample android application which demonstrates a minimal ToDo App. This app levarages the facilities of PubNub 
Realtime Data Stream Network and PubNUb's Android SDK to facilitate communication between different instances of the App thereby allowing collaboration among the users who use the App for collaborating on a project.


#PRECONDITION
This app is tested on the following Android versions / phone models.

#INSTALLATION
The apk package for this app is available on the following link. Transfer the package onto your phone and follow the standard insructions to install it.

#USAGE
1. The App allows the user to login as one of the three predefined usernames (Eric, Peter and Sam).
2. Once logged in the user can start adding tasks by clicking on the new task icon. All tasks are added to a predefined project.
3. Tasks added by one user will be synced in real time on the other users' app window if they are logged in.
4. Users can add tasks, modify a task's information or makr a task as complete. All operations on tasks are automagically synced up across all logged in users.

#SHORTCOMINGS
1. This is a demo app so the usernames and project is predefined and hard coded in the app.
2. All users must be logged in to get the task updates from other users. The app does not support data sync for users who log in at a later point of time.





