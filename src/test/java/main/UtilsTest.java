package main;

import static org.junit.Assert.*;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.junit.Test;

public class UtilsTest {

	@Test
	public void testStringToDate() {
		String dateString = "23/12/1981";
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date date = Utils.stringToDate(dateString);
		assertTrue(format.format(date.getTime()).equals(dateString));
	}

}
