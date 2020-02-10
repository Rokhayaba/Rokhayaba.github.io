# Ferret MVC_02_08

## Présentation


## Démarrer le projet
### IntelliJ IDEA
- Aller dans ``File > Open`` et sélectionner le dossier du projet
- Dans le panneau ``Maven`` sur le côté droit de l'IDE, lancer la phase ``package``
- Le jar exécutable se trouvera dans le dossier ``target`` de l'arborescence du projet
- Il est possible de créer une configuration de lancement depuis Maven : sélectionner ``Maven`` puis dans le champ ``Command line`` rentrer ``clean verify``


### Eclipse
#### Pré-requis
- Avoir installé Maven : https://maven.apache.org/install.html
- Avoir installé le plugin M2Eclipse ou autre intégration de Maven à Eclipse
- Dans le cas contraire, aller dans ``` Help > Install new software > Add... ``` Dans le champ ``` Name ``` rentrer ``M2Eclipse`` et dans le champ ``Location`` rentrer ``http://download.eclipse.org/technology/m2e/releases``, puis suivre le guide d'installation
- Utiliser l'installation locale de Maven au lieu de la version embarquée : ``Window > Preferences``, dérouler ``Maven`` puis aller dans ``Installation > Add``, sélectionner le dossier d'installation de Maven et cocher la case sur la nouvelle ligne. Appuyer sur ``Apply`` avant de quitter.

#### Création du projet
- Aller dans ``File > Import``, dérouler ``Maven`` et sélectionner ``Existing Maven Project``, choisir le dossier du projet (une ligne contenant ``/pom.xml com.ecn.ferretmvc:Ferret:3.0:jar`` doit apparaître) puis cliquer sur ``Finish``

#### Lancement du projet
- Clic droit sur le projet, puis aller dans ``Run As > Maven Build``
- Dans la fenêtre qui s'ouvre, donner un nom à la configuration et dans le champ ``goal``, rentrer ``verify`` (ou toute phase venant après verify, voir le fonctionnement de Maven)

#### Créer un jar exécutable
- Clic droit sur le projet, aller dans ``Run As > Maven Build``
- Dans le champ ``goal`` rentrer ``package`` puis exécuter la configuration de lancement
- Le jar se trouvera dans le dossier ``target`` de l'arborescence du projet