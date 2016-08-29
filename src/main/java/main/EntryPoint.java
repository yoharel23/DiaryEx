package main;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import controller.EventManager;
import request.Body;
import request.BodyExport;
import request.BodyRequest;
import request.BodySearch;

@Path("/diary")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EntryPoint {

	@GET
	@Path("getAll")
	public Response getAllEvents() {
		String result;
		try {
			result = EventManager.getAllEvents();
		} catch (Exception ex) {
			return Response.status(400).entity(ex.getLocalizedMessage()).build();
		}
		return Response.ok(result).build();
	}

	@POST
	@Path("insert")
	public Response insertEvent(@Body BodyRequest body) {
		String result;
		try {
			result = EventManager.insertEvent(body);
		} catch (Exception ex) {
			return Response.status(400).entity(ex.getLocalizedMessage()).build();
		}
		return Response.status(200).entity(result).build();
	}
	
	@POST
	@Path("searchByDescription")
	public Response searchEventByDescription(@Body BodySearch body) {
		String result = null;
		try {
			result = EventManager.searchEventByDescription(body);
		} catch (Exception ex) {
			return Response.status(400).entity(ex.getLocalizedMessage()).build();
		}
		return Response.status(200).entity(result).build();
	}
	
	@POST
	@Path("searchByDates")
	public Response searchEventByDates(@Body BodySearch body) {
		String result = null;
		try {
			result = EventManager.searchEventByDates(body);
		} catch (Exception ex) {
			return Response.status(400).entity(ex.getLocalizedMessage()).build();
		}
		return Response.status(200).entity(result).build();
	}
	
	@POST
	@Path("searchByDescriptionAndDates")
	public Response searchEventByDescriptionAndDates(@Body BodySearch body) {
		String result = null;
		try {
			result = EventManager.searchByDescriptionAndDates(body);
		} catch (Exception ex) {
			return Response.status(400).entity(ex.getLocalizedMessage()).build();
		}
		return Response.status(200).entity(result).build();
	}

	@POST
	@Path("update")
	public Response updateEvent(@Body BodyRequest body) {
		String result;
		try {
			result = EventManager.updateEvent(body);
		} catch (Exception ex) {
			return Response.status(400).entity(ex.getLocalizedMessage()).build();
		}
		return Response.status(200).entity(result).build();
	}

	@POST
	@Path("remove")
	public Response removeEvent(@Body BodyRequest body) {
		String result = null;
		try {
			result = EventManager.removeEvent(body);
		} catch (Exception ex) {
			return Response.status(400).entity(ex.getLocalizedMessage()).build();
		}
		return Response.status(200).entity(result).build();
	}

	@POST
	@Path("export")
	public Response exportData(@Body BodyExport body) {
		String result = EventManager.exportData(body.getFileName());
		return Response.status(200).entity(result).build();
	}

	@POST
	@Path("import")
	public Response importData(@Body BodyExport body) {
		String result = null;
		try {
			result = EventManager.importData(body.getFileName());
		} catch (Exception ex) {
			return Response.status(400).entity(ex.getLocalizedMessage()).build();
		}
		return Response.status(200).entity(result).build();
	}
}