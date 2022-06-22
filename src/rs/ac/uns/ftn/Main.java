package rs.ac.uns.ftn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import rs.ac.uns.ftn.db.CommandUtil;
import rs.ac.uns.ftn.db.ConnectionUtil;
import rs.ac.uns.ftn.db.dao.AutorDao;
import rs.ac.uns.ftn.db.dao.Dao;
import rs.ac.uns.ftn.db.dao.DonacijaKnjigeDao;
import rs.ac.uns.ftn.db.dao.KnjigaDao;
import rs.ac.uns.ftn.db.dao.PosetilacDao;
import rs.ac.uns.ftn.db.dao.ZanrDao;
import rs.ac.uns.ftn.model.Autor;
import rs.ac.uns.ftn.model.DonacijaKnjige;
import rs.ac.uns.ftn.model.Knjiga;
import rs.ac.uns.ftn.model.Posetilac;
import rs.ac.uns.ftn.model.Zanr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Main {

	private final static int NAZAD = 0;
	private final static int KREIRAJ_TABELE = 1;
	private final static int BRISI_TABELE = 2;
	private final static int UNESI_TEST_PODATKE = 3;
	private final static int UPIS = 4;
	private final static int IZMENA = 5;
	private final static int BRISANJE = 6;
	private final static int PREGLED = 7;
	private final static int C1 = 8;
	private final static int C2 = 9;
	private final static int C3 = 10;
	private final static int C4 = 11;
	private final static int D1 = 12;
	private final static int D2 = 13;
	
	private final static int ZANR = 1;
	private final static int AUTOR = 2;
	private final static int KNJIGA = 3;
	private final static int POSETILAC = 4;
	private final static int DONACIJA = 5;
	
	static final Scanner sc = new Scanner(System.in);
	
	private static void printModels()
	{
		System.out.println("Opcije:");
		System.out.println("0. Nazad");
		System.out.println("1. Zanr");
		System.out.println("2. Autor");
		System.out.println("3. Knjiga");
		System.out.println("4. Posetilac");
		System.out.println("5. Donacija knjige");
		System.out.println("Unesite broj izmedju 0 i 5 da odaberete opciju:");
	}
	
	private static void printOptions()
	{
		System.out.println("Opcije:");
		System.out.println("0. Prekid programa");
		System.out.println("Deo A)");
		System.out.println("1. Kreiranje svih tabela");
		System.out.println("2. Brisanje svih tabela");
		System.out.println("Deo B)");
		System.out.println("3. Automatski unos test podataka");
		System.out.println("4. Upis podataka");
		System.out.println("5. Izmena podataka");
		System.out.println("6. Brisanje podataka");
		System.out.println("7. Pregled podataka");
		System.out.println("Deo C)");
		System.out.println("8. Izvrsi 1. komandu dela C");
		System.out.println("9. Izvrsi 2. komandu dela C");
		System.out.println("10. Izvrsi 3. komandu dela C");
		System.out.println("11. Izvrsi 4. komandu dela C");
		System.out.println("Deo D)");
		System.out.println("12. Prikazi 1. izvestaj dela D");
		System.out.println("13. Prikazi 2. izvestaj dela D");
		System.out.println("Unesite broj izmedju 0 i 13 da odaberete opciju:");
	}
	
	private static void executeOption(int actionChoice)
	{
		try {
			switch (actionChoice)
			{
				case NAZAD: return;
				case KREIRAJ_TABELE: 
					CommandUtil.executeSQLFileCommands("createTables.sql"); 
					System.out.println("Uspesno su kreirane sve tabele.");
					break;
				case BRISI_TABELE: 
					CommandUtil.executeSQLFileCommands("dropTables.sql"); 
					System.out.println("Uspesno su obrisane sve tabele.");
					break;
				case UNESI_TEST_PODATKE:
					CommandUtil.executeTestDataEntry();
					System.out.println("Uspesno su uneti test podaci.");
					break;
				case UPIS:
				case IZMENA:
				case BRISANJE:
				case PREGLED:
					int modelChoice = -1;
					while (modelChoice != 0) {
						try {
							printModels();
							modelChoice = Integer.parseInt(sc.nextLine());
							executeOption(actionChoice, modelChoice);
						} catch (NumberFormatException ex) {
							System.out.println("Uneli ste neispravan broj.");
							continue;
						} catch (Exception ex) {
							ex.printStackTrace();
							continue;
						}
					}
					break;
				case C1:
					AutorDao autorDao = new AutorDao();
					autorDao.executeC1();
					break;
				case C2:
					DonacijaKnjigeDao donacijaDao = new DonacijaKnjigeDao();
					donacijaDao.executeC2();
					break;
				case C3:
					DonacijaKnjigeDao donacijaDao2 = new DonacijaKnjigeDao();
					donacijaDao2.executeC3(5);
					break;
				case C4:
					KnjigaDao knjigaDao = new KnjigaDao(); // add je uradjen transakciono.
					AutorDao autorDao2 = new AutorDao();
					ZanrDao zanrDao = new ZanrDao(); 
					knjigaDao.add(new Knjiga(50, "Price 2", 1946, "Opis 50", 9, 325, 
							new ArrayList<Autor>(Arrays.asList(autorDao2.get(1), autorDao2.get(4))),
							new ArrayList<Zanr>(Arrays.asList(zanrDao.get(6)))));
					System.out.println("Nova knjiga sa ID 50 je uspesno dodata transakciono.");
					System.out.println(knjigaDao.get(50));
					break;
				case D1:
					executeD1();
					break;
				case D2:
					executeD2();
					break;
				default: System.out.println("Morate uneti broj izmedju 0 i 13.");
			}
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	/**
	 * <p>Kreira drugi izvestaj iz dela D.</p>
	 * <p>Za svakog autora nacice koliko je knjiga napisao za svaki zanr 
	 * kao i koliko je knjiga ukupno napisao u koliko razlicitih zanrova.</p>
	 * */
	private static void executeD2() {
		String queryAutori = "SELECT AutorID, AutorIme || ' ' || AutorPrz FROM Autor";
		String queryKnjigeZanra = "SELECT z.zanrNaziv, COUNT(zk.knjigaID) "
				+ "FROM AutorKnjige ak inner join Knjiga k on ak.knjigaID=k.knjigaID "
				+ "inner join ZanrKnjige zk on k.knjigaID = zk.knjigaID "
				+ "inner join Zanr z on z.zanrID = zk.zanrID "
				+ "WHERE ak.autorID = ? GROUP BY z.zanrNaziv";
		ResultSet rsKnjigeZanra = null;
		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement preparedStatementAutori = connection.prepareStatement(queryAutori);
				PreparedStatement preparedStatementKnjigeZanra = connection.prepareStatement(queryKnjigeZanra);
				ResultSet rsAutori = preparedStatementAutori.executeQuery();) {
			System.out.println("Izvestaj o napisanim knjigama razlicitih zanrova autora:");
			
			while (rsAutori.next()) {
				System.out.println("ID, ime i prezime autora: " + rsAutori.getInt(1) + ", " + rsAutori.getString(2));
				
				preparedStatementKnjigeZanra.setInt(1, rsAutori.getInt(1));
				rsKnjigeZanra = preparedStatementKnjigeZanra.executeQuery();
				
				if (rsKnjigeZanra.isBeforeFirst()) {
					System.out.printf("%-50s %-50s\n", "Naziv zanra", "Broj napisanih knjiga u zanru");
				} else {
					System.out.println("Ovaj autor jos nije napisao ni jednu knjigu koju imamo.");
					rsKnjigeZanra.close();
					continue;
				}
				
				int ukupnoKnjiga = 0;
				int ukupnoZanrova = 0;
				
				while (rsKnjigeZanra.next()) {
					int brKnjiga = rsKnjigeZanra.getInt(2);
					ukupnoKnjiga += brKnjiga;
					ukupnoZanrova++;
					System.out.printf("%-50s %-50s\n", rsKnjigeZanra.getString(1), brKnjiga);
				}
				rsKnjigeZanra.close();
				
				System.out.println("Ovaj autor je napisao ukupno " + ukupnoKnjiga + " knjiga.");
				System.out.println("Te knjige se nalaze u " + ukupnoZanrova + " razlicitih zanrova.");
			}
			rsAutori.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (rsKnjigeZanra != null) {
			try {
				rsKnjigeZanra.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * <p>Kreira prvi izvestaj iz dela D.</p>
	 * <p>Za svakog posetioca nacice koliko je puta vrsio pojedine donacije 
	 * odredjenih knjiga i koliko je ukupno tih knjiga donirao.</p>
	 * */
	private static void executeD1() {
		String queryPosetioci = "SELECT posID, posIme || ' ' || posPrz FROM Posetilac";
		String queryDonacije = "SELECT k.knjiganaziv, count(donacijaID), sum(brKnjiga) "
				+ "FROM donacijaKnjige d inner join Knjiga k on d.knjigaID=k.knjigaID "
				+ "WHERE posID = ? GROUP BY posID, k.knjiganaziv";
		ResultSet rsDonacije = null;
		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement preparedStatementPosetioci = connection.prepareStatement(queryPosetioci);
				PreparedStatement preparedStatementDonacije = connection.prepareStatement(queryDonacije);
				ResultSet rsPosetioci = preparedStatementPosetioci.executeQuery();) {
			System.out.println("Izvestaj o izvrsenim donacijama odredjenih knjiga posetioca:");
			
			while (rsPosetioci.next()) {
				System.out.println("ID, ime i prezime posetioca: " + rsPosetioci.getInt(1) + ", " + rsPosetioci.getString(2));
				
				preparedStatementDonacije.setInt(1, rsPosetioci.getInt(1));
				rsDonacije = preparedStatementDonacije.executeQuery();
				
				if (rsDonacije.isBeforeFirst()) {
					System.out.printf("%-50s %-50s %-50s\n", "Naziv knjige", "Ukupan broj pojedinih donacija", "Ukupno doniranih knjiga");
				} else {
					System.out.println("Ovaj posetioc jos nije napravio ni jednu donaciju.");
					rsDonacije.close();
					continue;
				}
				
				int ukupnoDonacija = 0;
				int ukupnoKnjiga = 0;
				
				while (rsDonacije.next()) {
					int brDonacija = rsDonacije.getInt(2);
					int brKnjiga = rsDonacije.getInt(3);
					ukupnoDonacija += brDonacija;
					ukupnoKnjiga += brKnjiga;
					System.out.printf("%-50s %-50s %-50s\n", rsDonacije.getString(1), brDonacija, brKnjiga);
				}
				rsDonacije.close();
				
				System.out.println("Ovaj posetioc je izvrsio " + ukupnoDonacija + " pojedinacnih donacija.");
				System.out.println("Sveukupno je donirao " + ukupnoKnjiga + " knjiga.");
			}
			rsPosetioci.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (rsDonacije != null) {
			try {
				rsDonacije.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private static void executeOption(int actionChoice, int modelChoice) throws NumberFormatException {
		if (modelChoice == 0)
			return;
		
		Dao<Zanr> zanrDao = new ZanrDao();
		Dao<Autor> autorDao = new AutorDao();
		Dao<Knjiga> knjigaDao = new KnjigaDao();
		Dao<Posetilac> posetilacDao = new PosetilacDao();
		Dao<DonacijaKnjige> donacijaDao = new DonacijaKnjigeDao();
		
		switch (actionChoice)
		{
			case PREGLED:
				executePregled(modelChoice, zanrDao, autorDao, knjigaDao, posetilacDao, donacijaDao);
				break;
			case BRISANJE:
				executePregled(modelChoice, zanrDao, autorDao, knjigaDao, posetilacDao, donacijaDao);
				executeBrisanje(modelChoice, zanrDao, autorDao, knjigaDao, posetilacDao, donacijaDao);
				break;
			case UPIS:
				executePregled(modelChoice, zanrDao, autorDao, knjigaDao, posetilacDao, donacijaDao);
				executeUpis(modelChoice, zanrDao, autorDao, knjigaDao, posetilacDao, donacijaDao);
				break;
			case IZMENA:
				executePregled(modelChoice, zanrDao, autorDao, knjigaDao, posetilacDao, donacijaDao);
				executeIzmena(modelChoice, zanrDao, autorDao, knjigaDao, posetilacDao, donacijaDao);
				break;
			default:
				System.out.println("Greska, pogresna akcija odabrana...");
		}
	}

	private static void executeIzmena(int modelChoice, Dao<Zanr> zanrDao, Dao<Autor> autorDao, Dao<Knjiga> knjigaDao,
			Dao<Posetilac> posetilacDao, Dao<DonacijaKnjige> donacijaDao) {
		int id;
		switch (modelChoice)
		{
			case ZANR:
				System.out.println("Unesite ID zanra kojeg zelite da menjate ili -1 za nazad:");
				id = Integer.parseInt(sc.nextLine());
				if (id == -1)
					break;
				Zanr zanr = zanrDao.get(id);
				
				System.out.println("Unesite novi naziv zanra ili -1 ako ga ne menjate:");
				String nazivZanra = sc.nextLine();
				
				zanr.setNaziv(nazivZanra.equals("-1") ? zanr.getNaziv() : nazivZanra);
				zanrDao.update(zanr);
				System.out.println("Uspesno izmenjen zanr.");
				break;
			case AUTOR:
				System.out.println("Unesite ID autora kojeg zelite da menjate ili -1 za nazad:");
				id = Integer.parseInt(sc.nextLine());
				if (id == -1)
					break;
				Autor autor = autorDao.get(id);
				
				System.out.println("Unesite novo ime autora ili -1 ako ga ne menjate:");
				String autorIme = sc.nextLine();
				System.out.println("Unesite novo prezime autora ili -1 ako ga ne menjate:");
				String autorPrezime = sc.nextLine();
				System.out.println("Unesite novu kratku biografiju autora ili -1 ako ga ne menjate:");
				String biografija = sc.nextLine();
				
				autor.setIme(autorIme.equals("-1") ? autor.getIme() : autorIme);
				autor.setPrezime(autorPrezime.equals("-1") ? autor.getPrezime() : autorPrezime);
				autor.setKratkaBiografija(biografija.equals("-1") ? autor.getKratkaBiografija() : biografija);
				
				autorDao.update(autor);
				System.out.println("Uspesno izmenjen autor.");
				break;
			case KNJIGA:
				System.out.println("Unesite ID knjige koje zelite da izmenite ili -1 za nazad:");
				id = Integer.parseInt(sc.nextLine());
				if (id == -1)
					break;
				Knjiga knjiga = knjigaDao.get(id);
				
				System.out.println("Unesite nov naziv knjige ili -1 ako ga ne menjate:");
				String nazivKnjige = sc.nextLine();
				System.out.println("Unesite novu godinu izdavanja knjige ili -1 ako ga ne menjate:");
				int godinaIzdavanja = Integer.parseInt(sc.nextLine());
				System.out.println("Unesite nov kratak opis knjige ili -1 ako ga ne menjate:");
				String kratakOpis = sc.nextLine();
				System.out.println("Unesite nov broj izdanja knjige ili -1 ako ga ne menjate:");
				int brIzdanja = Integer.parseInt(sc.nextLine());
				System.out.println("Unesite nov broj strana knjige ili -1 ako ga ne menjate:");
				int brStrana = Integer.parseInt(sc.nextLine());
				
				System.out.println("Unesite nov broj autora knjige ili -1 ako ne menjate autore:");
				int brAutora = Integer.parseInt(sc.nextLine());
				List<Autor> autori = new ArrayList<Autor>();
				
				for (int i = 0; i < brAutora; i++) {
					if (i < knjiga.getAutori().size()) {
						Autor stariAutor = knjiga.getAutori().get(i);
						System.out.println("Stari " + (i + 1) + ". autor: " + stariAutor.toString());
						System.out.println("Unesite nov ID autora ili -1 ako ga ne menjate:");
						int novID = Integer.parseInt(sc.nextLine());
						if (novID == -1)
							autori.add(stariAutor);
						else
							autori.add(autorDao.get(novID));
					} else {
						System.out.println("Unesite nov ID autora:");
						int novID = Integer.parseInt(sc.nextLine());
						autori.add(autorDao.get(novID));
					}
				}
				
				System.out.println("Unesite nov broj zanrova knjige ili -1 ako ne menjate zanrove:");
				int brZanrova = Integer.parseInt(sc.nextLine());
				List<Zanr> zanrovi = new ArrayList<Zanr>();
				
				for (int i = 0; i < brZanrova; i++) {
					if (i < knjiga.getZanrovi().size()) {
						Zanr stariZanr = knjiga.getZanrovi().get(i);
						System.out.println("Stari " + (i + 1) + ". zanr: " + stariZanr.toString());
						System.out.println("Unesite nov ID zanra ili -1 ako ga ne menjate:");
						int novID = Integer.parseInt(sc.nextLine());
						if (novID == -1)
							zanrovi.add(stariZanr);
						else
							zanrovi.add(zanrDao.get(novID));
					} else {
						System.out.println("Unesite nov ID zanra:");
						int novID = Integer.parseInt(sc.nextLine());
						zanrovi.add(zanrDao.get(novID));
					}
				}
				
				knjiga.setNaziv(nazivKnjige.equals("-1") ? knjiga.getNaziv() : nazivKnjige);
				knjiga.setGodinaIzdavanja(godinaIzdavanja == -1 ? knjiga.getGodinaIzdavanja() : godinaIzdavanja);
				knjiga.setKratakOpis(kratakOpis.equals("-1") ? knjiga.getKratakOpis() : kratakOpis);
				knjiga.setBrojIzdanja(brIzdanja == -1 ? knjiga.getBrojIzdanja() : brIzdanja);
				knjiga.setBrojStrana(brStrana == -1 ? knjiga.getBrojStrana() : brStrana);
				knjiga.setAutori(autori.size() == 0 ? knjiga.getAutori() : autori);
				knjiga.setZanrovi(zanrovi.size() == 0 ? knjiga.getZanrovi() : zanrovi);
				
				knjigaDao.update(knjiga);
				System.out.println("Uspesno izmenjena knjiga.");
				break;
			case POSETILAC:
				System.out.println("Unesite ID posetioca kojeg zelite da izmenite ili -1 za nazad:");
				id = Integer.parseInt(sc.nextLine());
				if (id == -1)
					break;
				Posetilac posetilac = posetilacDao.get(id);
				
				System.out.println("Unesite novo ime posetioca ili -1 ako ga ne menjate:");
				String posetilacIme = sc.nextLine();
				System.out.println("Unesite novo prezime posetioca ili -1 ako ga ne menjate:");
				String posetilacPrezime = sc.nextLine();
				System.out.println("Unesite nov broj telefona posetioca ili -1 ako ga ne menjate:");
				String brojTelefona = sc.nextLine();
				System.out.println("Unesite nov email posetioca ili -1 ako ga ne menjate:");
				String email = sc.nextLine();
				System.out.println("Unesite novo stanje posetioca ili -1 ako ga ne menjate:");
				double stanje = Double.parseDouble(sc.nextLine());
				
				posetilac.setIme(posetilacIme.equals("-1") ? posetilac.getIme() : posetilacIme);
				posetilac.setPrezime(posetilacPrezime.equals("-1") ? posetilac.getPrezime() : posetilacPrezime);
				posetilac.setBrojTelefona(brojTelefona.equals("-1") ? posetilac.getBrojTelefona() : brojTelefona);
				posetilac.setEmail(email.equals("-1") ? posetilac.getEmail() : email);
				posetilac.setStanjeNaRacunu(stanje == -1 ? posetilac.getStanjeNaRacunu() : stanje);
				
				posetilacDao.update(posetilac);
				System.out.println("Uspesno izmenjen posetioc.");
				break;
			case DONACIJA:
				System.out.println("Unesite ID donacije koje zelite da izmenite ili -1 za nazad:");
				id = Integer.parseInt(sc.nextLine());
				if (id == -1)
					break;
				DonacijaKnjige donacija = donacijaDao.get(id);
				
				System.out.println("Unesite nov broj doniranih knjiga ili -1 ako ga ne menjate:");
				int brojKnjiga = Integer.parseInt(sc.nextLine());
				System.out.println("Unesite nov datum doniranja knjiga (po formatu dd/MM/yyyy) ili -1 ako ga ne menjate:");
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				Date datumDoniranja = null;
				while (true) {
					try {
						String s = sc.nextLine();
						if (s.equals("-1"))
							break;
						datumDoniranja = format.parse(s);
						break;
					} catch (ParseException e) {
						System.out.println("Datum mora biti unet po formatu dd/MM/yyyy");
					}
				}
				System.out.println("Unesite ID posetioca koji je donirao knjige, 0 ako je anonimno ili -1 ako ga ne menjate:");
				int idPosetioca = Integer.parseInt(sc.nextLine());
				Posetilac donator = posetilacDao.get(idPosetioca);
				
				System.out.println("Unesite ID knjige koja je donirana ili -1 ako je ne menjate:");
				int idKnjige = Integer.parseInt(sc.nextLine());
				Knjiga doniranaKnjiga = knjigaDao.get(idKnjige);
				
				donacija.setBrojKnjiga(brojKnjiga == -1 ? donacija.getBrojKnjiga() : brojKnjiga);
				donacija.setDatumDoniranja(datumDoniranja == null ? donacija.getDatumDoniranja() : datumDoniranja);
				donacija.setDonator(idPosetioca == -1 ? donacija.getDonator() : donator);
				donacija.setKnjiga(idKnjige == -1 ? donacija.getKnjiga() : doniranaKnjiga);
				
				donacijaDao.update(donacija);
				System.out.println("Uspesno izmenjena donacija.");
				break;
			default:
				System.out.println("Greska, pogresan model odabran...");
		}
	}

	private static void executeUpis(int modelChoice, Dao<Zanr> zanrDao, Dao<Autor> autorDao, Dao<Knjiga> knjigaDao,
			Dao<Posetilac> posetilacDao, Dao<DonacijaKnjige> donacijaDao) throws NumberFormatException {
		int id;
		switch (modelChoice)
		{
			case ZANR:
				System.out.println("Unesite ID novog zanra kojeg zelite da dodate ili -1 za nazad:");
				id = Integer.parseInt(sc.nextLine());
				if (id == -1)
					break;
				System.out.println("Unesite naziv novog zanra:");
				String nazivZanra = sc.nextLine();
				zanrDao.add(new Zanr(id, nazivZanra));
				System.out.println("Uspesno dodat novi zanr.");
				break;
			case AUTOR:
				System.out.println("Unesite ID novog autora kojeg zelite da dodate ili -1 za nazad:");
				id = Integer.parseInt(sc.nextLine());
				if (id == -1)
					break;
				System.out.println("Unesite ime novog autora:");
				String autorIme = sc.nextLine();
				System.out.println("Unesite prezime novog autora:");
				String autorPrezime = sc.nextLine();
				System.out.println("Unesite kratku biografiju novog autora:");
				String biografija = sc.nextLine();
				autorDao.add(new Autor(id, autorIme, autorPrezime, biografija));
				System.out.println("Uspesno dodat nov autor.");
				break;
			case KNJIGA:
				System.out.println("Unesite ID nove knjige koje zelite da dodate ili -1 za nazad:");
				id = Integer.parseInt(sc.nextLine());
				if (id == -1)
					break;
				
				System.out.println("Unesite naziv nove knjige:");
				String nazivKnjige = sc.nextLine();
				System.out.println("Unesite godinu izdavanja knjige:");
				int godinaIzdavanja = Integer.parseInt(sc.nextLine());
				System.out.println("Unesite kratak opis knjige:");
				String kratakOpis = sc.nextLine();
				System.out.println("Unesite broj izdanja knjige:");
				int brIzdanja = Integer.parseInt(sc.nextLine());
				System.out.println("Unesite broj strana knjige:");
				int brStrana = Integer.parseInt(sc.nextLine());
				
				System.out.println("Unesite broj autora knjige:");
				int brAutora = Integer.parseInt(sc.nextLine());
				List<Autor> autori = new ArrayList<Autor>();
				
				for (int i = 0; i < brAutora; i++) {
					System.out.println("Unesite ID autora:");
					autori.add(autorDao.get(Integer.parseInt(sc.nextLine())));
				}
				
				System.out.println("Unesite broj zanrova knjige:");
				int brZanrova = Integer.parseInt(sc.nextLine());
				List<Zanr> zanrovi = new ArrayList<Zanr>();
				
				for (int i = 0; i < brZanrova; i++) {
					System.out.println("Unesite ID zanra:");
					zanrovi.add(zanrDao.get(Integer.parseInt(sc.nextLine())));
				}
				
				knjigaDao.add(new Knjiga(id, nazivKnjige, godinaIzdavanja, kratakOpis, brIzdanja, brStrana, autori, zanrovi));
				System.out.println("Uspesno dodata nova knjiga.");
				break;
			case POSETILAC:
				System.out.println("Unesite ID novog posetioca kojeg zelite da dodate ili -1 za nazad:");
				id = Integer.parseInt(sc.nextLine());
				if (id == -1)
					break;
				System.out.println("Unesite ime novog posetioca:");
				String posetilacIme = sc.nextLine();
				System.out.println("Unesite prezime novog posetioca:");
				String posetilacPrezime = sc.nextLine();
				System.out.println("Unesite broj telefona novog posetioca:");
				String brojTelefona = sc.nextLine();
				System.out.println("Unesite email novog posetioca:");
				String email = sc.nextLine();
				System.out.println("Unesite stanje novog posetioca:");
				double stanje = Double.parseDouble(sc.nextLine());
				posetilacDao.add(new Posetilac(id, posetilacIme, posetilacPrezime, brojTelefona, email, stanje));
				System.out.println("Uspesno dodat nov posetioc.");
				break;
			case DONACIJA:
				System.out.println("Unesite ID nove donacije koje zelite da dodate ili -1 za nazad:");
				id = Integer.parseInt(sc.nextLine());
				if (id == -1)
					break;

				System.out.println("Unesite broj doniranih knjiga:");
				int brojKnjiga = Integer.parseInt(sc.nextLine());
				System.out.println("Unesite datum doniranja knjiga: (po formatu dd/MM/yyyy)");
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				Date datumDoniranja = null;
				while (true) {
					try {
						datumDoniranja = format.parse(sc.nextLine());
						break;
					} catch (ParseException e) {
						System.out.println("Datum mora biti unet po formatu dd/MM/yyyy");
					}
				}
				System.out.println("Unesite ID posetioca koji je donirao knjige ili -1 ako je anonimno:");
				int idPosetioca = Integer.parseInt(sc.nextLine());
				Posetilac posetilac = posetilacDao.get(idPosetioca);
				
				System.out.println("Unesite ID knjige koja je donirana:");
				int idKnjige = Integer.parseInt(sc.nextLine());
				Knjiga knjiga = knjigaDao.get(idKnjige);
				
				while (knjiga == null) {
					System.out.println("Greska, uneta knjiga ne postoji.");
					System.out.println("Unesite ID knjige koja je donirana:");
					idKnjige = Integer.parseInt(sc.nextLine());
					knjiga = knjigaDao.get(idKnjige);
				}
				
				donacijaDao.add(new DonacijaKnjige(id, brojKnjiga, datumDoniranja, posetilac, knjiga));
				System.out.println("Uspesno dodata nova donacija.");
				break;
			default:
				System.out.println("Greska, pogresan model odabran...");
		}
	}

	private static void executeBrisanje(int modelChoice, Dao<Zanr> zanrDao, Dao<Autor> autorDao, Dao<Knjiga> knjigaDao,
			Dao<Posetilac> posetilacDao, Dao<DonacijaKnjige> donacijaDao) throws NumberFormatException {
		int id;
		switch (modelChoice)
		{
			case ZANR:
				System.out.println("Unesite ID zanra kojeg zelite da obrisete ili -1 za nazad:");
				id = Integer.parseInt(sc.nextLine());
				if (id == -1)
					break;
				zanrDao.delete(id);
				System.out.println("Uspesno obrisano.");
				break;
			case AUTOR:
				System.out.println("Unesite ID autora kojeg zelite da obrisete ili -1 za nazad:");
				id = Integer.parseInt(sc.nextLine());
				if (id == -1)
					break;
				autorDao.delete(id);
				System.out.println("Uspesno obrisano.");
				break;
			case KNJIGA:
				System.out.println("Unesite ID knjige koje zelite da obrisete ili -1 za nazad:");
				id = Integer.parseInt(sc.nextLine());
				if (id == -1)
					break;
				knjigaDao.delete(id);
				System.out.println("Uspesno obrisano.");
				break;
			case POSETILAC:
				System.out.println("Unesite ID posetioca kojeg zelite da obrisete ili -1 za nazad:");
				id = Integer.parseInt(sc.nextLine());
				if (id == -1)
					break;
				posetilacDao.delete(id);
				System.out.println("Uspesno obrisano.");
				break;
			case DONACIJA:
				System.out.println("Unesite ID donacije koje zelite da obrisete ili -1 za nazad:");
				id = Integer.parseInt(sc.nextLine());
				if (id == -1)
					break;
				donacijaDao.delete(id);
				System.out.println("Uspesno obrisano.");
				break;
			default:
				System.out.println("Greska, pogresan model odabran...");
		}
	}

	private static void executePregled(int modelChoice, Dao<Zanr> zanrDao, Dao<Autor> autorDao, Dao<Knjiga> knjigaDao,
			Dao<Posetilac> posetilacDao, Dao<DonacijaKnjige> donacijaDao) {
		switch (modelChoice)
		{
			case ZANR:
				for (Zanr zanr : zanrDao.getAll()) {
					System.out.println(zanr.toString());
				}
				break;
			case AUTOR:
				for (Autor autor : autorDao.getAll()) {
					System.out.println(autor.toString());
				}
				break;
			case KNJIGA:
				for (Knjiga knjiga : knjigaDao.getAll()) {
					System.out.println(knjiga.toString());
				}
				break;
			case POSETILAC:
				for (Posetilac posetilac : posetilacDao.getAll()) {
					System.out.println(posetilac.toString());
				}
				break;
			case DONACIJA:
				for (DonacijaKnjige donacija : donacijaDao.getAll()) {
					System.out.println(donacija.toString());
				}
				break;
			default:
				System.out.println("Greska, pogresan model odabran...");
		}
	}

	public static void main(String[] args) {
		int option = -1;
		while (option != 0) {
			try {
				printOptions();
				option = Integer.parseInt(sc.nextLine());
				executeOption(option);
			} catch (NumberFormatException ex) {
				System.out.println("Uneli ste neispravan broj.");
				continue;
			} catch (Exception ex) {
				ex.printStackTrace();
				continue;
			}
		}
		sc.close();
	}
}