package rs.ac.uns.ftn.model;

import java.util.List;

public class Knjiga {
	private int id;
	private String naziv;
	private int godinaIzdavanja;
	private String kratakOpis;
	private int brojIzdanja;
	private int brojStrana;
	private List<Autor> autori;
	private List<Zanr> zanrovi;
	
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
	public int getGodinaIzdavanja() {
		return godinaIzdavanja;
	}
	public void setGodinaIzdavanja(int godinaIzdavanja) {
		this.godinaIzdavanja = godinaIzdavanja;
	}
	public String getKratakOpis() {
		return kratakOpis;
	}
	public void setKratakOpis(String kratakOpis) {
		this.kratakOpis = kratakOpis;
	}
	public int getBrojIzdanja() {
		return brojIzdanja;
	}
	public void setBrojIzdanja(int brojIzdanja) {
		this.brojIzdanja = brojIzdanja;
	}
	public int getBrojStrana() {
		return brojStrana;
	}
	public void setBrojStrana(int brojStrana) {
		this.brojStrana = brojStrana;
	}
	public List<Autor> getAutori() {
		return autori;
	}
	public void setAutori(List<Autor> autori) {
		this.autori = autori;
	}
	public List<Zanr> getZanrovi() {
		return zanrovi;
	}
	public void setZanrovi(List<Zanr> zanrovi) {
		this.zanrovi = zanrovi;
	}
	
	public Knjiga(int id, String naziv, int godinaIzdavanja, String kratakOpis, int brojIzdanja, int brojStrana,
			List<Autor> autori, List<Zanr> zanrovi) {
		super();
		this.id = id;
		this.naziv = naziv;
		this.godinaIzdavanja = godinaIzdavanja;
		this.kratakOpis = kratakOpis;
		this.brojIzdanja = brojIzdanja;
		this.brojStrana = brojStrana;
		this.autori = autori;
		this.zanrovi = zanrovi;
	}
	
	@Override
	public String toString() {
		return "Knjiga [id=" + id + ", naziv=" + naziv + ", godinaIzdavanja=" + godinaIzdavanja + ", kratakOpis="
				+ kratakOpis + ", brojIzdanja=" + brojIzdanja + ", brojStrana=" + brojStrana + ", autori=" + autori
				+ ", zanrovi=" + zanrovi + "]";
	}
}
