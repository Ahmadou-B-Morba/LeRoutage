package code;

import java.util.*;

/**
 * Table de routage d'un commutateur.
 *
 * Pour chaque destination connue du réseau, la table indique :
 *   - le prochain saut (next hop)
 *   - le coût total du chemin
 *
 * Principe : on applique Dijkstra depuis ce switch vers tous les autres noeuds,
 * puis on remonte les prédécesseurs pour trouver le premier saut.
 */
public class RoutingTable {

    /** Une ligne de la table de routage */
    public static class Entry {
        public final Noeud destination;
        public final Noeud nextHop;
        public final int   cost;

        public Entry(Noeud destination, Noeud nextHop, int cost) {
            this.destination = destination;
            this.nextHop     = nextHop;
            this.cost        = cost;
        }
    }

    private final Noeud        switchNode;  // le commutateur dont c'est la table
    private final List<Entry>  entries;

    // ── Constructeur : calcule la table depuis ce switch ─────────────────
    public RoutingTable(Noeud switchNode, Graph graph) {
        this.switchNode = switchNode;
        this.entries    = new ArrayList<>();
        compute(graph);
    }

    // ── Dijkstra complet depuis switchNode ───────────────────────────────
    private void compute(Graph graph) {

        Map<Noeud, Integer> dist = new HashMap<>();
        Map<Noeud, Noeud>   pred = new HashMap<>();
        Set<Noeud>          visited = new HashSet<>();

        PriorityQueue<Noeud> pq = new PriorityQueue<>(
            Comparator.comparingInt(n -> dist.get(n))
        );

        for (Noeud n : graph.getNoeudes()) {
            dist.put(n, Integer.MAX_VALUE);
            pred.put(n, null);
        }
        dist.put(switchNode, 0);
        pq.add(switchNode);

        while (!pq.isEmpty()) {
            Noeud cur = pq.poll();
            if (visited.contains(cur)) continue;
            visited.add(cur);

            for (Edge e : graph.getVoisin(cur)) {
                Noeud nb = e.getDestination();
                if (dist.get(cur) == Integer.MAX_VALUE) continue;
                int newDist = dist.get(cur) + e.getcout();
                if (newDist < dist.get(nb)) {
                    dist.put(nb, newDist);
                    pred.put(nb, cur);
                    pq.add(nb);
                }
            }
        }

        // ── Construire les entrées de la table ───────────────────────────
        for (Noeud dest : graph.getNoeudes()) {
            if (dest.equals(switchNode)) continue;          // pas soi-même
            if (dist.get(dest) == Integer.MAX_VALUE) continue; // inaccessible

            // Remonter les prédécesseurs pour trouver le 1er saut
            Noeud nextHop = findNextHop(dest, pred);
            entries.add(new Entry(dest, nextHop, dist.get(dest)));
        }

        // Trier : switches d'abord, puis machines ; par coût croissant
        entries.sort(Comparator
            .comparing((Entry e) -> e.destination.getComposant())  // MACHINE > SWITCH alpha
            .thenComparingInt(e -> e.cost));
    }

    /** Remonte pred[] depuis dest jusqu'à trouver le voisin direct de switchNode. */
    private Noeud findNextHop(Noeud dest, Map<Noeud, Noeud> pred) {
        Noeud cur  = dest;
        Noeud prev = dest;
        while (cur != null && !cur.equals(switchNode)) {
            prev = cur;
            cur  = pred.get(cur);
        }
        return prev; // premier noeud après switchNode sur le chemin
    }

    // ── Affichage console ────────────────────────────────────────────────
    public void display() {
        System.out.println("\n┌─────────────────────────────────────────────────────┐");
        System.out.printf ("│  Table de routage : %-32s│%n", switchNode.getNomComposant());
        System.out.println("├──────────────────┬──────────────────┬────────────────┤");
        System.out.println("│   Destination    │   Prochain saut  │      Coût      │");
        System.out.println("├──────────────────┼──────────────────┼────────────────┤");

        for (Entry e : entries) {
            System.out.printf("│ %-16s │ %-16s │ %6d         │%n",
                e.destination.getNomComposant(),
                e.nextHop.getNomComposant(),
                e.cost);
        }
        System.out.println("└──────────────────┴──────────────────┴────────────────┘");
    }

    // ── Getters ──────────────────────────────────────────────────────────
    public Noeud       getSwitchNode() { return switchNode; }
    public List<Entry> getEntries()    { return entries; }

    /**
     * Calcule et affiche les tables de routage de TOUS les commutateurs du graphe.
     */
    public static void displayAll(Graph graph) {
        System.out.println("\n══════════════════════════════════════════════════════");
        System.out.println("            TABLES DE ROUTAGE — TOUS LES SWITCHS");
        System.out.println("══════════════════════════════════════════════════════");

        for (Noeud n : graph.getNoeudes()) {
            if (n.getComposant().equals("SWITCH")) {
                new RoutingTable(n, graph).display();
            }
        }
    }
}