Udacity Android Developer Nanodegree. Project 7: Capstone, Stage 1 - Design  
Udacity Android Developer Nanodegree. Project 8: Capstone, Stage 2 - Build

This Capstone project for Udacity Android Developer Nanodegree corresponds with WatchThemAll series tracker app.

# WatchThemAll

## Description 

WatchThemAll is a series tracker app. Nowadays there are quite a lot of options regarding TV series. Sometimes it may be difficult to decide which series to watch and to be sure of not forgetting any episode of our favorite shows. This application aims to make all of that more easy to the user, at the same time that makes the hobby of watching and following TV series something much more enjoyable and funny.


## Intended User

This app is intended to TV series lovers, specially to those people who want to keep track of their favorite shows, to stay always updated and to know about new series to watch.

## Features

- Search TV series by keywords.
- See the details of a certain show or episode (including other users comments).
- Mark (and consult) the series (or episodes) you want to watch in the future (the watchlist).
- Mark (and consult) the series you have already seen (the collection). The user will be able to assign a grade to each show.
- Mark (and consult) the series you are currently watching.  
- Receive updated info about popular TV series.
- Personally configure some application settings: default order used in the results screen, initial screen opened when starting the app, etc.

## IMPORTANT
 
In order to get WatchThemAll app correctly working is needed to configure and use a trakt.tv API KEY.  
To get one, you must sign up for an account (https://trakt.tv/auth/join) and then register your own API app (https://trakt.tv/oauth/applications/new). This way, you will get a Client ID associated to your API app.  
Since it is not allowed to publicly share your personal API KEY, the code in this repository does not contain mine. Therefore, it would be needed to replace *PLACE_HERE_YOUR_TRAKT_API_APP_CLIENT_ID* placeholder in app *build.gradle* file with your actual and valid trakt API app Client ID:
  
app/build.gradle:
```gradle
apply plugin: 'com.android.application'
apply plugin: 'com.google.gms.google-services'
apply plugin: 'com.neenbedankt.android-apt'

android {
    ...
    buildTypes.each {
        it.buildConfigField 'String', 'TRAKT_API_KEY', '\"PLACE_HERE_YOUR_TRAKT_API_APP_CLIENT_ID\"'
    }
}
...
```

**The app is currently in development process. Latest version is 1.0 (which has been submitted as final project for Udacity Android Nanodegree)**

Some features from the above list has not been covered yet. Here there are some of the future lines for this project:

- Make it possible to the user to introduce a personal mark for each collected show or episode.
- Make it configurable the initial screen when the user opens the app (and also include a loading splash).
- Include improvements in the navigation drawer (in main screen) and the available options.
- Include internationalization also for the data comming from Trakt API that could be automatically translated to the supported languages (for now, spanish)
- Data backup through Drive or Dropbox
- Make it possible to use some filters in series search
- App free and paid variants
- Sharing functionality for both shows and episodes
- Notifications
- Watch faces
- Trakt account integration and more operations with the API
