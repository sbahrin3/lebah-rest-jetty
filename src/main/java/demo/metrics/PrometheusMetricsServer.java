package demo.metrics;

import io.prometheus.client.exporter.HTTPServer;
import io.prometheus.client.hotspot.DefaultExports;

public class PrometheusMetricsServer {
	
    public static void start() throws Exception {
    	System.out.println("Prometheus Metrics Server running at port 8081");
        DefaultExports.initialize();
        HTTPServer server = new HTTPServer("0.0.0.0", 8081);
    }

}
