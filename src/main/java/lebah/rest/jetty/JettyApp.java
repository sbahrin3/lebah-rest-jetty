package lebah.rest.jetty;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import demo.metrics.PrometheusMetricsServer;
import demo.services.GraphQLDataFetcherProvider;
import lebah.db.entity.Persistence;
import lebah.rest.servlets.GraphQL;
import lebah.rest.servlets.Path;
import lebah.rest.servlets.RestTemplate;

/**
 * 
 * @author shamsulbahrin
 * @since 24 Jan 2025
 */

public class JettyApp {
	
	public static Map<String, String> controllersMap = new HashMap<>();
	public static List<GraphQLDataFetcherProvider> graphQLProviders = new ArrayList<>();

	public static void main(String[] args) throws Exception {
		PrometheusMetricsServer.start();
		JettyApp.runServer(8080, "demo.controller", "demo.graphql");
	}

	public static void runServer(int port, String... controllerPath) throws Exception {
		//Scanning the controllers
		controllersMap = findControllers(controllerPath[0]);
		//Scanning the graphQLProviders
		graphQLProviders = findGraphQLProviders(controllerPath[1]);

		System.out.println("LeBAH REST/GraphQL - 2025");
		Server server = new Server(port);
		System.out.println("Starting Jetty version " + Server.getVersion());

		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		context.addServlet(new ServletHolder(new RestTemplate()), "/*");		
		server.setHandler(context);

		// Start the server
		server.start();
		System.out.println("Server started at port " + port);
		System.out.println("REST Controller Path is " + controllerPath[0]);
		System.out.println("GraphQL Provider Path is " + controllerPath[1]);
		
		//Initialize Database
		System.out.println("Initializing Database.");
		Persistence.db();
		System.out.println("Database Initialized.");
		System.out.println("LeBAH REST/GraphQL Server is READY.");
		
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
							String value = p.value() != null && !"".equals(p.value()) ? p.value().startsWith("/") ? p.value().substring(1) : p.value() : "";
							annotatedClasses.put(value, clazz.getName());
						}
					}
				}
			}
		}
		return annotatedClasses;
	}
	
	public static List<GraphQLDataFetcherProvider> findGraphQLProviders(String graphQLProviderPath) throws Exception {
		List<GraphQLDataFetcherProvider> providers = new ArrayList<>();
		String path = graphQLProviderPath.replace('.', '/');
		Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(path);
		while (resources.hasMoreElements()) {
			File directory = new File(resources.nextElement().getFile());
			if (directory.exists()) {
				for (String file : directory.list()) {
					if (file.endsWith(".class")) {
						String className = graphQLProviderPath + '.' + file.substring(0, file.length() - 6);
						Class<?> clazz = Class.forName(className);
						if (clazz.isAnnotationPresent(GraphQL.class)) {
							Object object = Class.forName(className)
									.getDeclaredConstructor()
									.newInstance();
							if ( object instanceof GraphQLDataFetcherProvider ) {
								providers.add((GraphQLDataFetcherProvider) object);
							}
						}
					}
				}
			}
		}
		return providers;
	}

}
