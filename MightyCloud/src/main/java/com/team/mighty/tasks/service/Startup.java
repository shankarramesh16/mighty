package com.team.mighty.tasks.service;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.team.mighty.logger.MightyLogger;


/**
 * Servlet implementation class StartupServlet
 */
@Service
public class Startup extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final MightyLogger logger = MightyLogger.getLogger(Startup.class);
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Startup() {
		super();
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		try {
			logger.debug("Utility initialization");
				Scheduler.getInstance().startTasks();
			
		} catch (Exception e) {
			logger.debug("Error in Utility",e);
		}
	}

	public void destroy() {
		try {
			Scheduler.getInstance().endTasks();
			// System.out.println("===================Destroy Method====================");
		} catch (Exception e) {
			logger.debug("Error in Utility destroy ",e);
		}
	}

	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}
}
