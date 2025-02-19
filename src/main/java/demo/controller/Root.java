package demo.controller;

import lebah.rest.api.RestRequest;
import lebah.rest.servlets.Get;
import lebah.rest.servlets.Path;

@Path("/")
public class Root extends RestRequest {
	
	@Get("")
	public void getInfo() {
		
		response.put("message", "This is root class");
	}

}
