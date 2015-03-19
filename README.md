Collaborative ToDo App for Android using PubNub
================================================

##INTRODUCTION
This is a sample android application which demonstrates a minimal ToDo App. This app levarages the facilities of PubNub 
Realtime Data Stream Network and PubNUb's Android SDK to facilitate communication between different instances of the App thereby allowing collaboration among the users who use the App for collaborating on a project.


##ANDROID VERSION
This app is tested on the Android 4.4.2 & 4.2 (Kitkat) only.  

##INSTALLATION
The latest version of this app is available on the below GitHub Release page.
https://github.com/chinmoyeedash/pubnub-todoapp-android/releases/tag/pubsub-0.1

Transfer the APK package onto your phone and follow the standard insructions to install it.

##USAGE
1. The App allows the user to login as one of the three predefined usernames (Peter, Sam or Eric). Launch the app from two or three different phones by logging in with one of the predefined users. There is no password required for this app.
2. Once logged in, the user can start adding tasks by clicking on the new task (+) icon on the top. All tasks are added to a predefined project.
3. All tasks added or modified by any one of the users will be  updated on the other users' app window to allow seamless collaboration.
4. Users can add tasks, modify a task's information or mark a task as complete. All operations on tasks are automagically synced up across all logged in users.

##SHORTCOMINGS
1. This is a demo app so the usernames and project is predefined and hard coded in the app.
2. All users must be logged in to get the task updates from other users. The app does not support data sync for users who log in at a later point of time.
3. The App is tested only on Android Kitkat. A sollow on update will be done in a few days to test it for Lolipop.





