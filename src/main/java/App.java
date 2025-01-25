import lebah.rest.jetty.JettyApp;

public class App {
	
	public static void main(String[] args) throws Exception {
		JettyApp.runServer(8080, "demo.controller");
	}

}
