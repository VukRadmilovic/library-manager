package rs.ac.uns.ftn.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rs.ac.uns.ftn.db.ConnectionUtil;
import rs.ac.uns.ftn.model.Zanr;

public class ZanrDao implements Dao<Zanr> {

	@Override
	public Zanr get(int id) {
		String query = "SELECT * FROM zanr WHERE zanrID = ?";
		ResultSet rs = null;
		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				Zanr zanr = new Zanr(id, rs.getString("zanrNaziv"));
				rs.close();
				return zanr;
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
	public List<Zanr> getAll() {
		String query = "SELECT * FROM zanr";
		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);
				ResultSet rs = preparedStatement.executeQuery();) {
			List<Zanr> zanrovi = new ArrayList<Zanr>();
			while (rs.next()) {
				zanrovi.add(new Zanr(rs.getInt(1), rs.getString(2)));
			}
			return zanrovi;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void add(Zanr zanr) {
		String query = "INSERT INTO Zanr VALUES(?, ?)";
		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setInt(1, zanr.getId());
			preparedStatement.setString(2, zanr.getNaziv());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(Zanr noviZanr) {
		String query = "UPDATE Zanr SET ZanrNaziv = ? WHERE ZanrID = ?";
		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setString(1, noviZanr.getNaziv());
			preparedStatement.setInt(2, noviZanr.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(int id) {
		String queryZanr = "DELETE FROM Zanr WHERE zanrID = ?";
		String queryZanrKnjige = "DELETE FROM ZanrKnjige WHERE zanrID = ?";
		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement preparedStatementZanr = connection.prepareStatement(queryZanr);
				PreparedStatement preparedStatementZanrKnjige = connection.prepareStatement(queryZanrKnjige);) {
			preparedStatementZanrKnjige.setInt(1, id);
			preparedStatementZanrKnjige.executeUpdate();
			
			preparedStatementZanr.setInt(1, id);
			preparedStatementZanr.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}