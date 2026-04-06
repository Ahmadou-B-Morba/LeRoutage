package code;

import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;

import java.util.List;

/**
 * Visualisation du réseau avec GraphStream.
 * Utilise les noms complets pour éviter le conflit avec code.Graph.
 *
 * - Noeuds MACHINE  : cercle bleu
 * - Noeuds SWITCH   : carré orange
 * - Source          : vert       (après calcul)
 * - Destination     : rouge      (après calcul)
 * - Intermédiaires  : jaune      (après calcul)
 * - Arêtes chemin   : rouge épais
 */
public class GraphStreamVisualizer {

    private static final String STYLESHEET =
        "graph { fill-color: #1e1e2e; padding: 60px; }" +

        "node { size: 28px; text-size: 13; text-color: white; " +
        "       text-style: bold; text-background-mode: rounded-box; " +
        "       text-background-color: rgba(0,0,0,180); text-padding: 3px; }" +

        "node.machine { fill-color: #4a9eff; shape: circle; stroke-mode: plain; " +
        "               stroke-color: #89c4ff; stroke-width: 2px; }" +

        "node.switch_ { fill-color: #ff8c42; shape: rounded-box; stroke-mode: plain; " +
        "               stroke-color: #ffb380; stroke-width: 2px; }" +

        "node.source  { fill-color: #50fa7b; size: 34px; " +
        "               stroke-mode: plain; stroke-color: #a0ffb8; stroke-width: 3px; }" +

        "node.dest    { fill-color: #ff5555; size: 34px; " +
        "               stroke-mode: plain; stroke-color: #ffaaaa; stroke-width: 3px; }" +

        "node.path    { fill-color: #f1fa8c; size: 30px; " +
        "               stroke-mode: plain; stroke-color: #ffff80; stroke-width: 2px; }" +

        "edge { fill-color: #555577; size: 2px; text-size: 11; text-color: #bbbbcc; " +
        "       text-background-mode: plain; text-background-color: #1e1e2e; text-padding: 2px; }" +

        "edge.path { fill-color: #ff5555; size: 4px; text-color: #ff9999; }";

    // SingleGraph de GraphStream — nommé gsGraph pour ne pas confondre avec code.Graph
    private final SingleGraph gsGraph;

    public GraphStreamVisualizer(code.Graph myGraph) {
        System.setProperty("org.graphstream.ui", "swing");

        gsGraph = new SingleGraph("Réseau");
        gsGraph.setAttribute("ui.stylesheet", STYLESHEET);
        gsGraph.setAttribute("ui.quality");
        gsGraph.setAttribute("ui.antialias");

        // ── Noeuds ───────────────────────────────────────────────────────
        for (Noeud n : myGraph.getNoeudes()) {
            org.graphstream.graph.Node gsNode = gsGraph.addNode(n.getId());
            gsNode.setAttribute("ui.label", n.getNomComposant());
            gsNode.setAttribute("ui.class",
                n.getComposant().equals("MACHINE") ? "machine" : "switch_");
        }

        // ── Arêtes (une seule par liaison bidirectionnelle) ───────────────
        List<Edge> edges = myGraph.getEdges();
        int edgeId = 0;
        for (int i = 0; i < edges.size(); i += 2) {
            Edge e = edges.get(i);
            try {
                org.graphstream.graph.Edge gsEdge = gsGraph.addEdge(
                    "e" + edgeId++,
                    e.getSource().getId(),
                    e.getDestination().getId(),
                    false  // non orienté
                );
                gsEdge.setAttribute("ui.label", String.valueOf(e.getcout()));
            } catch (Exception ex) {
                // arête déjà présente — ignorer
            }
        }
    }

    /** Affiche le réseau complet sans mise en surbrillance. */
    public void displayNetwork() {
        Viewer viewer = gsGraph.display();
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
        System.out.println("[GraphStream] Réseau affiché.");
    }

    /**
     * Met en surbrillance le plus court chemin, puis affiche la fenêtre.
     *
     * @param path      liste de Noeud retournée par Dijkstra.shortestPath()
     * @param totalCost coût total du chemin
     */
    public void displayShortestPath(List<Noeud> path, int totalCost) {
        if (path == null || path.size() < 2) {
            System.out.println("[GraphStream] Aucun chemin à afficher.");
            displayNetwork();
            return;
        }

        // Remettre tous les noeuds à leur style d'origine
        for (org.graphstream.graph.Node n : gsGraph) {
            String originalClass = (String) n.getAttribute("ui.class");
            // on remet machine/switch_ si pas encore modifié
        }

        Noeud src  = path.get(0);
        Noeud dest = path.get(path.size() - 1);

        // Noeuds intermédiaires en jaune
        for (int i = 1; i < path.size() - 1; i++) {
            org.graphstream.graph.Node n = gsGraph.getNode(path.get(i).getId());
            if (n != null) n.setAttribute("ui.class", "path");
        }

        // Source en vert, destination en rouge
        org.graphstream.graph.Node srcNode = gsGraph.getNode(src.getId());
        org.graphstream.graph.Node dstNode = gsGraph.getNode(dest.getId());
        if (srcNode != null) srcNode.setAttribute("ui.class", "source");
        if (dstNode != null) dstNode.setAttribute("ui.class", "dest");

        // Arêtes du chemin en rouge
        for (int i = 0; i < path.size() - 1; i++) {
            String fromId = path.get(i).getId();
            String toId   = path.get(i + 1).getId();
            org.graphstream.graph.Edge gsEdge = findEdge(fromId, toId);
            if (gsEdge != null) gsEdge.setAttribute("ui.class", "path");
        }

        gsGraph.setAttribute("ui.title",
            "Chemin : " + src.getNomComposant() +
            " → " + dest.getNomComposant() +
            "  (coût = " + totalCost + ")");

        Viewer viewer = gsGraph.display();
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);

        System.out.println("[GraphStream] " + formatPath(path) +
                           "  |  Coût = " + totalCost);
    }

    // ── helpers ──────────────────────────────────────────────────────────

    /** Cherche une arête reliant fromId et toId (dans les deux sens). */
    private org.graphstream.graph.Edge findEdge(String fromId, String toId) {
        for (org.graphstream.graph.Edge e : gsGraph.edges().toList()) {
            String s = e.getSourceNode().getId();
            String t = e.getTargetNode().getId();
            if ((s.equals(fromId) && t.equals(toId)) ||
                (s.equals(toId)   && t.equals(fromId))) {
                return e;
            }
        }
        return null;
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