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

        // ── 2. Menu interactif ───────────────────────────────────────────
        Scanner scanner = new Scanner(System.in);
        Dijkstra dijkstra = new Dijkstra(graph);
        GraphStreamVisualizer viz = new GraphStreamVisualizer(graph);

        boolean running = true;
        while (running) {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║         MENU — TP4 ROUTAGE           ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1. Afficher le réseau (graphique)   ║");
            System.out.println("║  2. Plus court chemin (Dijkstra)     ║");
            System.out.println("║  3. Tables de routage (tous switchs) ║");
            System.out.println("║  4. Table de routage (un switch)     ║");
            System.out.println("║  5. Circuit virtuel (deux machines)  ║");
            System.out.println("║  6. Quitter                          ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Choix : ");
            String choix = scanner.nextLine().trim();

            switch (choix) {

                // ── Afficher le réseau ──────────────────────────────────
                case "1":
                    viz.displayNetwork();
                    break;

                // ── Plus court chemin ───────────────────────────────────
                case "2":
                    printNoeuds(graph);
                    System.out.print("ID source      : ");
                    String idSrc = scanner.nextLine().trim().toUpperCase();
                    System.out.print("ID destination : ");
                    String idDst = scanner.nextLine().trim().toUpperCase();

                    Noeud src  = graph.getRechercheN(idSrc);
                    Noeud dest = graph.getRechercheN(idDst);

                    if (src == null || dest == null) {
                        System.out.println("⚠  Identifiant(s) inconnu(s).");
                        break;
                    }

                    List<Noeud> path = dijkstra.shortestPath(src, dest);
                    if (!path.isEmpty()) {
                        System.out.print("\nChemin : ");
                        for (int i = 0; i < path.size(); i++) {
                            System.out.print(path.get(i).getNomComposant());
                            if (i < path.size() - 1) System.out.print(" → ");
                        }
                        System.out.println();
                        viz.displayShortestPath(path, dijkstra.getLastCost());
                    }
                    break;

                // ── Tables de routage — tous les switchs ────────────────
                case "3":
                    RoutingTable.displayAll(graph);
                    break;

                // ── Table de routage — un switch au choix ───────────────
                case "4":
                    printSwitchs(graph);
                    System.out.print("ID du switch : ");
                    String idSw = scanner.nextLine().trim().toUpperCase();
                    Noeud sw = graph.getRechercheN(idSw);

                    if (sw == null || !sw.getComposant().equals("SWITCH")) {
                        System.out.println("⚠  Switch introuvable.");
                        break;
                    }
                    new RoutingTable(sw, graph).display();
                    break;

                // ── Circuit virtuel — deux machines ─────────────────────
                case "5":
                    printMachines(graph);
                    System.out.print("ID machine source      : ");
                    String idM1 = scanner.nextLine().trim().toUpperCase();
                    System.out.print("ID machine destination : ");
                    String idM2 = scanner.nextLine().trim().toUpperCase();

                    Noeud m1 = graph.getRechercheN(idM1);
                    Noeud m2 = graph.getRechercheN(idM2);

                    if (m1 == null || m2 == null) {
                        System.out.println("⚠  Identifiant(s) inconnu(s).");
                        break;
                    }
                    if (!m1.getComposant().equals("MACHINE") ||
                        !m2.getComposant().equals("MACHINE")) {
                        System.out.println("⚠  Veuillez choisir deux MACHINES.");
                        break;
                    }

                    VirtualCircuit vc = new VirtualCircuit(m1, m2, graph);
                    vc.display();

                    // Mise en surbrillance GraphStream si un chemin existe
                    if (!vc.getPath().isEmpty()) {
                        viz.displayShortestPath(vc.getPath(), vc.getTotalCost());
                    }
                    break;

                // ── Quitter ─────────────────────────────────────────────
                case "6":
                    running = false;
                    System.out.println("Au revoir !");
                    break;

                default:
                    System.out.println("Option invalide.");
            }
        }
        scanner.close();
    }

    // ── Helpers d'affichage ──────────────────────────────────────────────

    private static void printNoeuds(Graph graph) {
        System.out.println("\nNoeuds disponibles :");
        for (Noeud n : graph.getNoeudes())
            System.out.printf("  %-5s → %s [%s]%n",
                n.getId(), n.getNomComposant(), n.getComposant());
    }

    private static void printSwitchs(Graph graph) {
        System.out.println("\nSwitchs disponibles :");
        for (Noeud n : graph.getNoeudes())
            if (n.getComposant().equals("SWITCH"))
                System.out.printf("  %-5s → %s%n", n.getId(), n.getNomComposant());
    }

    private static void printMachines(Graph graph) {
        System.out.println("\nMachines disponibles :");
        for (Noeud n : graph.getNoeudes())
            if (n.getComposant().equals("MACHINE"))
                System.out.printf("  %-5s → %s%n", n.getId(), n.getNomComposant());
    }
}