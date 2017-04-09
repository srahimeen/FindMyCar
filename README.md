# FindMyCar
Exactly what it’s named; an app that helps you find your car! Wherever you're parked, this app will help youn find your way back to it.

## Screenshots

<img src="http://i.imgur.com/LTYydPv.png" width="263px" height="420px" /> <img src="http://i.imgur.com/D2Hq3Gf.png" width="263px" height="420px" /> <img src="http://i.imgur.com/pudiKHu.png" width="263px" height="420px" /> 

## Installation
Clone the repository and open with Android Studio. Build and run the application from Android Studio onto your device or on the Android Emulator. This application utilizes the GPS, Magnetometer and Accelerometer sensors along with Internet connectivity.

## System Design

### Activities and Fragments

#### MainActivity and MainFragment: 
Initial landing page which displays the map, the live current location of the car as a marker, and the button to mark it as parked. When the user marks their car as parked, the FloatingActionButton will turn into a directions button. When the pin is placed, a floor number is either calculated if the correct sensors are present, or manually entered via a dialog.

#### CompassActivity: 
Once the user presses the directions button, the CompassActivity will start. The CompassActivity will display a compass which will direct the user towards the previously placed “pin” for the parked car, as well as display which elevation level (Floor Number, Height, etc.) the car was parked at.  Once the car has been found, the user will be able to tap the the back button in order to mark the car as found and be redirection to the MainActivity/MainFragment.

#### HistoryFragment:  
This displays the places that the user has parked at in the past stored in a local SQLite database.

#### GarageInfoFragment: 
This displays detailed information about the parking garage/pinned location.

#### SettingsFragment: 
Allows user to make changes to how the application functions. 

### Utility Classes

#### CompassAssistant: 
This class is a compass helper. It provides data to rotate a view to point it to north in the compass UI.

#### LocationAddress: 
Get the location from a given latitude and longitude using reverse geocaching and the Geocoder android class.

#### MovingAverageList: 
Gets the moving average as the phone updates the different sensors.

### Ignored Files

**secret_keys.xml:** Required to store values of all API and secret keys used by the application


## Contributing
1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D

## Credits
#### Original Developers: 
Soufin Rahimeen, Aaron Hall, Tin Nguyen

## License
MIT License
