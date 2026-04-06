package code;

public class Noeud {

	private String Id;
	private String NomComposant;
	private String Composant; 
	
	public Noeud(String id, String nom, String com ) {
		this.Id = id;
		this.NomComposant = nom;
		this.Composant = com;
	}
	
	public String getId() { return Id;}
	public String getNomComposant() {return NomComposant;}
	public String getComposant() {return Composant;}
	
	@Override
	public String toString() {
		return "["+Composant+":"+Id+":"+NomComposant+"]";
	}
}
