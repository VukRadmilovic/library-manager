package rs.ac.uns.ftn.model;

import java.util.Date;

public class DonacijaKnjige {
	private int id;
	private int brojKnjiga;
	private Date datumDoniranja;
	private Posetilac donator;
	private Knjiga knjiga;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getBrojKnjiga() {
		return brojKnjiga;
	}
	public void setBrojKnjiga(int brojKnjiga) {
		this.brojKnjiga = brojKnjiga;
	}
	public Date getDatumDoniranja() {
		return datumDoniranja;
	}
	public void setDatumDoniranja(Date datumDoniranja) {
		this.datumDoniranja = datumDoniranja;
	}
	public Posetilac getDonator() {
		return donator;
	}
	public void setDonator(Posetilac donator) {
		this.donator = donator;
	}
	public Knjiga getKnjiga() {
		return knjiga;
	}
	public void setKnjiga(Knjiga knjiga) {
		this.knjiga = knjiga;
	}
	
	public DonacijaKnjige(int id, int brojKnjiga, Date datumDoniranja, Posetilac donator, Knjiga knjiga) {
		super();
		this.id = id;
		this.brojKnjiga = brojKnjiga;
		this.datumDoniranja = datumDoniranja;
		this.donator = donator;
		this.knjiga = knjiga;
	}
	
	@Override
	public String toString() {
		return "DonacijaKnjige [id=" + id + ", brojKnjiga=" + brojKnjiga + ", datumDoniranja=" + datumDoniranja
				+ ", donator=" + donator + ", knjiga=" + knjiga + "]";
	}
}
