package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;

import databaseLayer.DBConnection;
import event.Event;
import event.SearchEvent;
import main.Utils;
import request.BodyRequest;
import request.BodySearch;

public class EventManager {

	private static final Logger LOGGER = Logger.getLogger(EventManager.class.getName());

	public static String getAllEvents() throws Exception {
		List<Event> eventList = new ArrayList<>();
		DBConnection connection = new DBConnection();
		try {
			connection.getConnection();
			eventList = connection.getAllEvents();
		} catch (SQLException ex) {
			LOGGER.log(Level.SEVERE, "failed to retrieve events", ex);
			throw new Exception("failed to retrieve events");
		}
		finally {
			try {
				connection.closeConnection();
			} catch (SQLException ex) {
				LOGGER.log(Level.SEVERE, "failed to close DB connection.", ex);
				return "failed to close DB connection.";
			}
		}
		return new Gson().toJson(eventList);
	}

	public static String insertEvent(BodyRequest body) throws Exception {
		validateInsert(body);
		Event event = new Event(body);
		DBConnection connection = new DBConnection();
		try {
			connection.getConnection();
			connection.insertEvent(event);
		}
		catch (SQLException ex){
			LOGGER.log(Level.SEVERE, "failed to insert event", ex);
			throw new Exception("failed to insert event");
		}
		finally {
			try {
				connection.closeConnection();
			} catch (SQLException ex) {
				LOGGER.log(Level.SEVERE, "failed to close DB connection.", ex);
				return "failed to close DB connection.";
			}
		}
		return "event saved successfully";
	}


	/**
	 * Update event will get ALL event details - unchanged and changed fields
	 */
	public static String updateEvent(BodyRequest body) throws Exception {
		validateUpdate(body);
		DBConnection connection = new DBConnection();
		Event event = new Event(body);
		try {
			connection.getConnection();
			connection.updateEvent(event);
		} catch (SQLException ex) {
			LOGGER.log(Level.SEVERE, "failed to update events", ex);
			throw new Exception("failed to update events");
		}
		finally {
			try {
				connection.closeConnection();
			} catch (SQLException ex) {
				LOGGER.log(Level.SEVERE, "failed to close DB connection.", ex);
				return "failed to close DB connection.";
			}
		}
		return "event updated successfully";
	}

	public static String searchEventByDescription(BodySearch body) throws Exception {
		List<Event> eventList = new ArrayList<>();
		DBConnection connection = new DBConnection();
		validate(body, "description");
		SearchEvent searchEvent = new SearchEvent(body.getEventDescription());
		try {
			connection.getConnection();
			eventList = connection.searchEventByDescription(searchEvent);
		} catch (SQLException ex) {
			LOGGER.log(Level.SEVERE, "failed to retrieve events", ex);
			throw new Exception("failed to retrieve events");
		}
		finally {
			try {
				connection.closeConnection();
			} catch (SQLException ex) {
				LOGGER.log(Level.SEVERE, "failed to close DB connection.", ex);
				return "failed to close DB connection.";
			}
		}
		return new Gson().toJson(eventList);
	}

	public static String searchEventByDates(BodySearch body) throws Exception {
		List<Event> eventList = new ArrayList<>();
		DBConnection connection = new DBConnection();
		validate(body, "dates");
		SearchEvent searchEvent = new SearchEvent(body);
		try {
			connection.getConnection();
			eventList = connection.searchEventByDates(searchEvent);
		} catch (SQLException ex) {
			LOGGER.log(Level.SEVERE, "failed to retrieve events", ex);
			throw new Exception("failed to retrieve events");
		}
		finally {
			try {
				connection.closeConnection();
			} catch (SQLException ex) {
				LOGGER.log(Level.SEVERE, "failed to close DB connection.", ex);
				return "failed to close DB connection.";
			}
		}
		return new Gson().toJson(eventList);
	}

