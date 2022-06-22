package rs.ac.uns.ftn.model;

public class Posetilac {
	private int id;
	private String ime;
	private String prezime;
	private String brojTelefona;
	private String email;
	private double stanjeNaRacunu;
	
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
	public String getBrojTelefona() {
		return brojTelefona;
	}
	public void setBrojTelefona(String brojTelefona) {
		this.brojTelefona = brojTelefona;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public double getStanjeNaRacunu() {
		return stanjeNaRacunu;
	}
	public void setStanjeNaRacunu(double stanjeNaRacunu) {
		this.stanjeNaRacunu = stanjeNaRacunu;
	}
	
	public Posetilac(int id, String ime, String prezime, String brojTelefona, String email, double stanjeNaRacunu) {
		super();
		this.id = id;
		this.ime = ime;
		this.prezime = prezime;
		this.brojTelefona = brojTelefona;
		this.email = email;
		this.stanjeNaRacunu = stanjeNaRacunu;
	}
	
	@Override
	public String toString() {
		return "Posetilac [id=" + id + ", ime=" + ime + ", prezime=" + prezime + ", brojTelefona=" + brojTelefona
				+ ", email=" + email + ", stanjeNaRacunu=" + stanjeNaRacunu + "]";
	}
}
