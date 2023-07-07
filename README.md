# cash-app

 This project is developed in jetpack compose using MVVM design pattern. Since there is only one screen and it is fairly simple, jetpack compose has been used since this would accomodate most of the use cases applicable to this screen.
 MVVM architecture has been used in this app due to previous experience with this design pattern and most of compose examples available from google code labs also use MVVM pattern.

Some Trade Offs were made while developing this project due to shortage of time and lack of expertise in certain areas.
  -> For Error Screens, very basic error screens are used to display error information.
  -> Hard coded dimensions and strings are used.
  -> No Dependency Injection is used (due to lack of expertise in this domain).
  -> No Swipe to Refresh functionality was added and instead Retry button is provided to refresh screen in case of error scenario.

Third Party Library Used:

REtrofit: For making network calls.
Moshi: for converting network response to corresponding data objects.

Steps to Run:
1. Download this project from this github repo.
2. Import the project in android studio and install apk on physical device or emulator.
