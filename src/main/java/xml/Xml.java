package xml;

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import event.EventList;

public class Xml {

	public static boolean exportDiary(EventList eventList, String fileName) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(EventList.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(eventList, System.out);
			jaxbMarshaller.marshal(eventList, new File(fileName));
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	public static EventList importDiary(String fileName) {
		JAXBContext jaxbContext;
		EventList eventList = new EventList();
		try {
			jaxbContext = JAXBContext.newInstance(EventList.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			eventList = (EventList) jaxbUnmarshaller.unmarshal(new File(fileName));
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return eventList;

	}


}
//	private static final String ID = "id";
//	private static final String EVENT_NAME = "name";
//	private static final String EVENT_DESCRIPTION = "description";
//	private static final String EVENT_DATE = "date";
//
//	public XmlWriter(){}
//
//	public static Document exportDB(List<Event> events) {
//
//		try {
//			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder dBuilder;
//
//			dBuilder = dbFactory.newDocumentBuilder();
//
//			Document doc = dBuilder.newDocument();
//
//			// root element
//			Element rootElement = doc.createElement("Events");
//			doc.appendChild(rootElement);
//
//			for (Event event : events) {
//				rootElement.appendChild(addChild(doc, event));
//			}
//			return doc;
//
//		} catch (ParserConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		return null;
//	}
//
//	private static Element addChild(Document doc, Event event) {
//		//  event element
//		Element eventElement = doc.createElement("event");
//		eventElement.appendChild(createChildElement(doc, ID, String.valueOf(event.getID())));
//		eventElement.appendChild(createChildElement(doc, EVENT_DATE, String.valueOf(event.getDate())));
//		eventElement.appendChild(createChildElement(doc, EVENT_NAME, String.valueOf(event.getName())));
//		eventElement.appendChild(createChildElement(doc, EVENT_DESCRIPTION, String.valueOf(event.getDescription())));
//		
//		return eventElement;
//	}
//	
//	private static Element createChildElement(Document doc, String tagName, String value) {
//		Element child = doc.createElement(tagName);
//		child.setTextContent(value);
//		
//		return child;
//		
//	}
//	 
//}
