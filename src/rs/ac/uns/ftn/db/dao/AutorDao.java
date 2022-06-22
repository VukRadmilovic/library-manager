package rs.ac.uns.ftn.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rs.ac.uns.ftn.db.ConnectionUtil;
import rs.ac.uns.ftn.model.Autor;

public class AutorDao implements Dao<Autor> {
	
	@Override
	public Autor get(int id) {
		String query = "SELECT * FROM autor WHERE autorID = ?";
		ResultSet rs = null;
		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setInt(1, id);
			rs = preparedStatement.executeQuery();
			if (rs.next()) {
				Autor autor = new Autor(id, rs.getString("autorIme"), rs.getString("autorPrz"), rs.getString("kratkaBio"));
				rs.close();
				return autor;
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
	public List<Autor> getAll() {
		String query = "SELECT * FROM autor";
		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);
				ResultSet rs = preparedStatement.executeQuery();) {
			List<Autor> autori = new ArrayList<Autor>();
			while (rs.next()) {
				autori.add(new Autor(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4)));
			}
			return autori;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void add(Autor autor) {
		String query = "INSERT INTO Autor VALUES(?, ?, ?, ?)";
		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setInt(1, autor.getId());
			preparedStatement.setString(2, autor.getIme());
			preparedStatement.setString(3, autor.getPrezime());
			preparedStatement.setString(4, autor.getKratkaBiografija());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(Autor noviAutor) {
		String query = "UPDATE autor SET autorIme = ?, autorPrz = ?, kratkaBio = ? WHERE autorID = ?";
		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);) {
			preparedStatement.setString(1, noviAutor.getIme());
			preparedStatement.setString(2, noviAutor.getPrezime());
			preparedStatement.setString(3, noviAutor.getKratkaBiografija());
			preparedStatement.setInt(4, noviAutor.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(int id) {
		String queryAutor = "DELETE FROM autor WHERE autorID = ?";
		String queryAutorKnjige = "DELETE FROM autorKnjige WHERE autorID = ?";
		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement preparedStatementAutor = connection.prepareStatement(queryAutor);
				PreparedStatement preparedStatementAutorKnjige = connection.prepareStatement(queryAutorKnjige);) {
			preparedStatementAutorKnjige.setInt(1, id);
			preparedStatementAutorKnjige.executeUpdate();
			
			preparedStatementAutor.setInt(1, id);
			preparedStatementAutor.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Izvrsava 1. upit u delu C.
	 * Pronalazi sve autore cije ime ima drugo slovo a, sortirane po imenu u opadajucem redosledu.
	 * */
	public void executeC1() {
		String query = "SELECT * FROM Autor WHERE AutorIme LIKE '_a%' ORDER BY AutorIme desc";
		try (Connection connection = ConnectionUtil.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query);
				ResultSet rs = preparedStatement.executeQuery();) {
			while (rs.next()) {
				Autor autor = new Autor(rs.getInt("autorID"), rs.getString("autorIme"), rs.getString("autorPrz"), rs.getString("kratkaBio"));
				System.out.println(autor);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
