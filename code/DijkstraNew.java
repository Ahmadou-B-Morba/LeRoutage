package code;

import java.util.*;

public class Dijkstra {

    private Graph graph;
    private int lastCost = 0; // coût du dernier chemin calculé

    public Dijkstra(Graph graph) {
        this.graph = graph;
    }

    public List<Noeud> shortestPath(Noeud source, Noeud destination) {

        // ── 1. Initialisation ──────────────────────────────────────────
        Map<Noeud, Integer> distances    = new HashMap<>();
        Map<Noeud, Noeud>   predecessors = new HashMap<>();
        Set<Noeud>          visited      = new HashSet<>();

        PriorityQueue<Noeud> queue = new PriorityQueue<>(
            Comparator.comparingInt(n -> distances.get(n))
        );

        for (Noeud noeud : graph.getNoeudes()) {
            distances.put(noeud, Integer.MAX_VALUE);
            predecessors.put(noeud, null);
        }

        distances.put(source, 0);
        queue.add(source);

        // ── 2. Exploration ─────────────────────────────────────────────
        while (!queue.isEmpty()) {

            Noeud current = queue.poll();

            if (current.equals(destination)) break;
            if (visited.contains(current))   continue;
            visited.add(current);

            for (Edge edge : graph.getVoisin(current)) {
                Noeud neighbor = edge.getDestination();

                // Éviter le débordement entier si distance == MAX_VALUE
                if (distances.get(current) == Integer.MAX_VALUE) continue;

                int newDist = distances.get(current) + edge.getcout();

                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    predecessors.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        // ── 3. Reconstruction du chemin ────────────────────────────────
        List<Noeud> path = new ArrayList<>();
        Noeud step = destination;

        while (step != null) {
            path.add(0, step);
            step = predecessors.get(step);
        }

        if (path.isEmpty() || !path.get(0).equals(source)) {
            System.out.println("Aucun chemin trouvé entre "
                + source.getNomComposant() + " et " + destination.getNomComposant());
            lastCost = -1;
            return new ArrayList<>();
        }

        lastCost = distances.get(destination);
        System.out.println("Coût total : " + lastCost);
        return path;
    }

    /** Retourne le coût du dernier appel à shortestPath(). */
    public int getLastCost() {
        return lastCost;
    }
}
