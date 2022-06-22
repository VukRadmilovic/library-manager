package rs.ac.uns.ftn.db;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

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

public class CommandUtil {
	public static String baseResourcePath = "resources/";
	
	public static void executeSQLFileCommands(String file) {
		try {
			String[] commands = getCommandsFromFile(file);
			executeCommands(commands);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * A method that executes an array of SQL commands;
	 * 
	 * NOTE: Each command is executed in a separate transaction!
	 * 
	 * @param commands
	 * @throws SQLException
	 */
	private static void executeCommands(String... commands) throws SQLException {
		try (Connection connection = ConnectionUtil.getConnection();
				Statement statement = connection.createStatement()) {
			for (String command : commands) {
				statement.execute(command);
			}
		}
	}

	
	/**
	 * A method for extracting SQL commands from an SQL file
	 * 
	 * @param file
	 * @return an array of commands
	 * @throws IOException
	 */
	private static String[] getCommandsFromFile(String file) throws IOException {
		if(baseResourcePath == "") {
			System.out.println("Warning! baseResourcePath not set!");
		}
		
		byte[] fileBytes = Files.readAllBytes(Paths.get(baseResourcePath + file));
		String commandsStr = new String(fileBytes, StandardCharsets.UTF_8);

		String[] commands = commandsStr.split(";");

		String[] trimmedCommands = new String[commands.length];
		for (int i = 0; i < commands.length; i++) {
			trimmedCommands[i] = commands[i].trim();
		}

		return trimmedCommands;
	}
	
	public static void executeTestDataEntry()
	{
		addTestZanrovi();
		addTestAutori();
		addTestKnjige();
		addTestPosetioci();
		addTestDonacije();
	}

	private static void addTestZanrovi() {
		Dao<Zanr> zanrDao = new ZanrDao();
		zanrDao.add(new Zanr(1, "Rat"));
		zanrDao.add(new Zanr(2, "Drama"));
		zanrDao.add(new Zanr(3, "Ljubavni"));
		zanrDao.add(new Zanr(4, "Komedija"));
		zanrDao.add(new Zanr(5, "Horor"));
		zanrDao.add(new Zanr(6, "Triler"));
		zanrDao.add(new Zanr(7, "Fantastika"));
		zanrDao.add(new Zanr(8, "Naucna fantastika"));
		zanrDao.add(new Zanr(9, "Misterija"));
		zanrDao.add(new Zanr(10, "Istorija"));
	}
	
	private static void addTestAutori() {
		Dao<Autor> autorDao = new AutorDao();
		autorDao.add(new Autor(1, "Anton", "Cehov", "Biografija 2"));
		autorDao.add(new Autor(2, "Marsel", "Prust", "Biografija 3"));
		autorDao.add(new Autor(3, "Francis", "Ficdzerald", "Biografija 4"));
		autorDao.add(new Autor(4, "Vilijam", "Sekspir", "Biografija 5"));
		autorDao.add(new Autor(5, "Mark", "Tven", "Biografija 6"));
		autorDao.add(new Autor(6, "Vladimir", "Nabokov", "Biografija 7"));
		autorDao.add(new Autor(7, "Lav", "Tolstoj", "Biografija 8"));
		autorDao.add(new Autor(8, "Gustav", "Flober", "Biografija 9"));
		autorDao.add(new Autor(9, "Ivo", "Andric", "Biografija 10"));
		autorDao.add(new Autor(10, "Dzordz", "Eliot", "Biografija 1"));
	}
	
	private static void addTestKnjige() {
		Dao<Knjiga> knjigaDao = new KnjigaDao();
		Dao<Autor> autorDao = new AutorDao();
		Dao<Zanr> zanrDao = new ZanrDao();
		knjigaDao.add(new Knjiga(1, "Price", 1945, "Opis 2", 3, 350, 
				new ArrayList<Autor>(Arrays.asList(autorDao.get(1), autorDao.get(4))),
				new ArrayList<Zanr>(Arrays.asList(zanrDao.get(6)))));
		knjigaDao.add(new Knjiga(2, "U traganju", 1982, "Opis 3", 5, 170, 
				new ArrayList<Autor>(Arrays.asList(autorDao.get(10), autorDao.get(2))),
				new ArrayList<Zanr>(Arrays.asList(zanrDao.get(3), zanrDao.get(5)))));
		knjigaDao.add(new Knjiga(3, "Gatsbi", 1999, "Opis 4", 4, 235, 
				new ArrayList<Autor>(Arrays.asList(autorDao.get(2))),
				new ArrayList<Zanr>(Arrays.asList(zanrDao.get(3), zanrDao.get(2)))));
		knjigaDao.add(new Knjiga(4, "Hamlet", 2001, "Opis 5", 8, 255, 
				new ArrayList<Autor>(Arrays.asList(autorDao.get(4))),
				new ArrayList<Zanr>(Arrays.asList(zanrDao.get(4)))));
		knjigaDao.add(new Knjiga(5, "Haklberi Fin", 1845, "Opis 6", 9, 150, 
				new ArrayList<Autor>(Arrays.asList(autorDao.get(4), autorDao.get(5))),
				new ArrayList<Zanr>(Arrays.asList(zanrDao.get(10), zanrDao.get(2)))));
		knjigaDao.add(new Knjiga(6, "Rat i mir", 1878, "Opis 7", 1, 550, 
				new ArrayList<Autor>(Arrays.asList(autorDao.get(6), autorDao.get(7))),
				new ArrayList<Zanr>(Arrays.asList(zanrDao.get(10), zanrDao.get(2)))));
		knjigaDao.add(new Knjiga(7, "Gospodja Bovari", 1900, "Opis 8", 1, 50, 
				new ArrayList<Autor>(Arrays.asList(autorDao.get(9), autorDao.get(8))),
				new ArrayList<Zanr>(Arrays.asList(zanrDao.get(10), zanrDao.get(2)))));
		knjigaDao.add(new Knjiga(8, "Ana karenjina", 1870, "Opis 9", 3, 310, 
				new ArrayList<Autor>(Arrays.asList(autorDao.get(10))),
				new ArrayList<Zanr>(Arrays.asList(zanrDao.get(10)))));
		knjigaDao.add(new Knjiga(9, "Prokleta avlija", 1964, "Opis 10", 1, 350, 
				new ArrayList<Autor>(Arrays.asList(autorDao.get(10), autorDao.get(1), autorDao.get(2))),
				new ArrayList<Zanr>(Arrays.asList(zanrDao.get(10), zanrDao.get(1), zanrDao.get(2)))));
		knjigaDao.add(new Knjiga(10, "Midlmarc", 1871, "Opis 1", 1, 250, 
				new ArrayList<Autor>(Arrays.asList(autorDao.get(10), autorDao.get(2))),
				new ArrayList<Zanr>(Arrays.asList(zanrDao.get(10), zanrDao.get(2)))));
	}
	
	private static void addTestPosetioci() {
		Dao<Posetilac> posetilacDao = new PosetilacDao();
		posetilacDao.add(new Posetilac(1, "Milos", "Milosevic", "932-66-789", "milos@gmail.com", 0));
		posetilacDao.add(new Posetilac(2, "Mihajlo", "Mihajlovic", "999-556-333", "mihajlo@gmail.com", 1500));
		posetilacDao.add(new Posetilac(3, "Milana", "Milanic", "123-456-455", "milana@gmail.com", 250));
		posetilacDao.add(new Posetilac(4, "Iva", "Ivic", "123-429-789", "iva@gmail.com", 500));
		posetilacDao.add(new Posetilac(5, "Vanja", "Vanjic", "173-475-329", "vanja@gmail.com", 2500));
		posetilacDao.add(new Posetilac(6, "Karlo", "Karlovic", "132-456-129", "karlo@gmail.com", 5450));
		posetilacDao.add(new Posetilac(7, "Marko", "Markovic", "111-456-749", "marko@gmail.com", 0));
		posetilacDao.add(new Posetilac(8, "Lazar", "Radmanovic", "222-456-389", "lazar@gmail.com", 0));
		posetilacDao.add(new Posetilac(9, "Mina", "Ivanovic", "333-446-799", "mina@gmail.com", 0));
		posetilacDao.add(new Posetilac(10, "Vuk", "Radmilovic", "123-456-789", "email@gmail.com", 500));
	}
	
	private static void addTestDonacije() {
		Dao<DonacijaKnjige> donacijeDao = new DonacijaKnjigeDao();
		Dao<Posetilac> posetilacDao = new PosetilacDao();
		Dao<Knjiga> knjigaDao = new KnjigaDao();
		donacijeDao.add(new DonacijaKnjige(1, 1, 
				new GregorianCalendar(2022, Calendar.MAY, 10).getTime(), 
				null, knjigaDao.get(1)));
		donacijeDao.add(new DonacijaKnjige(2, 5, 
				new GregorianCalendar(2022, Calendar.MAY, 11).getTime(), 
				posetilacDao.get(1), knjigaDao.get(2)));
		donacijeDao.add(new DonacijaKnjige(3, 4, 
				new GregorianCalendar(2022, Calendar.MAY, 12).getTime(), 
				posetilacDao.get(4), knjigaDao.get(3)));
		donacijeDao.add(new DonacijaKnjige(4, 10, 
				new GregorianCalendar(2022, Calendar.MAY, 12).getTime(), 
				posetilacDao.get(9), knjigaDao.get(4)));
		donacijeDao.add(new DonacijaKnjige(5, 21, 
				new GregorianCalendar(2022, Calendar.MAY, 12).getTime(), 
				posetilacDao.get(4), knjigaDao.get(3)));
		donacijeDao.add(new DonacijaKnjige(6, 1, 
				new GregorianCalendar(2022, Calendar.MAY, 14).getTime(), 
				posetilacDao.get(10), knjigaDao.get(2)));
		donacijeDao.add(new DonacijaKnjige(7, 3, 
				new GregorianCalendar(2022, Calendar.JUNE, 10).getTime(), 
				posetilacDao.get(4), knjigaDao.get(7)));
		donacijeDao.add(new DonacijaKnjige(8, 2, 
				new GregorianCalendar(2022, Calendar.JUNE, 10).getTime(), 
				posetilacDao.get(2), knjigaDao.get(9)));
		donacijeDao.add(new DonacijaKnjige(9, 13, 
				new GregorianCalendar(2022, Calendar.JUNE, 12).getTime(), 
				posetilacDao.get(2), knjigaDao.get(8)));
		donacijeDao.add(new DonacijaKnjige(10, 1, 
				new GregorianCalendar(2022, Calendar.JUNE, 12).getTime(), 
				null, knjigaDao.get(6)));
	}
}
