package code;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.view.Viewer;

import java.util.List;

/**
 * Visualisation du réseau avec GraphStream.
 * - Noeuds MACHINE  : cercle bleu
 * - Noeuds SWITCH   : carré orange
 * - Arêtes normales : gris avec le coût affiché
 * - Arêtes du plus court chemin : rouge/épais
 */
public class GraphStreamVisualizer {

    // ── CSS du graphe ──────────────────────────────────────────────────
    private static final String STYLESHEET =
        "graph { fill-color: #1e1e2e; padding: 60px; }" +

        "node { size: 28px; text-size: 13; text-color: white; " +
        "       text-style: bold; text-background-mode: rounded-box; " +
        "       text-background-color: rgba(0,0,0,180); text-padding: 3px; }" +

        "node.machine { fill-color: #4a9eff; shape: circle; stroke-mode: plain; " +
        "               stroke-color: #89c4ff; stroke-width: 2px; }" +

        "node.switch_  { fill-color: #ff8c42; shape: rounded-box; stroke-mode: plain; " +
        "                stroke-color: #ffb380; stroke-width: 2px; }" +

        "node.source   { fill-color: #50fa7b; size: 34px; stroke-color: #a0ffb8; stroke-width: 3px; }" +
        "node.dest     { fill-color: #ff5555; size: 34px; stroke-color: #ffaaaa; stroke-width: 3px; }" +
        "node.path     { fill-color: #f1fa8c; size: 30px; stroke-color: #ffff80; stroke-width: 2px; }" +

        "edge { fill-color: #555577; size: 2px; text-size: 11; text-color: #bbbbcc; " +
        "       text-background-mode: plain; text-background-color: #1e1e2e; text-padding: 2px; }" +
        "edge.path { fill-color: #ff5555; size: 4px; text-color: #ff9999; }";

    private final Graph gsGraph;

    // ── Constructeur : construit le graphe GraphStream depuis notre Graph ──
    public GraphStreamVisualizer(code.Graph myGraph) {
        System.setProperty("org.graphstream.ui", "swing");

        gsGraph = new SingleGraph("Réseau");
        gsGraph.setAttribute("ui.stylesheet", STYLESHEET);
        gsGraph.setAttribute("ui.quality");
        gsGraph.setAttribute("ui.antialias");

        // Ajouter les noeuds
        for (Noeud n : myGraph.getNoeudes()) {
            org.graphstream.graph.Node gsNode = gsGraph.addNode(n.getId());
            gsNode.setAttribute("ui.label", n.getNomComposant());

            if (n.getComposant().equals("MACHINE")) {
                gsNode.setAttribute("ui.class", "machine");
            } else {
                gsNode.setAttribute("ui.class", "switch_");
            }
        }

        // Ajouter les arêtes (on ne prend qu'une direction pour l'affichage)
        List<Edge> edges = myGraph.getEdges();
        int edgeId = 0;
        for (int i = 0; i < edges.size(); i += 2) {
            Edge e = edges.get(i);
            String id = "e" + edgeId++;
            try {
                org.graphstream.graph.Edge gsEdge =
                    gsGraph.addEdge(id, e.getSource().getId(), e.getDestination().getId(), false);
                gsEdge.setAttribute("ui.label", String.valueOf(e.getcout()));
                gsEdge.setAttribute("cout", e.getcout());
            } catch (Exception ex) {
                // arête déjà présente — ignorer
            }
        }
    }

    /**
     * Affiche le réseau complet sans mise en surbrillance.
     */
    public void displayNetwork() {
        Viewer viewer = gsGraph.display();
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
        System.out.println("[GraphStream] Réseau affiché.");
    }

    /**
     * Met en surbrillance le plus court chemin calculé par Dijkstra,
     * puis affiche la fenêtre.
     *
     * @param path     Liste de noeuds retournée par Dijkstra.shortestPath()
     * @param totalCost Coût total du chemin
     */
    public void displayShortestPath(List<Noeud> path, int totalCost) {
        if (path == null || path.size() < 2) {
            System.out.println("[GraphStream] Aucun chemin à afficher.");
            displayNetwork();
            return;
        }

        Noeud src  = path.get(0);
        Noeud dest = path.get(path.size() - 1);

        // Colorier les noeuds intermédiaires
        for (int i = 1; i < path.size() - 1; i++) {
            org.graphstream.graph.Node n = gsGraph.getNode(path.get(i).getId());
            if (n != null) n.setAttribute("ui.class", "path");
        }

        // Source et destination avec une couleur distincte
        org.graphstream.graph.Node srcNode  = gsGraph.getNode(src.getId());
        org.graphstream.graph.Node dstNode  = gsGraph.getNode(dest.getId());
        if (srcNode != null) srcNode.setAttribute("ui.class", "source");
        if (dstNode != null) dstNode.setAttribute("ui.class", "dest");

        // Colorier les arêtes du chemin
        for (int i = 0; i < path.size() - 1; i++) {
            String from = path.get(i).getId();
            String to   = path.get(i + 1).getId();
            org.graphstream.graph.Edge gsEdge = gsGraph.getEdge(edgeKey(from, to));
            if (gsEdge == null) gsEdge = gsGraph.getEdge(edgeKey(to, from));
            if (gsEdge != null) gsEdge.setAttribute("ui.class", "path");
        }

        // Titre de la fenêtre
        gsGraph.setAttribute("ui.title",
            "Plus court chemin : " + src.getNomComposant() +
            " → " + dest.getNomComposant() +
            "  (coût = " + totalCost + ")");

        Viewer viewer = gsGraph.display();
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);

        System.out.println("[GraphStream] Chemin affiché : " + formatPath(path) +
                           " | Coût total = " + totalCost);
    }

    // ── Helpers ──────────────────────────────────────────────────────────

    private String edgeKey(String from, String to) {
        // Les IDs d'arêtes sont "e0", "e1"… on cherche par noeuds incidents
        for (org.graphstream.graph.Edge e : gsGraph.edges().toList()) {
            if (e.getSourceNode().getId().equals(from) &&
                e.getTargetNode().getId().equals(to)) return e.getId();
            if (e.getSourceNode().getId().equals(to) &&
                e.getTargetNode().getId().equals(from)) return e.getId();
        }
        return "";
    }

    private String formatPath(List<Noeud> path) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < path.size(); i++) {
            sb.append(path.get(i).getNomComposant());
            if (i < path.size() - 1) sb.append(" → ");
        }
        return sb.toString();
    }
}
