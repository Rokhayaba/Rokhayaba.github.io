# Ferret version RegulomeDB

## Overview

## New features
Ferret 2.3 adds a new annotation. Select ```Advanced annotations``` in the ```Settings``` menu to find the [RegulomeDB](https://regulomedb.org) score in the .txt output file.

You can also choose to output an html file to view the .txt file's content in a fancier way. Open it with your Internet browser to view the result in a table, with colours and links to the sources of the various scores.


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