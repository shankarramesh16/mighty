package com.mighty.spotify.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RedirectedSpotifyAccess
 */
public class RedirectedSpotifyAccess extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RedirectedSpotifyAccess() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: Redirected ").append(request.getContextPath());
		String	code =	request.getParameter("code");
		String	state =	request.getParameter("state");
		String	error =	request.getParameter("error");
		if(error == null || error.equalsIgnoreCase("")) {
			
		}
		System.out.println(code);
		
		String url = "https://accounts.spotify.com/api/token";
		
		try {
			sendPost(url, code);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private void sendPost(String url, String code) throws Exception {

		 SSLContext context = SSLContext.getInstance("TLS"); 
         context.init(null, new X509TrustManager[]{new X509TrustManager(){ 
                 public void checkClientTrusted(X509Certificate[] chain, 
                                 String authType) throws CertificateException {} 
                 public void checkServerTrusted(X509Certificate[] chain, 
                                 String authType) throws CertificateException {} 
                 public X509Certificate[] getAcceptedIssuers() { 
                         return new X509Certificate[0]; 
                 }}}, new SecureRandom()); 
         HttpsURLConnection.setDefaultSSLSocketFactory( 
                         context.getSocketFactory()); 

		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		con.setRequestMethod("POST");
		con.setDoOutput(true);
	    Map<String,String> arguments = new HashMap<String, String>();
	    arguments.put("code", code);
	    arguments.put("grant_type", "authorization_code");
	    arguments.put("client_id", "8cda18d9034947759f0b09e68e17c7c1");
	    arguments.put("client_secret", "5d38b745cb0445f793b950b36eec95aa");
	    arguments.put("redirect_uri", "http://localhost:8080/SpotifyAccess/RedirectedSpotifyAccess");
	    StringBuilder sj = new StringBuilder();
	    for(Map.Entry<String,String> entry : arguments.entrySet()) {
	        sj.append(URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8") + "&");
	    }
	    byte[] out = sj.toString().getBytes();

	    con.setFixedLengthStreamingMode(out.length);
	    con.connect();
	    try
	    {
	        OutputStream os = con.getOutputStream();
	        os.write(out);
	        
	        BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			//print result
			System.out.println(response.toString());

	    }
	    catch (Exception e)
	    {
	    	e.printStackTrace();
	    }
		
	}

}
