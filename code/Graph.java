package code;

import java.util.ArrayList;
import java.util.List;

public class Graph {
	
	private List<Noeud> Noeuds;
	private List<Edge> Edges;
	
	public Graph() {
		this.Noeuds = new ArrayList<>();
		this.Edges = new ArrayList<>();		
	}
	
	public void AjoutNoeud(Noeud noeud) {
		Noeuds.add(noeud);
	}
	
	public void AjoudEdge(Noeud src, Noeud dst, int cout) {
		Edges.add(new Edge(src, dst, cout));
		Edges.add(new Edge(dst, src, cout));
	}

	//Trouver les voisins d'un noeud c'est a dire ceux qui lui sont connectés
	public List<Edge> getVoisin(Noeud noeud){
		List<Edge> voisins = new ArrayList<>();
		for(Edge edge : Edges) {
			if(edge.getSource().equals(noeud))
			voisins.add(edge);
		}
		return voisins;
	}
	
	//Recherche d'un noued par son Id
	public Noeud getRechercheN(String id) {
		for (Noeud noeud : Noeuds) {
			if(noeud.getId().equals(id)) {
				return noeud;
			}
		}
		return null;
	}
	
	//Afficher le reseau
	public void display() {
		System.out.println("==Réseau==");
		System.out.println("Noeuds :");
		for(Noeud node : Noeuds) {
			System.out.println(" "+node);
		}
		
		System.out.println("==LIASON==");
		System.out.println("Edges :");
		for(int i = 0; i< Edges.size(); i += 2) {
			System.out.println(" "+Edges.get(i));
		}
	}
	
	// Getters
    public List<Noeud> getNoeudes() { return Noeuds; }
    public List<Edge> getEdges() { return Edges; }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
