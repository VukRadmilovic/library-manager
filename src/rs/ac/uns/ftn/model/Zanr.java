package rs.ac.uns.ftn.model;

public class Zanr {
	private int id;
	private String naziv;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNaziv() {
		return naziv;
	}
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	
	public Zanr(int id, String naziv) {
		super();
		this.id = id;
		this.naziv = naziv;
	}
	
	@Override
	public String toString() {
		return "Zanr [id=" + id + ", naziv=" + naziv + "]";
	}
}
