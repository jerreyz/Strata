Setting up OpenGamma Strata in Eclipse
--------------------------------------

This guide is designed to allow you to setup OpenGamma Strata in Eclipse for development.


#### Overview

OpenGamma Strata uses Maven as its primary build system.
Installation into Eclipse is therefore based on the m2e Maven-Eclipse plugin.


#### Installation steps

1. Download Eclipse
 - We recommend using the Classic download of Eclipse as it has less bloat than other builds.
 - We have tested using Eclipse Luna, http://www.eclipse.org/downloads/
  
2. Obtain the OpenGamma source code
 - This can be obtained by cloning the git repository using `git clone https://github.com/OpenGamma/Strata.git`

3. Install and Start Eclipse
 - Simply unzip the download into your preferred installation location
 - Start Eclipse, ensuring that you have a brand new workspace
 - Note that the OpenGamma Strata source code must NOT be located inside your Eclipse workspace
  
4. Install the Eclipse preferences
 - Go to "File -> Import"
 - Select the "General -> Preferences" option
 - In the popup, click the "Browse..." button
 - Choose the file `Strata/eclipse/install/Strata-Eclipse-Preferences.epf`
 - Click "Finish"

5. Install the Eclipse plugins
 - Go to "Help -> Install new software..."
 - Click the 'Add...' button in the top right
 - Add any plugins deemed necessary
 - Restart Eclipse if requested to do so
  
6. Import the OpenGamma source code
 - Go to "File -> Import"
 - Select the "Maven -> Existing Maven Projects" option
 - In the popup, click the "Browse..." button
 - Choose the root directory of the OpenGamma source code - `Strata`
 - Click "Finish"
 - Enjoy a coffee while everything is installed!
   Note that this will involve Maven downloading jar file dependencies to the local repository cache.

7. Get exploring!
 - A variety of launch configurations are supplied.
   See the "Run configurations..." popup (the down arrow beside the play button)


#### Hints and Tips

- Ensure that you do not have your source code inside your Eclipse workspace.
  If you do, nothing will work correctly!

- The combination of Eclipse, Maven and m2e sometimes gets confused.
  If projects have compile errors at any point when they shouldn't, try these three steps:
 - Select the project and right-click "Refresh"
 - Select the project and right-click "Maven -> Update Maven"
 - Select the project and from the main menu choose "Project -> Clean"


#### Active development of OpenGamma Strata

If you are actively devloping OpenGamma Strata, then it is recommended to take some addition steps.

8. Load the code formatter
 - Go to the Eclipse Preferences.
   This is normally 'Window->Preferences...', but 'Eclipse->Preferences..' on Mac.
 - Select the "Java -> Code Style -> Formatter" page
 - Click "Import..."
 - Choose the file `Strata/eclipse/install/Strata-Eclipse-Formatter-Java.xml`
 - Click "OK"

9. Load the code templates
 - Go to the Eclipse Preferences
 - Select the "Java -> Code Style -> Code templates" page
 - Click "Import..."
 - Choose the file `Strata/eclipse/install/Strata-Eclipse-CodeTemplates-Java.xml`
 - Click "OK"


#### Questions and comments

Please contact us publicly via the [forums](http://forums.opengamma.com/) or
privately as per your support contract.

[![OpenGamma](http://developers.opengamma.com/res/display/default/chrome/masthead_logo.png "OpenGamma")](http://developers.opengamma.com)
