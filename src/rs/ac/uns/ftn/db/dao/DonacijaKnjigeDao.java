package rs.ac.uns.ftn.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rs.ac.uns.ftn.db.ConnectionUtil;
import rs.ac.uns.ftn.model.DonacijaKnjige;
import rs.ac.uns.ftn.model.Knjiga;
import rs.ac.uns.ftn.model.Posetilac;

public class DonacijaKnjigeDao implements Dao<DonacijaKnjige> {

	@Override
	public DonacijaKnjige get(int id) {
		String query = "SELECT * FROM donacijaKnjige WHERE donacijaID = ?";
		ResultSet rs = null;
		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				Dao<Posetilac> posetilacDao = new PosetilacDao();
				Dao<Knjiga> knjigaDao = new KnjigaDao();
				DonacijaKnjige donacija = new DonacijaKnjige(id, rs.getInt("brKnjiga"), rs.getDate("datDoniranja"), posetilacDao.get(rs.getInt("posID")), knjigaDao.get(rs.getInt("knjigaID")));
				rs.close();
				return donacija;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public List<DonacijaKnjige> getAll() {
		String query = "SELECT donacijaID FROM donacijaKnjige";
		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);
				ResultSet rs = preparedStatement.executeQuery();) {
			List<DonacijaKnjige> donacije = new ArrayList<DonacijaKnjige>();
			while (rs.next()) {
				donacije.add(get(rs.getInt(1)));
			}
			return donacije;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void add(DonacijaKnjige donacija) {
		String query = "INSERT INTO DonacijaKnjige VALUES(?, ?, ?, ?, ?)";
		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setInt(1, donacija.getId());
			preparedStatement.setInt(2, donacija.getBrojKnjiga());
			preparedStatement.setDate(3, new java.sql.Date(donacija.getDatumDoniranja().getTime()));
			
			if (donacija.getDonator() == null) {
				preparedStatement.setNull(4, java.sql.Types.INTEGER);
			} else {
				preparedStatement.setInt(4, donacija.getDonator().getId());
			}
			
			if (donacija.getKnjiga() == null) {
				preparedStatement.setNull(5, java.sql.Types.INTEGER);
			} else {
				preparedStatement.setInt(5, donacija.getKnjiga().getId());
			}
			
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(DonacijaKnjige donacija) {
		String query = "UPDATE donacijaKnjige SET brKnjiga = ?, datDoniranja = ?, posID = ?, knjigaID = ? WHERE donacijaID = ?";
		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setInt(1, donacija.getBrojKnjiga());
			preparedStatement.setDate(2, new java.sql.Date(donacija.getDatumDoniranja().getTime()));
			
			if (donacija.getDonator() == null) {
				preparedStatement.setNull(3, java.sql.Types.INTEGER);
			} else {
				preparedStatement.setInt(3, donacija.getDonator().getId());
			}
			
			preparedStatement.setInt(4, donacija.getKnjiga().getId());
			preparedStatement.setInt(5, donacija.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(int id) {
		String query = "DELETE FROM donacijaKnjige WHERE donacijaID = ?";
		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Izvrsava 2. upit u delu C.
	 * Pronalazi sve donacije knjiga povezane sa imenom i prezimenom posetioca
	 * koji ih je donirao kao i nazivom knjige koja je donirana.
	 * */
	public void executeC2() {
		String query = "SELECT donacijaID, brKnjiga, datDoniranja, posIme || ' ' || posPrz, knjigaNaziv"
				+ " FROM posetilac p inner join donacijaKnjige d on p.posID = d.posID inner join knjiga k on k.knjigaID = d.knjigaID";
		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);
				ResultSet rs = preparedStatement.executeQuery();) {
			System.out.printf("%-12s %-16s %-14s %-50s %-50s\n", "ID Donacije", "Broj don. knjiga", "Datum donacije", "Donator", "Knjiga");
			while (rs.next()) {
				int donID = rs.getInt(1);
				int brKnjiga = rs.getInt(2);
				Date datDon = rs.getDate(3);
				String donator = rs.getString(4);
				String knjiga = rs.getString(5);
				System.out.printf("%-12s %-16s %-14s %-50s %-50s\n", donID, brKnjiga, datDon, donator, knjiga);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * <p>Izvrsava 3. upit u delu C.</p>
	 * <p>Pronalazi kolika je ukupna kolicina doniranih knjiga za svaku knjigu koja je ikada bila donirana.</p>
	 * Izostavljaju se one knjige koje su donirane manje od cutoff puta.
	 * @param cutoff broj donacija ispod kojeg se knjige izostavljaju
	 * */
	public void executeC3(int cutoff) {
		String query = "SELECT knjigaID, SUM(brKnjiga) FROM donacijaKnjige GROUP BY knjigaID HAVING SUM(brKnjiga) >= ?";
		ResultSet rs = null;
		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setInt(1, cutoff);
			rs = preparedStatement.executeQuery();
			System.out.printf("%-10s %-50s\n", "ID knjige", "Ukupan broj doniranih knjiga");
			while (rs.next()) {
				System.out.printf("%-10s %-50s\n", rs.getInt(1), rs.getInt(2));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}