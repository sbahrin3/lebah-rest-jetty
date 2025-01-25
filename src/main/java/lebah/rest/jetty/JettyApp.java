package lebah.rest.jetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class JettyApp {
	
	public static void main(String[] args) throws Exception {
        
		JettyApp.runServer(8080, "lebah.demo.controller");
    }
	
	
	public static void runServer(int port, String controllerPath) throws Exception {
		Server server = new Server(port);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.setAttribute("controllerPath", controllerPath);
        context.addServlet(new ServletHolder(new lebah.rest.servlets.RestTemplate()), "/*");
        server.setHandler(context);
        // Start the server
        server.start();
        server.join();
	}

}
