PowerConnectedDemo
=================

Android broadcast receiver demo.

This demo uses a BroadcastReceiver class to listen for an ACTION_POWER_CONNECTED
action. This action occurs when the power/usb cable is plugged into the device.

When this action is triggered the BroadcastReceiver class starts a Service class 
to 'play' a user defined message. The Service class itself uses a TextToSpeech 
class to play the message.

I've also added a mini tutorial using the ShowcaseView library. You'll need to 
include this library if you want to build and run the demo. More information can 
be found about ShowcaseView at https://github.com/amlcurran/ShowcaseView.

The SharedPreferences class is used to save the user defined message. 
 
  


