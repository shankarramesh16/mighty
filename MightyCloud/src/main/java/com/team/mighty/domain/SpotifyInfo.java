package com.team.mighty.domain;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the spotify_info database table.
 * 
 */
@Entity
@Table(name="spotify_info")
@NamedQuery(name="SpotifyInfo.findAll", query="SELECT s FROM SpotifyInfo s")
public class SpotifyInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Column(name="access_token")
	private String accessToken;

	@Column(name="expires_in")
	private String expiresIn;
	

	@Column(name="refresh_token")
	private String refreshToken;

	public SpotifyInfo() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAccessToken() {
		return this.accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getExpiresIn() {
		return this.expiresIn;
	}

	public void setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
	}


	public String getRefreshToken() {
		return this.refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

}