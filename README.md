## Modularity
The app is designed from the bottom up to be very modular. This means there are a couple of pieces of code that 'glue' everything together, and beyond that eachs ection of the app is a free-standing piece of code.

In terms of the implementation of this, `MainActivity` is the central controller that runs the navigation and is in charge of showing each of the other modules. Each module is in it's own Java subpackage (e.g., something like `org.openaccessbutton.openaccessbutton.blog` and `org.openaccessbutton.openaccessbutton.advocacy`).

Modules are added completely dynamically to the app using XML. The file `raw/navigation.xml` contains a list of `<item>`s, each of which is a module within the app. So to add a new section to the app, just create a new `Fragment` and reference it in `raw/navigation.xml`.

To keep everything purely modular, modules (Fragments) are allowed to launch child fragments. This is achieved using `OnFragmentNeededListener` which is implemented by `MainActivity`. Modules can simply call `OnFragmentNeededListener.launchFragment` to launch fragments.

## Introduction Pages
When the app launches, unles the user has signed up then IntroActivity is run. This shows the user a number of explanatory pages they swipe through before getting to the main app.


## Button
ButtonSubmitActivity is used to actually record paywall hits. This is again seperate from other modules of the app because it's an overarching thing. ButtonSubmitActivity can be launched by calling it with an ACTION\_SEND intent, and in practice this happens from outside the app (e.g., the share button in browsers) and the OAB button in the browser module inside the app.
