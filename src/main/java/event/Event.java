package event;

import java.sql.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import main.Utils;
import request.BodyRequest;
import xml.DateAdapter;

@XmlRootElement(name = "event")
public class Event {

	private long ID = -1;
	private String name = null;
	private String description = null;
	private Date date = null;
	
	public Event() {}
	
	public Event(BodyRequest body) {
		this.ID = body.getEventId();
		this.name = body.getEventName();
		this.description = body.getEventDescription();
		this.date = Utils.stringToDate(body.getEventDate());
	}
	
	public String getName() {
		return name;
	}
	
	@XmlElement( name = "name" )
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	
	@XmlElement( name = "description" )
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getDate() {
		return date;
	}
	
	@XmlElement( name = "date" )
    @XmlJavaTypeAdapter(DateAdapter.class)
	public void setDate(Date date) {
		this.date = date;
	}
	public long getID() {
		return ID;
	}
	
	@XmlAttribute( name = "ID" )
	public void setID(long iD) {
		ID = iD;
	}
	
}
