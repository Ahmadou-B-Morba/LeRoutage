package code;

public class Edge {
	
	public Noeud source;
	public Noeud destination;
	public int cout;
	
	public Edge(Noeud src, Noeud dst, int wei) {
		this.source = src;
		this.destination = dst;
		this.cout = wei;
	}
	
	public Noeud getSource() {return source ;}
	public Noeud getDestination() {return destination;}
	public int getcout() {return cout;}
	
	@Override
	public String toString() {
		return source.getNomComposant() +"--("+cout+")-->"+destination.getNomComposant();
	}
}
