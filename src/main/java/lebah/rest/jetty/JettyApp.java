package lebah.rest.jetty;

import java.io.File;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import lebah.rest.servlets.Path;
import lebah.rest.servlets.RestTemplate;


public class JettyApp {
	
	public static Map<String, String> controllersMap = new HashMap<>();

	public static void main(String[] args) throws Exception {

		JettyApp.runServer(8080, "qard.controller");
	}


	public static void runServer(int port, String controllerPath) throws Exception {
		
		//Scanning the controllers
		controllersMap = findControllers(controllerPath);

		System.out.println("Lebah REST API, 2025");

		Server server = new Server(port);

		System.out.println("Starting Jetty version " + Server.getVersion());

		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		context.setAttribute("controllerPath", controllerPath);

		context.addServlet(new ServletHolder(new RestTemplate()), "/*");
		server.setHandler(context);

		// Start the server
		server.start();
		System.out.println("Server started at port " + port);
		System.out.println("Controller Path is " + controllerPath);
		server.join();

	}
	
	public static Map<String, String> findControllers(String controllerPath) throws Exception {
		Map<String, String> annotatedClasses = new HashMap<>();
		String path = controllerPath.replace('.', '/');
		Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(path);

		while (resources.hasMoreElements()) {
			File directory = new File(resources.nextElement().getFile());
			if (directory.exists()) {
				for (String file : directory.list()) {
					if (file.endsWith(".class")) {
						String className = controllerPath + '.' + file.substring(0, file.length() - 6);
						Class<?> clazz = Class.forName(className);
						if (clazz.isAnnotationPresent(Path.class)) {
							Path p = clazz.getAnnotation(Path.class);
							String value = p.value() != null && !"".equals(p.value()) ? p.value().substring(1) : "";
							annotatedClasses.put(value, clazz.getName());
						}
					}
				}
			}
		}
		return annotatedClasses;
	}

}
