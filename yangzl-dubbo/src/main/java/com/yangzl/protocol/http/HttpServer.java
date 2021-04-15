package com.yangzl.protocol.http;

import org.apache.catalina.*;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardEngine;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.startup.Tomcat;

/**
 * @author yangzl
 * @date 2021/3/29
 * @desc embed tomcat server
 */
public class HttpServer {

	public void start(String hostname, int port) {
		Tomcat tomcat = new Tomcat();
		Server server = tomcat.getServer();
		Service service = server.findService("Tomcat");

		Connector connector = new Connector();
		connector.setPort(port);
		connector.setURIEncoding("UTF-8");

		Engine engine = new StandardEngine();
		engine.setDefaultHost(hostname);

		Host host = new StandardHost();
		host.setName(hostname);

		Context context = new StandardContext();
		context.setPath("");
		context.addLifecycleListener(new Tomcat.FixContextListener());

		host.addChild(context);
		engine.addChild(host);

		service.addConnector(connector);
		service.setContainer(engine);

		// tomcat 是 servlet 容器
		Tomcat.addServlet(context, "dispatcherServlet", new DispatcherServlet());
		context.addServletMappingDecoded("/*", "dispatcherServlet");

		try {
			tomcat.start();
			server.await();
		} catch (LifecycleException e) {
			e.printStackTrace();
		}

	}

}
