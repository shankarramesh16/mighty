package com.mighty.spotify.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SpotifyAccessToken1
 */
@WebServlet(name="spotifyAccessToken")
public class SpotifyAccessToken extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private String client_id="8cda18d9034947759f0b09e68e17c7c1";
	private String response_type="code";
	private String redirect_uri="http://localhost:8080/SpotifyAccess/RedirectedSpotifyAccess";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SpotifyAccessToken() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			System.out.printf("IN doget ");
			String url = "https://accounts.spotify.com/authorize?client_id="+client_id+"&response_type="+response_type+"&redirect_uri="+redirect_uri;
			response.sendRedirect(url);
			//sendGet();
		} catch(Exception e){
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

	private String sendGet() throws Exception {

		String url = "https://accounts.spotify.com/authorize?client_id="+client_id+"&response_type="+response_type+"&redirect_uri="+redirect_uri;

		
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

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		System.out.println(response.toString());
		return response.toString();
		
	}
}
