# Ferret version parallélisation

## Présentation
Cette version exploite le multi-threading pour améliorer les performances de Ferret.


## Démarrer le projet
### IntelliJ IDEA

### Eclipse
Bon courage
#### Pré-requis
- Avoir installé Maven : https://maven.apache.org/install.html
- Avoir installé le plugin M2Eclipse ou autre intégration de Maven
- Dans le cas contraire, aller dans ``` Help > Install new software > Add... ``` Dans le champ ``` Name ``` rentrer ``M2Eclipse`` et dans le champ ``Location`` rentrer ``http://download.eclipse.org/technology/m2e/releases``, puis suivre le guide d'installation
- Utiliser l'installation locale de Maven : ``Window > Preferences`` dérouler ``Maven`` puis aller dans ``Installation > Add`` sélectionner le dossier d'installation de Maven et cocher la case sur la nouvelle ligne. Appuyer sur ``Apply`` avant de quitter.

#### Démarrage
- Aller dans ``File > Import``, dérouler ``Maven`` et sélectionner ``Existing Maven Project``, choisir le dossier du projet (une ligne contenant ``/pom.xml com.ecn.ferretmvc:Ferret:3.0:jar`` doit apparaître) puis cliquer sur ``Finish``