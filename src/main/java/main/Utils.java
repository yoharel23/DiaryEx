package main;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Utils {
	private static final Logger LOGGER = Logger.getLogger(Utils.class.getName());

	public static Date stringToDate(String stringDate) {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date eventDate = null;
		try {
			java.util.Date parsedDate = format.parse(stringDate);
			eventDate = new Date(parsedDate.getTime());
		} catch (ParseException ex) {
			LOGGER.log(Level.WARNING, "failed to convert event date", ex);
		}
		return eventDate;
	}
}
