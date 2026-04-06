package code;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // ── 1. Construction du graphe ────────────────────────────────────
        Graph graph = new Graph();

        Noeud pc1 = new Noeud("PC1", "PC_1", "MACHINE");
        Noeud pc2 = new Noeud("PC2", "PC_2", "MACHINE");
        Noeud pc3 = new Noeud("PC3", "PC_3", "MACHINE");
        Noeud pc4 = new Noeud("PC4", "PC_4", "MACHINE");
        Noeud pc5 = new Noeud("PC5", "PC_5", "MACHINE");
        Noeud pc6 = new Noeud("PC6", "PC_6", "MACHINE");
        Noeud pc7 = new Noeud("PC7", "PC_7", "MACHINE");

        Noeud s1 = new Noeud("S1", "Switch_1", "SWITCH");
        Noeud s2 = new Noeud("S2", "Switch_2", "SWITCH");
        Noeud s3 = new Noeud("S3", "Switch_3", "SWITCH");
        Noeud s4 = new Noeud("S4", "Switch_4", "SWITCH");
        Noeud s5 = new Noeud("S5", "Switch_5", "SWITCH");

        graph.AjoutNoeud(pc1); graph.AjoutNoeud(pc2); graph.AjoutNoeud(pc3);
        graph.AjoutNoeud(pc4); graph.AjoutNoeud(pc5); graph.AjoutNoeud(pc6);
        graph.AjoutNoeud(pc7);
        graph.AjoutNoeud(s1);  graph.AjoutNoeud(s2);  graph.AjoutNoeud(s3);
        graph.AjoutNoeud(s4);  graph.AjoutNoeud(s5);

        graph.AjoudEdge(pc1, s1, 2);
        graph.AjoudEdge(pc2, s3, 1);
        graph.AjoudEdge(pc3, s5, 4);
        graph.AjoudEdge(pc4, s2, 9);
        graph.AjoudEdge(pc5, s4, 5);
        graph.AjoudEdge(pc6, s5, 7);
        graph.AjoudEdge(pc7, s5, 2);

        graph.AjoudEdge(s1, s2, 8);
        graph.AjoudEdge(s1, s3, 2);
        graph.AjoudEdge(s2, s4, 4);
        graph.AjoudEdge(s3, s4, 5);
        graph.AjoudEdge(s3, s5, 9);
        graph.AjoudEdge(s4, s5, 5);

        // ── 2. Affichage console ─────────────────────────────────────────
        graph.display();

        // ── 3. Menu interactif ───────────────────────────────────────────
        Scanner scanner = new Scanner(System.in);
        Dijkstra dijkstra = new Dijkstra(graph);
        GraphStreamVisualizer viz = new GraphStreamVisualizer(graph);

        boolean running = true;
        while (running) {
            System.out.println("\n╔══════════════════════════════╗");
            System.out.println("║     MENU ROUTAGE TP4         ║");
            System.out.println("╠══════════════════════════════╣");
            System.out.println("║ 1. Afficher le réseau        ║");
            System.out.println("║ 2. Calculer plus court chemin║");
            System.out.println("║ 3. Quitter                   ║");
            System.out.println("╚══════════════════════════════╝");
            System.out.print("Choix : ");

            String choix = scanner.nextLine().trim();

            switch (choix) {
                case "1":
                    viz.displayNetwork();
                    break;

                case "2":
                    // Lister les noeuds disponibles
                    System.out.println("\nNoeuds disponibles :");
                    for (Noeud n : graph.getNoeudes()) {
                        System.out.println("  " + n.getId() + " → " + n.getNomComposant()
                                + " [" + n.getComposant() + "]");
                    }

                    System.out.print("\nID source      : ");
                    String idSrc = scanner.nextLine().trim().toUpperCase();
                    System.out.print("ID destination : ");
                    String idDst = scanner.nextLine().trim().toUpperCase();

                    Noeud source = graph.getRechercheN(idSrc);
                    Noeud dest   = graph.getRechercheN(idDst);

                    if (source == null || dest == null) {
                        System.out.println("⚠  Identifiant(s) inconnu(s). Réessayez.");
                        break;
                    }

                    List<Noeud> path = dijkstra.shortestPath(source, dest);

                    if (!path.isEmpty()) {
                        // Affichage texte du chemin
                        System.out.println("\n── Chemin trouvé ──────────────────");
                        for (int i = 0; i < path.size(); i++) {
                            System.out.print(path.get(i).getNomComposant());
                            if (i < path.size() - 1) System.out.print(" → ");
                        }
                        System.out.println();

                        // Affichage graphique avec surbrillance
                        int totalCost = dijkstra.getLastCost();
                        viz.displayShortestPath(path, totalCost);
                    }
                    break;

                case "3":
                    running = false;
                    System.out.println("Au revoir !");
                    break;

                default:
                    System.out.println("Option invalide.");
            }
        }
        scanner.close();
    }
}
