package databaseLayer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import event.Event;
import event.SearchEvent;

public class DBHelper {

	public static enum searchQueries {
		DESCRIPTION("DESCRIPTION", "SELECT rowid, * FROM events WHERE event_description = ?"),
		DATES("DATES", "SELECT rowid,* FROM events WHERE event_date BETWEEN ? and ?"),
		BOTH("BOTH", "SELECT rowid,* FROM events WHERE event_description = ? AND event_date BETWEEN ? and ?");

		private String query;
		private String method;

		searchQueries(String method, String query) { 
			this.method = method;
			this.query = query; 
		}
		public String getQuery() {
			return this.query;
		}
		public String getMethod() {
			return this.method;
		}
	}

	public static final String insertQueries = "INSERT INTO events (event_name, event_description, event_date) VALUES(?,?,?)";
	public static final String updateQuery = "UPDATE events SET event_name = ?, event_description = ?, event_date = ? WHERE rowid = ?";
	public static final String getAllQuery = "SELECT rowid, * FROM events";
	public static final String removeQuery = "DELETE FROM events WHERE rowid = ?";

	public static void prepareInsertStatement(PreparedStatement statement, Event event) throws SQLException {

		if (event.getName() == null) {
			throw new SQLException("Must enter name.");
		}else {
			statement.setString(1, event.getName());
		}

		if (event.getDescription() == null) {
			statement.setNull(2, java.sql.Types.INTEGER);
		}else {
			statement.setString(2, event.getDescription());
		}

		if (event.getDate() == null) {
			throw new SQLException("Must enter date.");
		}else {
			statement.setDate(3, event.getDate());
		}
	}

	public static void prepareUpdateStatement(PreparedStatement statement, Event event) throws SQLException {

		if (event.getName() == null || event.getDescription() == null || event.getDate() == null || event.getID() == -1) {
			throw new SQLException("Update event was cancelled: one or more fields are missing.");
		}
		statement.setString(1,event.getName());
		statement.setString(2,event.getDescription());
		statement.setDate(3,event.getDate());
		statement.setLong(4, event.getID());
	}

	public static void prepareSearchStatement(PreparedStatement statement, SearchEvent searchEvent, String method) throws SQLException {

		switch (method) { 
		case "DESCRIPTION":
			statement.setString(1, searchEvent.getDescription());
			break;
		case "DATES": 
			statement.setDate(1, searchEvent.getFromDate());
			statement.setDate(2, searchEvent.getToDate());
			break;
		case "BOTH":
			statement.setString(1, searchEvent.getDescription());
			statement.setDate(2,searchEvent.getFromDate());
			statement.setDate(3,searchEvent.getToDate());
			break;
		}
	}


	public static void prepareRemoveQuery(PreparedStatement statement, long eventId) throws SQLException {
		if (eventId == -1) {
			throw new SQLException("Must specify event ID.");
		}
		else {
			statement.setLong(1, eventId);
		}
	}

	public static Event getFromResultSet(ResultSet rs) throws SQLException {
		Event event = new Event();
		event.setID(rs.getLong("rowid"));
		event.setName(rs.getString("event_name"));
		event.setDescription(rs.getString("event_description"));
		event.setDate(rs.getDate("event_date"));
		return event;
	}
}
