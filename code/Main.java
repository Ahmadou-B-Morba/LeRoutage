package code;
public class Main {
    public static void main(String[] args) {

        // 1. Créer le graphe
        Graph graph = new Graph();

        // -----------------------------------------------
        // 2. Créer les 7 machines
        // -----------------------------------------------
        Noeud pc1 = new Noeud("PC1", "PC_1", "MACHINE");
        Noeud pc2 = new Noeud("PC2", "PC_2", "MACHINE");
        Noeud pc3 = new Noeud("PC3", "PC_3", "MACHINE");
        Noeud pc4 = new Noeud("PC4", "PC_4", "MACHINE");
        Noeud pc5 = new Noeud("PC5", "PC_5", "MACHINE");
        Noeud pc6 = new Noeud("PC6", "PC_6", "MACHINE");
        Noeud pc7 = new Noeud("PC7", "PC_7", "MACHINE");

        // -----------------------------------------------
        // 3. Créer les 5 switchs
        // -----------------------------------------------
        Noeud s1 = new Noeud("S1", "Switch_1", "SWITCH");
        Noeud s2 = new Noeud("S2", "Switch_2", "SWITCH");
        Noeud s3 = new Noeud("S3", "Switch_3", "SWITCH");
        Noeud s4 = new Noeud("S4", "Switch_4", "SWITCH");
        Noeud s5 = new Noeud("S5", "Switch_5", "SWITCH");

        // -----------------------------------------------
        // 4. Ajouter tous les noeuds au graphe
        // -----------------------------------------------
        graph.AjoutNoeud(pc1); graph.AjoutNoeud(pc2); graph.AjoutNoeud(pc3);
        graph.AjoutNoeud(pc4); graph.AjoutNoeud(pc5); graph.AjoutNoeud(pc6);
        graph.AjoutNoeud(pc7);
        graph.AjoutNoeud(s1);  graph.AjoutNoeud(s2);  graph.AjoutNoeud(s3);
        graph.AjoutNoeud(s4);  graph.AjoutNoeud(s5);

        // -----------------------------------------------
        // 5. Créer les liaisons
        // -----------------------------------------------

        // Machines connectées à leurs switchs
        graph.AjoudEdge(pc1, s1, 2);   // PC1 → Switch_1
        graph.AjoudEdge(pc2, s3, 1);   // PC2 → Switch_3
        graph.AjoudEdge(pc3, s5, 4);   // PC3 → Switch_5
        graph.AjoudEdge(pc4, s2, 9);   // PC4 → Switch_2
        graph.AjoudEdge(pc5, s4, 5);   // PC5 → Switch_4
        graph.AjoudEdge(pc6, s5, 7);   // PC6 → Switch_5
        graph.AjoudEdge(pc7, s5, 2);   // PC7 → Switch_5

        // Liaisons entre switchs
        graph.AjoudEdge(s1, s2, 8);    // Switch_1 → Switch_2
        graph.AjoudEdge(s1, s3, 2);    // Switch_1 → Switch_3
        graph.AjoudEdge(s2, s4, 4);    // Switch_2 → Switch_4
        graph.AjoudEdge(s3, s4, 5);    // Switch_3 → Switch_4
        graph.AjoudEdge(s3, s5, 9);    // Switch_3 → Switch_5
        graph.AjoudEdge(s4, s5, 5);    // Switch_4 → Switch_5

        // -----------------------------------------------
        // 6. Afficher le réseau complet
        // -----------------------------------------------
        graph.display();

        // -----------------------------------------------
        // 7. Tester les voisins de chaque switch
        // -----------------------------------------------
        System.out.println("\n=== VOISINS DE CHAQUE SWITCH ===");
        Noeud[] switchs = {s1, s2, s3, s4, s5};
        for (Noeud s : switchs) {
            System.out.println("\nVoisins de " + s.getNomComposant() + " :");
            for (Edge e : graph.getVoisin(s)) {
                System.out.println("  → " + e.getDestination().getNomComposant()
                                 + " (coût: " + e.getcout() + ")");
            }
        }
    }
}
