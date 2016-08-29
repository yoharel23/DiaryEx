package event;

import java.sql.Date;

import main.Utils;
import request.BodySearch;

public class SearchEvent {

	private String eventDescription;
	private Date fromDate;
	private Date toDate;
	
	public SearchEvent() {}
	
	public SearchEvent(BodySearch body) {
		this.eventDescription = body.getEventDescription();
		this.fromDate = Utils.stringToDate(body.getFromDate());
		this.toDate = Utils.stringToDate(body.getToDate());
	}
	
	public SearchEvent(String eventDesription) {
		this.eventDescription = eventDesription;
	}

	public String getDescription() {
		return eventDescription;
	}

	public void setDescription(String description) {
		this.eventDescription = description;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
}
