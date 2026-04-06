<<<<<<< HEAD
# TP4 Réseau — Routage avec GraphStream

## Prérequis
- Java 11+
- Maven 3.6+

## Structure du projet

```
tp4/
├── pom.xml
└── src/
    └── main/
        └── java/
            └── code/
                ├── Noeud.java
                ├── Edge.java
                ├── Graph.java
                ├── Dijkstra.java          ← version mise à jour (getLastCost)
                ├── GraphStreamVisualizer.java  ← NOUVEAU
                └── Main.java              ← version mise à jour (menu interactif)
```

## Compilation et exécution

```bash
# Compiler et créer le JAR avec dépendances
mvn clean package

# Lancer l'application
java -jar target/tp4-routage-1.0-jar-with-dependencies.jar
```

## Légende de l'affichage GraphStream

| Couleur / Forme     | Signification              |
|---------------------|----------------------------|
| Cercle bleu         | Machine (PC)               |
| Carré orange        | Commutateur (Switch)       |
| Nœud vert           | Source du chemin           |
| Nœud rouge          | Destination du chemin      |
| Nœud jaune          | Nœud intermédiaire du chemin |
| Arête rouge épaisse | Arête faisant partie du plus court chemin |

## Dépendances GraphStream

- `gs-core 2.0` — API principale (graphe, attributs, algorithmes)
- `gs-ui-swing 2.0` — Moteur d'affichage Swing
=======
# LeRoutage
>>>>>>> 7b0c017112bcfac434bf54647910eda551546a41
