package rs.ac.uns.ftn.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rs.ac.uns.ftn.db.ConnectionUtil;
import rs.ac.uns.ftn.model.Autor;
import rs.ac.uns.ftn.model.Knjiga;
import rs.ac.uns.ftn.model.Zanr;

public class KnjigaDao implements Dao<Knjiga> {

	@Override
	public Knjiga get(int id) {
		String queryKnjiga = "SELECT * FROM knjiga WHERE knjigaID = ?";
		String queryZanrovi = "SELECT zanrID FROM zanrKnjige WHERE knjigaID = ?";
		String queryAutori = "SELECT autorID FROM autorKnjige WHERE knjigaID = ?";
		ResultSet rs = null;
		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement preparedStatementKnjiga = connection.prepareStatement(queryKnjiga);
				PreparedStatement preparedStatementZanrovi = connection.prepareStatement(queryZanrovi);
				PreparedStatement preparedStatementAutori = connection.prepareStatement(queryAutori);) {
			List<Zanr> zanrovi = new ArrayList<Zanr>();
			List<Autor> autori = new ArrayList<Autor>();
			
			preparedStatementZanrovi.setInt(1, id);
			rs = preparedStatementZanrovi.executeQuery();
			Dao<Zanr> zanrDao = new ZanrDao();
			
			while (rs.next()) {
				zanrovi.add(zanrDao.get(rs.getInt(1)));
			}
			rs.close();
			
			preparedStatementAutori.setInt(1, id);
			rs = preparedStatementAutori.executeQuery();
			Dao<Autor> autorDao = new AutorDao();
			
			while (rs.next()) {
				autori.add(autorDao.get(rs.getInt(1)));
			}
			rs.close();
			
			preparedStatementKnjiga.setInt(1, id);
			rs = preparedStatementKnjiga.executeQuery();
			
			if (rs.next()) {
				Knjiga knjiga = new Knjiga(id, rs.getString("knjigaNaziv"), rs.getInt("godIzdavanja"), rs.getString("kratakOpis"),
						rs.getInt("brIzdanja"), rs.getInt("brStrana"), autori, zanrovi);
				rs.close();
				return knjiga;
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
	public List<Knjiga> getAll() {
		String query = "SELECT knjigaID FROM knjiga";
		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);
				ResultSet rs = preparedStatement.executeQuery();) {
			List<Knjiga> knjige = new ArrayList<Knjiga>();
			while (rs.next()) {
				knjige.add(get(rs.getInt(1)));
			}
			return knjige;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Dodavanje knjiga uradjeno pomocu transakcija.
	 * */
	@Override
	public void add(Knjiga knjiga) {
		String queryKnjiga = "INSERT INTO Knjiga VALUES(?, ?, ?, ?, ?, ?)";
		String queryZanrovi = "INSERT INTO ZanrKnjige VALUES(?, ?)";
		String queryAutori = "INSERT INTO AutorKnjige VALUES(?, ?)";
		try (Connection connection = ConnectionUtil.getConnection()) {
			connection.setAutoCommit(false);
			
			try (PreparedStatement preparedStatementKnjiga = connection.prepareStatement(queryKnjiga);
					PreparedStatement preparedStatementZanrovi = connection.prepareStatement(queryZanrovi);
					PreparedStatement preparedStatementAutori = connection.prepareStatement(queryAutori);) {
				preparedStatementKnjiga.setInt(1, knjiga.getId());
				preparedStatementKnjiga.setString(2, knjiga.getNaziv());
				preparedStatementKnjiga.setInt(3, knjiga.getGodinaIzdavanja());
				preparedStatementKnjiga.setString(4, knjiga.getKratakOpis());
				preparedStatementKnjiga.setInt(5, knjiga.getBrojIzdanja());
				preparedStatementKnjiga.setInt(6, knjiga.getBrojStrana());
				preparedStatementKnjiga.executeUpdate();
				
				preparedStatementZanrovi.setInt(2, knjiga.getId());
				for (Zanr zanr : knjiga.getZanrovi()) {
					preparedStatementZanrovi.setInt(1, zanr.getId());
					preparedStatementZanrovi.executeUpdate();
				}
				
				preparedStatementAutori.setInt(2, knjiga.getId());
				for (Autor autor : knjiga.getAutori()) {
					preparedStatementAutori.setInt(1, autor.getId());
					preparedStatementAutori.executeUpdate();
				}
			} catch (SQLException e) {
				connection.rollback();
				throw e;
			}
			
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(Knjiga novaKnjiga) {
		delete(novaKnjiga.getId());
		add(novaKnjiga);
	}

	@Override
	public void delete(int id) {
		String queryAutori = "DELETE FROM autorKnjige WHERE knjigaID = ?";
		String queryZanrovi = "DELETE FROM zanrKnjige WHERE knjigaID = ?";
		String queryKnjiga = "DELETE FROM knjiga WHERE knjigaID = ?";
		String queryDonacija = "DELETE FROM DonacijaKnjige WHERE knjigaID = ?";
		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement preparedStatementAutori = connection.prepareStatement(queryAutori);
				PreparedStatement preparedStatementZanrovi = connection.prepareStatement(queryZanrovi);
				PreparedStatement preparedStatementKnjiga = connection.prepareStatement(queryKnjiga);
				PreparedStatement preparedStatementDonacije = connection.prepareStatement(queryDonacija)) {
			preparedStatementAutori.setInt(1, id);
			preparedStatementAutori.executeUpdate();
			
			preparedStatementZanrovi.setInt(1, id);
			preparedStatementZanrovi.executeUpdate();
			
			preparedStatementDonacije.setInt(1, id);
			preparedStatementDonacije.executeUpdate();
			
			preparedStatementKnjiga.setInt(1, id);
			preparedStatementKnjiga.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
