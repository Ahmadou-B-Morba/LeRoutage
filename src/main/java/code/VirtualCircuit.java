package code;

import java.util.*;

/**
 * Table de circuit virtuel (VC) entre deux machines.
 *
 * Un circuit virtuel représente la séquence des liaisons à établir
 * dans l'ordre pour acheminer les données de la machine source
 * jusqu'à la machine destination.
 *
 * Chaque ligne indique :
 *   - le commutateur traversé
 *   - le port d'entrée  (interface par laquelle le paquet arrive)
 *   - le port de sortie (interface par laquelle le paquet repart)
 *   - le lien vers le noeud suivant
 */
public class VirtualCircuit {

    /** Une ligne de la table de circuit virtuel */
    public static class VCEntry {
        public final Noeud  switchNode;   // commutateur traversé
        public final int    portIn;       // port d'entrée
        public final int    portOut;      // port de sortie
        public final Noeud  from;         // noeud précédent
        public final Noeud  to;           // noeud suivant

        public VCEntry(Noeud switchNode, int portIn, int portOut,
                       Noeud from, Noeud to) {
            this.switchNode = switchNode;
            this.portIn     = portIn;
            this.portOut    = portOut;
            this.from       = from;
            this.to         = to;
        }
    }

    private final Noeud        source;
    private final Noeud        destination;
    private final List<Noeud>  path;          // chemin complet source → destination
    private final List<VCEntry> vcEntries;    // lignes du circuit virtuel
    private final int           totalCost;

    // ── Constructeur ─────────────────────────────────────────────────────
    public VirtualCircuit(Noeud source, Noeud destination, Graph graph) {
        this.source      = source;
        this.destination = destination;

        // Calcul du plus court chemin via Dijkstra
        Dijkstra dijkstra = new Dijkstra(graph);
        this.path      = dijkstra.shortestPath(source, destination);
        this.totalCost = dijkstra.getLastCost();
        this.vcEntries = new ArrayList<>();

        if (!path.isEmpty()) {
            buildVCTable(graph);
        }
    }

    // ── Construction de la table VC ──────────────────────────────────────
    private void buildVCTable(Graph graph) {
        /*
         * On parcourt le chemin noeud par noeud.
         * Pour chaque commutateur intermédiaire (i.e. type SWITCH),
         * on détermine :
         *   - portIn  = index du voisin précédent dans la liste de voisins du switch
         *   - portOut = index du voisin suivant  dans la liste de voisins du switch
         *
         * Remarque : la numérotation des ports est basée sur l'ordre dans
         * lequel les voisins ont été ajoutés au graphe (getVoisin() = liste d'adjacence).
         */
        for (int i = 1; i < path.size() - 1; i++) {
            Noeud prev    = path.get(i - 1);
            Noeud current = path.get(i);
            Noeud next    = path.get(i + 1);

            // current doit être un switch (les machines n'ont pas de table VC)
            if (!current.getComposant().equals("SWITCH")) continue;

            List<Edge> voisins = graph.getVoisin(current);

            int portIn  = findPortIndex(voisins, prev);
            int portOut = findPortIndex(voisins, next);

            vcEntries.add(new VCEntry(current, portIn, portOut, prev, next));
        }
    }

    /** Retourne l'index (numéro de port) du voisin dans la liste d'adjacence. */
    private int findPortIndex(List<Edge> voisins, Noeud target) {
        for (int i = 0; i < voisins.size(); i++) {
            if (voisins.get(i).getDestination().equals(target)) {
                return i;
            }
        }
        return -1; // ne devrait pas arriver
    }

    // ── Affichage console ────────────────────────────────────────────────
    public void display() {
        if (path.isEmpty()) {
            System.out.println("Aucun circuit virtuel : pas de chemin entre "
                + source.getNomComposant() + " et " + destination.getNomComposant());
            return;
        }

        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.printf ("║  Circuit Virtuel : %-43s║%n",
            source.getNomComposant() + " → " + destination.getNomComposant());
        System.out.printf ("║  Coût total      : %-43s║%n", totalCost);
        System.out.println("╠══════════════════════════════════════════════════════════════╣");

        // Chemin complet
        System.out.print("║  Chemin : ");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < path.size(); i++) {
            sb.append(path.get(i).getNomComposant());
            if (i < path.size() - 1) sb.append(" → ");
        }
        System.out.printf("%-52s║%n", sb.toString());

        System.out.println("╠══════════════════╦═══════════╦═══════════╦══════════════════╣");
        System.out.println("║    Commutateur   ║ Port entré║ Port sort.║  Lien suivant    ║");
        System.out.println("╠══════════════════╬═══════════╬═══════════╬══════════════════╣");

        // Liaison machine source → premier switch
        System.out.printf("║ %-16s ║     —     ║  port %-3d ║ %-16s ║%n",
            source.getNomComposant(),
            vcEntries.isEmpty() ? 0 : vcEntries.get(0).portIn,
            vcEntries.isEmpty() ? destination.getNomComposant() : vcEntries.get(0).switchNode.getNomComposant());

        // Lignes intermédiaires (switchs)
        for (int i = 0; i < vcEntries.size(); i++) {
            VCEntry e = vcEntries.get(i);
            String nextLabel = (i < vcEntries.size() - 1)
                ? vcEntries.get(i + 1).switchNode.getNomComposant()
                : destination.getNomComposant();

            System.out.printf("║ %-16s ║  port %-3d ║  port %-3d ║ %-16s ║%n",
                e.switchNode.getNomComposant(),
                e.portIn,
                e.portOut,
                nextLabel);
        }

        // Liaison dernier switch → machine destination
        System.out.printf("║ %-16s ║     —     ║     —     ║ %-16s ║%n",
            destination.getNomComposant(), "arrivée");

        System.out.println("╚══════════════════╩═══════════╩═══════════╩══════════════════╝");

        // Liste ordonnée des liaisons à établir
        System.out.println("\n  Liaisons à établir dans l'ordre :");
        for (int i = 0; i < path.size() - 1; i++) {
            System.out.printf("    %d. %s  ──────  %s%n",
                i + 1,
                path.get(i).getNomComposant(),
                path.get(i + 1).getNomComposant());
        }
    }

    // ── Getters ──────────────────────────────────────────────────────────
    public List<Noeud>   getPath()      { return path; }
    public List<VCEntry> getVcEntries() { return vcEntries; }
    public int           getTotalCost() { return totalCost; }
}