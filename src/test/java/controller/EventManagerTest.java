package controller;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import request.BodySearch;
import controller.EventManager;

public class EventManagerTest {
	
	@Test(expected = Exception.class)
	public void testFindFieldsInclBetweenMissingTo() throws Exception {
		String method = "dates";
		BodySearch body = new BodySearch();
		body.setFromDate("22/12/2013");
		
		EventManager.validate(body, method);
	}
	
	@Test(expected = Exception.class)
	public void testFindFieldsInclBetweenMissingFrom() throws Exception {
		String method = "dates";
		BodySearch body = new BodySearch();
		body.setToDate("13/12/2012");
		
		EventManager.validate(body, method);
	}

	@Test (expected = Exception.class)
	public void testFindFieldsInclBetweenNegative() throws Exception {
		String method = "both";
		BodySearch body = new BodySearch();
		body.setFromDate("12/12/2013");
		body.setToDate("13/12/2012");

		EventManager.validate(body, method);
	}

}
