# Ferret version NCBI annotations

## Overview


## Launching the project
### IntelliJ IDEA
- Go into ``File > Open`` and select the project directory
- In the ``Maven`` panel on the right-hand side of the IDE, launch the ``package`` phase
- The executable jar will be located into the ``target`` folder at the project root


### Eclipse
#### Pre-requisite
- Having installed Maven : https://maven.apache.org/install.html
- Having installed the M2Eclipse plugin or any other Maven integration to Eclipse
- If not, go into ``` Help > Install new software > Add... ```. In the ``` Name ``` field enter ``M2Eclipse`` and in the ``Location`` field enter ``http://download.eclipse.org/technology/m2e/releases``, then follow the installation guide.
- Select the local Maven installation insted of the embedded one : ``Window > Preferences``, expand ``Maven`` then go into ``Installation > Add``, select the Maven installation folder and tick the box on the new line. Make sure to press ``Apply`` before leaving.

#### Create the project
- Go into ``File > Import``, expand ``Maven`` and select ``Existing Maven Project``, choose your project directory (a line reading ``/pom.xml com.ecn.ferretmvc:Ferret:[version number]:jar`` should appear) then click on ``Finish``

#### Launching the project
- Right click on the project name, then go into ``Run As > Maven Build``
- In the window that just opened, give a name to the configuration and in the field ``goal``, enter ``verify`` (or any other phase coming after test, check the lifecycle for more details)

#### Create an executable jar
- Right click on the project name and go into ``Run As > Maven Build``
- In the ``goal`` field enter ``package`` then run the launch configuration
- The executable jar will be located into the ``target`` folder at the project root