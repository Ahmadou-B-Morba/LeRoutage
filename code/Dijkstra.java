
package code;

import java.util.*;

public class Dijkstra {

    private Graph graph;

    public Dijkstra(Graph graph) {
        this.graph = graph;
    }

    public List<Noeud> shortestPath(Noeud source, Noeud destination) {

        // -----------------------------------------------
        // 1. Initialisation
        // -----------------------------------------------

        // Distance de source vers chaque noeud (∞ par défaut)
        Map<Noeud, Integer> distances = new HashMap<>();

        // Pour reconstruire le chemin : "par quel noeud suis-je arrivé ici ?"
        Map<Noeud, Noeud> predecessors = new HashMap<>();

        // File de priorité : on explore toujours le noeud le plus proche
        // (distance, noeud)
        PriorityQueue<Noeud> queue = new PriorityQueue<>(
            Comparator.comparingInt(n -> distances.get(n))
        );

        // Noeuds déjà visités
        Set<Noeud> visited = new HashSet<>();

        // Initialiser toutes les distances à l'infini
        for (Noeud Noeud : graph.getNoeudes()) {
            distances.put(Noeud, Integer.MAX_VALUE);
            predecessors.put(Noeud, null);
        }

        // La source est à distance 0
        distances.put(source, 0);
        queue.add(source);

        // -----------------------------------------------
        // 2. Exploration
        // -----------------------------------------------
        while (!queue.isEmpty()) {

            // On prend le noeud le plus proche
            Noeud current = queue.poll();

            // Si on est arrivé à destination, on s'arrête
            if (current.equals(destination)) break;

            // Si déjà visité, on passe
            if (visited.contains(current)) continue;
            visited.add(current);

            // Explorer tous les voisins
            for (Edge edge : graph.getVoisin(current)) {

                Noeud neighbor = edge.getDestination();
                int newDist   = distances.get(current) + edge.getcout();

                // Si on a trouvé un chemin plus court
                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    predecessors.put(neighbor, current);
                    queue.add(neighbor); // on l'ajoute à explorer
                }
            }
        }

        // -----------------------------------------------
        // 3. Reconstruction du chemin
        // -----------------------------------------------
        List<Noeud> path = new ArrayList<>();
        Noeud step = destination;

        // On remonte les prédécesseurs depuis la destination
        while (step != null) {
            path.add(0, step); // on ajoute au début
            step = predecessors.get(step);
        }

        // Si le chemin ne commence pas par la source = pas de chemin trouvé
        if (!path.get(0).equals(source)) {
            System.out.println("Aucun chemin trouvé !");
            return new ArrayList<>();
        }

        // Afficher le coût total
        System.out.println("Coût total : " + distances.get(destination));

        return path;
    }
}