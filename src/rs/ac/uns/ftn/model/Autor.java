package rs.ac.uns.ftn.model;

public class Autor {
	private int id;
	private String ime;
	private String prezime;
	private String kratkaBiografija;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	public String getPrezime() {
		return prezime;
	}
	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}
	public String getKratkaBiografija() {
		return kratkaBiografija;
	}
	public void setKratkaBiografija(String kratkaBiografija) {
		this.kratkaBiografija = kratkaBiografija;
	}
	
	public Autor(int id, String ime, String prezime, String kratkaBiografija) {
		super();
		this.id = id;
		this.ime = ime;
		this.prezime = prezime;
		this.kratkaBiografija = kratkaBiografija;
	}
	
	@Override
	public String toString() {
		return "Autor [id=" + id + ", ime=" + ime + ", prezime=" + prezime + ", kratkaBiografija=" + kratkaBiografija
				+ "]";
	}
}
