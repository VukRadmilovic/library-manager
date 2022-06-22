package rs.ac.uns.ftn.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rs.ac.uns.ftn.db.ConnectionUtil;
import rs.ac.uns.ftn.model.Posetilac;

public class PosetilacDao implements Dao<Posetilac> {

	@Override
	public Posetilac get(int id) {
		String query = "SELECT * FROM posetilac WHERE posID = ?";
		ResultSet rs = null;
		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				Posetilac posetilac = new Posetilac(id, rs.getString("posIme"), rs.getString("posPrz"), rs.getString("brTel"), rs.getString("email"), rs.getDouble("stanje"));
				rs.close();
				return posetilac;
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
	public List<Posetilac> getAll() {
		String query = "SELECT * FROM posetilac";
		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);
				ResultSet rs = preparedStatement.executeQuery();) {
			List<Posetilac> posetioci = new ArrayList<Posetilac>();
			while (rs.next()) {
				posetioci.add(new Posetilac(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getDouble(6)));
			}
			return posetioci;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void add(Posetilac posetilac) {
		String query = "INSERT INTO Posetilac VALUES(?, ?, ?, ?, ?, ?)";
		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setInt(1, posetilac.getId());
			preparedStatement.setString(2, posetilac.getIme());
			preparedStatement.setString(3, posetilac.getPrezime());
			preparedStatement.setString(4, posetilac.getBrojTelefona());
			preparedStatement.setString(5, posetilac.getEmail());
			preparedStatement.setDouble(6, posetilac.getStanjeNaRacunu());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(Posetilac noviPosetilac) {
		String query = "UPDATE posetilac SET posIme = ?, posPrz = ?, brTel = ?, email = ?, stanje = ? WHERE posID = ?";
		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setString(1, noviPosetilac.getIme());
			preparedStatement.setString(2, noviPosetilac.getPrezime());
			preparedStatement.setString(3, noviPosetilac.getBrojTelefona());
			preparedStatement.setString(4, noviPosetilac.getEmail());
			preparedStatement.setDouble(5, noviPosetilac.getStanjeNaRacunu());
			preparedStatement.setInt(6, noviPosetilac.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(int id) {
		String queryPosetilac = "DELETE FROM posetilac WHERE posID = ?";
		String queryDonacija = "UPDATE DonacijaKnjige SET posID = NULL WHERE posID = ?";
		
		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement preparedStatementPosetilac = connection.prepareStatement(queryPosetilac);
				PreparedStatement preparedStatementDonacija = connection.prepareStatement(queryDonacija)) {
			
			preparedStatementDonacija.setInt(1, id);
			preparedStatementDonacija.executeUpdate();
			
			preparedStatementPosetilac.setInt(1, id);
			preparedStatementPosetilac.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}