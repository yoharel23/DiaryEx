package databaseLayer;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import databaseLayer.DBHelper;
import event.Event;
import event.SearchEvent;
import main.Utils;

public class DBHelperTest {

	private static final String DB_DRIVER = "org.h2.Driver";
//	private static final String DB_CONNECTION = "jdbc:h2:mem:test";
	private static final String DB_CONNECTION = "jdbc:h2:file:./h2db.db";
	private static final String DB_USER = "";
	private static final String DB_PASSWORD = "";
	Connection dbConnection = null;

	@Before
	public void getDBConnection() {
		String CreateQuery = "CREATE TABLE events(rowid INTEGER, event_date DATETIME, event_name VARCHAR, event_description VARCHAR)";
		PreparedStatement createPreparedStatement;
		try {
			Class.forName(DB_DRIVER);
			dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
			createPreparedStatement = dbConnection.prepareStatement(CreateQuery);
			createPreparedStatement.execute();
			createPreparedStatement.close();
		} catch (SQLException | ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void testPrepareInsertStatementParameterCount() throws SQLException {
		String sqlQuery = DBHelper.insertQueries;
		PreparedStatement statement = dbConnection.prepareStatement(sqlQuery);

		Event event = new Event();
		event.setDate(Utils.stringToDate("12/12/2015"));
		event.setName("yoni");
		event.setDescription("description");

		DBHelper.prepareInsertStatement(statement, event);

		assertTrue(statement.getParameterMetaData().getParameterCount() == 3);
	}
	
	@Test
	public void testPrepareUpdateStatementParameterCount() throws SQLException {
		String sqlQuery = DBHelper.updateQuery;
		PreparedStatement statement = dbConnection.prepareStatement(sqlQuery);

		Event event = new Event();
		event.setID(1);
		event.setName("yoni");
		event.setDate(Utils.stringToDate("12/12/2015"));
		event.setDescription("test");

		DBHelper.prepareUpdateStatement(statement, event);

		assertTrue(statement.getParameterMetaData().getParameterCount() == 4);
	}
	
	@Test
	public void testPrepareSearchStatementParameterCount() throws SQLException {
		String sqlQuery = DBHelper.searchQueries.DATES.getQuery();
		String method = DBHelper.searchQueries.DATES.getMethod();
		PreparedStatement statement = dbConnection.prepareStatement(sqlQuery);

		SearchEvent searchEvent = new SearchEvent();
		searchEvent.setFromDate(Utils.stringToDate("12/10/2013"));
		searchEvent.setToDate(Utils.stringToDate("15/10/2013"));

		DBHelper.prepareSearchStatement(statement, searchEvent, method);

		assertTrue(statement.getParameterMetaData().getParameterCount() == 2);
	}
	
	@Test
	public void testPrepareInsertStatement() throws SQLException {
		String sqlQuery = DBHelper.insertQueries;
		PreparedStatement statement = dbConnection.prepareStatement(sqlQuery);

		Event event = new Event();
		event.setDate(Utils.stringToDate("12/12/2015"));
		event.setName("yoni");
		event.setDescription("description");

		DBHelper.prepareInsertStatement(statement, event);

		assertTrue(statement.toString().contains("{1: 'yoni', 2: 'description', 3: DATE '2015-12-12'}"));
	}
	
	@Test
	public void testPrepareInsertStatementWithoutDescription() throws SQLException {
		String sqlQuery = DBHelper.insertQueries;
		PreparedStatement statement = dbConnection.prepareStatement(sqlQuery);

		Event event = new Event();
		event.setDate(Utils.stringToDate("12/12/2015"));
		event.setName("yoni");

		DBHelper.prepareInsertStatement(statement, event);

		assertTrue(statement.toString().contains("{1: 'yoni', 2: NULL, 3: DATE '2015-12-12'}"));
	}
	
	@Test (expected = SQLException.class)
	public void testPrepareInsertStatementWithoutName() throws SQLException {
		String sqlQuery = DBHelper.insertQueries;
		PreparedStatement statement = dbConnection.prepareStatement(sqlQuery);

		Event event = new Event();
		event.setDate(Utils.stringToDate("12/12/2015"));
		event.setDescription("description");

		DBHelper.prepareInsertStatement(statement, event);
	}
	
	@Test (expected = SQLException.class)
	public void testPrepareInsertStatementWithoutDate() throws SQLException {
		String sqlQuery = DBHelper.insertQueries;
		PreparedStatement statement = dbConnection.prepareStatement(sqlQuery);

		Event event = new Event();
		event.setName("yoni");
		event.setDescription("description");

		DBHelper.prepareInsertStatement(statement, event);
	}
	
	@Test
	public void testPrepareRemoveQuery() throws SQLException {
		String sqlQuery = DBHelper.removeQuery;
		PreparedStatement statement = dbConnection.prepareStatement(sqlQuery);

		Event event = new Event();
		event.setID(2);

		DBHelper.prepareRemoveQuery(statement, event.getID());

		assertTrue(statement.toString().contains("{1: 2}"));
	}
	
	@Test (expected = SQLException.class)
	public void testPrepareRemoveQueryNegative() throws SQLException {
		String sqlQuery = DBHelper.removeQuery;
		PreparedStatement statement = dbConnection.prepareStatement(sqlQuery);

		Event event = new Event();

		DBHelper.prepareRemoveQuery(statement, event.getID());
	}
	
	@Test
	public void testPrepareUpdateStatement() throws SQLException {
		String sqlQuery = DBHelper.updateQuery;
		PreparedStatement statement = dbConnection.prepareStatement(sqlQuery);

		Event event = new Event();
		event.setID(3);
		event.setDate(Utils.stringToDate("12/12/2015"));
		event.setName("yoni");
		event.setDescription("description");

		DBHelper.prepareUpdateStatement(statement, event);

		assertTrue(statement.toString().contains("{1: 'yoni', 2: 'description', 3: DATE '2015-12-12', 4: 3}"));
	}
	
	@Test (expected = SQLException.class)
	public void testPrepareUpdateStatementWithoutID() throws SQLException {
		String sqlQuery = DBHelper.updateQuery;
		PreparedStatement statement = dbConnection.prepareStatement(sqlQuery);

		Event event = new Event();
		event.setDate(Utils.stringToDate("12/12/2015"));
		event.setName("yoni");
		event.setDescription("description");

		DBHelper.prepareUpdateStatement(statement, event);
	}
	
	@Test (expected = SQLException.class)
	public void testPrepareUpdateStatementWithMissingParameter() throws SQLException {
		String sqlQuery = DBHelper.updateQuery;
		PreparedStatement statement = dbConnection.prepareStatement(sqlQuery);

		Event event = new Event();
		event.setID(3);
		event.setDate(Utils.stringToDate("12/12/2015"));
		event.setDescription("description");

		DBHelper.prepareUpdateStatement(statement, event);
	}
	
	@Test
	public void testGetFromResultSet() throws SQLException, ParseException {
		String date = "2011-12-24";
		SimpleDateFormat sqliteDateFormat = new SimpleDateFormat("yyyy-mm-dd");
        Date javaDate = sqliteDateFormat.parse(date);
       
		ResultSet resultSetMock = Mockito.mock(ResultSet.class);
		Mockito.when(resultSetMock.getString("event_name")).thenReturn("test_1");
		Mockito.when(resultSetMock.getString("event_description")).thenReturn("test_desc_1");
		Mockito.when(resultSetMock.getDate("event_date")).thenReturn(new java.sql.Date(javaDate.getTime()));
		Mockito.when(resultSetMock.getLong("rowid")).thenReturn((long) 1);
		
		Event event = DBHelper.getFromResultSet(resultSetMock);
		
		assertEquals("test_1", event.getName());
		assertEquals("test_desc_1", event.getDescription());
		assertEquals(1, event.getID());
		assertEquals(javaDate, event.getDate());
	}
	
	@After
	public void closeDbConnection() {
		try {
			PreparedStatement statement = dbConnection.prepareStatement("DROP TABLE events IF EXISTS");
			statement.execute();
			statement.close();
			dbConnection.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
