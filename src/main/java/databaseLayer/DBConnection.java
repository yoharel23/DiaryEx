package databaseLayer;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import event.Event;
import event.EventList;
import event.SearchEvent;
import xml.Xml;

public class DBConnection {

	private static Connection connection = null;
	private PreparedStatement statement = null;
	private static final Logger LOGGER = Logger.getLogger(DBConnection.class.getName());

	public void createDb() throws SQLException {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:diary.sqlite");
			statement = connection.prepareStatement("create table if not exists events(event_date DATETIME, event_name VARCHAR, event_description VARCHAR)");
			statement.setQueryTimeout(30);
			statement.execute();
			statement.close();
		} catch (SQLException ex) {
			LOGGER.log(Level.SEVERE, "failed to create db connection", ex);
		} finally {
			closeConnection();
		}
	}

	public void getConnection() throws SQLException {
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:diary.sqlite");
		}catch (SQLException ex) {
			LOGGER.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
			throw new SQLException("Failed to fetch DB connection.");
		}
	}

	public void closeConnection() throws SQLException {
		try {
		if (!statement.isClosed()) {
			statement.close();
		}
		if(connection != null)
			connection.close();
		} catch (SQLException ex) {
			LOGGER.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
			throw new SQLException("Failed to close DB connection.");
		}
	}

	public List<Event> getAllEvents() throws SQLException {
		String sqlQuery = DBHelper.getAllQuery;
		List<Event> events = new ArrayList<>();
		
		statement = connection.prepareStatement(sqlQuery);
		ResultSet results = statement.executeQuery();
		while (results.next()) {
			Event event = DBHelper.getFromResultSet(results);
			events.add(event);
		}

		return events;
	}

	public void insertEvent(Event event) throws SQLException {
		String sqlQuery = DBHelper.insertQueries;

		statement = connection.prepareStatement(sqlQuery);
		DBHelper.prepareInsertStatement(statement, event);
		statement.executeUpdate();
	}

	public void removeEvent(long eventId) throws SQLException {
		String sqlQuery = DBHelper.removeQuery;
		statement = connection.prepareStatement(sqlQuery);

		DBHelper.prepareRemoveQuery(statement, eventId);

		statement.execute();
	}

	public void exportData(String fileName) throws IOException, SQLException {
		File file = new File(fileName);
		file.createNewFile();
		String sqlQuery = DBHelper.getAllQuery;
		List<Event> events = new ArrayList<>();

		statement = connection.prepareStatement(sqlQuery);
		ResultSet rs = statement.executeQuery() ;
		// loop through the result set
		while (rs.next()) {
			Event event = DBHelper.getFromResultSet(rs);
			events.add(event);
		}

		EventList eventList = new EventList();
		eventList.setEventList(events);
		Xml.exportDiary(eventList, fileName);
	}

	public void importData(String fileName) throws SQLException {
		EventList eventList = Xml.importDiary(fileName);
		//INSERT INTO events (id, event_date, event_name, event_description) VALUES(?,?,?,?)
		String sqlQuery = DBHelper.insertQueries;
		statement= null; 
		statement = connection.prepareStatement(sqlQuery);
		for(Event event : eventList.getEventList()) {

			statement.setString(1,event.getName());
			statement.setString(2, event.getDescription());
			statement.setDate(3,event.getDate());
			statement.addBatch();
		}
		statement.executeBatch();
		statement.close();
	}

	public void updateEvent(Event event) throws SQLException {
		String sqlQuery = DBHelper.updateQuery;
		statement = connection.prepareStatement(sqlQuery);
		DBHelper.prepareUpdateStatement(statement, event);
		statement.executeUpdate();
	}

	public List<Event> searchEventByDescription(SearchEvent searchEvent) throws SQLException {
		String sqlQuery = DBHelper.searchQueries.DESCRIPTION.getQuery();
		String method = DBHelper.searchQueries.DESCRIPTION.getMethod();
		List<Event> events = new ArrayList<>();
		
		statement = connection.prepareStatement(sqlQuery);
		DBHelper.prepareSearchStatement(statement, searchEvent, method);
		ResultSet results = statement.executeQuery();
		while (results.next()) {
			Event event = DBHelper.getFromResultSet(results);
			events.add(event);
		}

		return events;
	}

	public List<Event> searchByDescriptionAndDates(SearchEvent searchEvent) throws SQLException {
		String sqlQuery = DBHelper.searchQueries.BOTH.getQuery();
		String method = DBHelper.searchQueries.BOTH.getMethod();
		List<Event> events = new ArrayList<>();
		
		statement = connection.prepareStatement(sqlQuery);
		DBHelper.prepareSearchStatement(statement, searchEvent, method);
		ResultSet results = statement.executeQuery();
		while (results.next()) {
			Event event = DBHelper.getFromResultSet(results);
			events.add(event);
		}
		return events;
	}

	public List<Event> searchEventByDates(SearchEvent searchEvent) throws SQLException {
		String sqlQuery = DBHelper.searchQueries.DATES.getQuery();
		String method = DBHelper.searchQueries.DATES.getMethod();
		List<Event> events = new ArrayList<>();

		statement = connection.prepareStatement(sqlQuery);
		DBHelper.prepareSearchStatement(statement, searchEvent, method);
		ResultSet results = statement.executeQuery();
		while (results.next()) {
			Event event = DBHelper.getFromResultSet(results);
			events.add(event);
		}
		return events;
	}
}