	public static String searchByDescriptionAndDates(BodySearch body) throws Exception {
		List<Event> eventList = new ArrayList<>();
		DBConnection connection = new DBConnection();
		validate(body, "both");
		SearchEvent searchEvent = new SearchEvent(body);
		try {
			connection.getConnection();
			eventList = connection.searchByDescriptionAndDates(searchEvent);
		} catch (SQLException ex) {
			LOGGER.log(Level.SEVERE, "failed to retrieve events", ex);
			throw new Exception("failed to retrieve events");
		}
		finally {
			try {
				connection.closeConnection();
			} catch (SQLException ex) {
				LOGGER.log(Level.SEVERE, "failed to close DB connection.", ex);
				return "failed to close DB connection.";
			}
		}
		return new Gson().toJson(eventList);
	}

	public static String removeEvent(BodyRequest body) throws Exception {
		DBConnection connection = new DBConnection();
		try {
			connection.getConnection();
			connection.removeEvent(body.getEventId());
		} catch (SQLException ex) {
			LOGGER.log(Level.SEVERE, "failed to insert event", ex);
			throw new Exception("failed to remove event");
		}
		finally {
			try {
				connection.closeConnection();
			} catch (SQLException ex) {
				LOGGER.log(Level.SEVERE, "failed to close DB connection.", ex);
				return "failed to close DB connection.";
			}
		}
		return "event removed successfully";
	}

	public static String exportData(String fileName) {
		DBConnection connection = new DBConnection();
		try {
			connection.getConnection();
			connection.exportData(fileName);
		} catch (SQLException | IOException ex) {
			LOGGER.log(Level.SEVERE, "failed to retrieve events", ex);
			return "failed to export data.";
		}
		finally {
			try {
				connection.closeConnection();
			} catch (SQLException ex) {
				LOGGER.log(Level.SEVERE, "failed to close DB connection.", ex);
				return "failed to close DB connection.";
			}
		}
		return "export events finished successfully.";
	}

	public static String importData(String fileName) throws Exception {
		DBConnection connection = new DBConnection();
		try {
			connection.getConnection();
			connection.importData(fileName);
		} catch (SQLException ex) {
			LOGGER.log(Level.SEVERE, "failed to import event", ex);
			throw new Exception("failed to import data.");
		}
		finally {
			try {
				connection.closeConnection();
			} catch (SQLException ex) {
				LOGGER.log(Level.SEVERE, "failed to close DB connection.", ex);
				return "failed to close DB connection.";
			}
		}
		return "Import events finished successfully";
	}

	protected static void validate(BodySearch body, String method) throws Exception {

		switch(method) {
		case "description":
			if (body.getEventDescription() == null){
				throw new Exception("Description is missing.");
			}
			break;
		case "dates":
			if(body.getFromDate() != null && body.getToDate() == null) {
				throw new Exception("Request invalid : Must be To Date");
			}

			if(body.getFromDate() == null && body.getToDate() != null) {
				throw new Exception("Request invalid : Must be From Date");
			}
			if (Utils.stringToDate(body.getFromDate()).getTime() > Utils.stringToDate(body.getToDate()).getTime()) {
				throw new Exception("Request invalid : From Date must be earlier than To Date");
			}
			break;
		case "both":
			if (body.getEventDescription() == null){
				throw new Exception("Description is missing.");
			}
			if(body.getFromDate() != null && body.getToDate() == null) {
				throw new Exception("Request invalid : Must be To Date");
			}
			if(body.getFromDate() == null && body.getToDate() != null) {
				throw new Exception("Request invalid : Must be From Date");
			}
			if (Utils.stringToDate(body.getFromDate()).getTime() > Utils.stringToDate(body.getToDate()).getTime()) {
				throw new Exception("Request invalid : From Date must be earlier than To Date");
			}
		}
	}

	protected static void validateUpdate(BodyRequest body) throws Exception {
		if(body.getEventId() == -1 || body.getEventName() == null ||
				body.getEventDescription() == null || body.getEventDate() == null) {
			throw new Exception("Event update cancelled: one or more fields are missing.");	
		}
	}

	protected static void validateInsert(BodyRequest body) throws Exception {
		if(body.getEventName() == null || body.getEventDate() == null) {
			throw new Exception("Event insert cancelled: must enter name and date.");	
		}
	}
}
