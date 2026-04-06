# TP4 — Routage & Algorithme de Dijkstra

Projet Java réalisé dans le cadre du cours de Réseaux de licence 3 informatique à ULHN.
Il simule un réseau de machines et de commutateurs, calcule les plus courts chemins via l'algorithme de Dijkstra, génère les tables de routage et les circuits virtuels, et visualise le tout avec GraphStream.

## Fonctionnalités

- Visualisation graphique du réseau (GraphStream)
- Calcul du plus court chemin entre deux nœuds (Dijkstra)
- Génération des tables de routage pour tous les commutateurs ou un seul
- Établissement de circuits virtuels entre deux machines avec ports d'entrée/sortie

## Structure
src/
└── code/
    ├── Main.java                  # Point d'entrée, menu interactif
    ├── Graph.java                 # Modèle du graphe (nœuds + arêtes)
    ├── Noeud.java                 # Représente un nœud (machine ou switch)
    ├── Edge.java                  # Représente une liaison avec coût
    ├── Dijkstra.java              # Algorithme de Dijkstra
    ├── RoutingTable.java          # Calcul et affichage des tables de routage
    ├── VirtualCircuit.java        # Calcul et affichage des circuits virtuels
    └── GraphStreamVisualizer.java # Visualisation graphique avec GraphStream

    ## Prérequis

- Java 11+
- JAR GraphStream : `gs-core` et `gs-ui-swing`

## Lancement
```bash
javac -cp ".:lib/*" code/*.java
java -cp ".:lib/*" code.Main
```

## Auteur

Ahmadou B. Morba — Mastère Cybersécurité, ULHN  
