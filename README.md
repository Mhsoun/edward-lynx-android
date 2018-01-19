# Edward Lynx! (Android App) #
Technical Documentation 

### List of Software and Third-party libraries used ###
This application was developed using Android Studio (latest on stable channel) as its IDE. Android emulator for the device simulator on your local machine. 

Libraries used:

* [Rxjava with retrofit](http://randomdotnext.com/retrofit-rxjava/)
RxJava and RxAndroid libraries allow us to easily do async processing using principles of functional reactive programming. Square's Retrofit project allows us to easily handle HTTP requests and responses
* [Single Date and Time Picker](https://github.com/florent37/SingleDateAndTimePicker)
A custom date and time picker in one widget
* [Firebase](https://firebase.google.com/docs/cloud-messaging/android/client)
Firebase Cloud Messaging (FCM) is a cross-platform messaging solution that lets you reliably deliver messages
* [Expandable RecyclerView](https://bignerdranch.github.io/expandable-recycler-view/)
Expandable RecyclerView can be used with any stock Android RecyclerView to provide expandable items.
* [FitChart](https://github.com/txusballesteros/fit-chart)
Fit Chart is an Android view similar to Google Fit wheel chart
* [MpAndroidChart](https://github.com/PhilJay/MPAndroidChart)
A powerful Android chart view / graph view library, supporting line- bar- pie- radar- bubble- and candlestick charts as well as scaling, dragging and animations.
* [Circle Page Indicator](https://github.com/ongakuer/CircleIndicator)
A lightweight viewpager indicator like in nexus 5 launcher
* [Crashlytics](https://try.crashlytics.com/)
Crashlytics is part of Fabric, which has been acquired by Google. We expect to continue to operate Fabric without any interruptions at this time; however, terms of use will change in connection with the transaction.


## Project Repository ##
The project is hosted in Bitbucket, under this repository. The repository contains the ff. Files:

* edward-lynx-android - the Android project folder wherein all project-related files needed to be able to successfully run the project through Android Studio.
* .gitignore - a file needed to be able to seclude unnecessary files to the repository.

## Build Process ##
To be able to run the project to your local machine, the ff. conditions must be satisfied.

* Android Studio 2.2 or higher is installed on your machine.
* Android SDK (included on Android Studio installer) is installed on your machine specifically API 15 and above needed. Also all the latest Google repository and Google play service
* Genymotion is optional. It is used for the device simulator on you local machine. You can use the Android Studio emulator or an actual Android phone, just enable Developer options of your phone using this link.

### Running the app ###
Satisfying the above mentioned conditions, we can now start building the application on our local machines:

* Clone the repository from the link provided in the Project Repository section of this document.
* After the repository has been cloned, open Android studio app and choose open an existing Android Studio project. Locate the folder of the cloned repository and choose ingenuity-insta-android-app.
* Click Sync Now on the upper right of the IDE for syncing all the dependencies needed
* Run the project using the Run button in the Android Studio IDE or on the Menu bar, choose Run and click run ‘app’. It should work and run the app without any issues.

## Deployment (Test Builds) ##
Test Builds will be distributed to users with the help of HockeyApp. An .apk file is needed to be uploaded to the HockeyApp app we created earlier for the vendor target. 

* On the Build Variants Section on the lower left of the IDE, choose the build variant to build a release. 
* After syncing, Click **Build** and select **Build apk**.
* The release build apk is located on **app/build/outputs/apk** folder and select the apk with product vendor name with the release keyword.
* After the .apk file is generated, you can now start uploading the file to HockeyApp.

*Click the Add Version button in the dashboard.
*Simply drag the .apk file in the designated area to start uploading.

## Unit and UI Testing ##
The project includes Unit and UI testing to test various parts of the app such as API calls and the UI views. Android Studio has built-in tools for Unit and UI testing which makes is easier for developers to run tests. To run these test cases:

### UI Testing ###
* Open terminal window in Android Studio.
* Run **./gradlew installDebugAndroidTest**
### Unit Testing ###
* Open terminal window in Android Studio.
* Run **./gradlew testDebugUnitTest**