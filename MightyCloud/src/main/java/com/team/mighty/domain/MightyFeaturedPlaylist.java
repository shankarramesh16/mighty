package com.team.mighty.domain;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;


/**
 * The persistent class for the mighty_featured_playlist database table.
 * 
 */
@Entity
@Table(name="mighty_featured_playlist")
@NamedQuery(name="MightyFeaturedPlaylist.findAll", query="SELECT m FROM MightyFeaturedPlaylist m")
public class MightyFeaturedPlaylist extends BaseEntityInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "ID")
	private String id;

	@Column(name="`Created By`")
	private String created_By;

	@Temporal(TemporalType.DATE)
	private Date created_Date;

	private String genre;

	private String playlist_ID;

	private String playlist_Name;

	private String playlist_URL;

	private String status;

	private String updated_By;

	@Temporal(TemporalType.TIMESTAMP)
	private Date updated_Date;

	public MightyFeaturedPlaylist() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreated_By() {
		return this.created_By;
	}

	public void setCreated_By(String created_By) {
		this.created_By = created_By;
	}

	public Date getCreated_Date() {
		return this.created_Date;
	}

	public void setCreated_Date(Date created_Date) {
		this.created_Date = created_Date;
	}

	public String getGenre() {
		return this.genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getPlaylist_ID() {
		return this.playlist_ID;
	}

	public void setPlaylist_ID(String playlist_ID) {
		this.playlist_ID = playlist_ID;
	}

	public String getPlaylist_Name() {
		return this.playlist_Name;
	}

	public void setPlaylist_Name(String playlist_Name) {
		this.playlist_Name = playlist_Name;
	}

	public String getPlaylist_URL() {
		return this.playlist_URL;
	}

	public void setPlaylist_URL(String playlist_URL) {
		this.playlist_URL = playlist_URL;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUpdated_By() {
		return this.updated_By;
	}

	public void setUpdated_By(String updated_By) {
		this.updated_By = updated_By;
	}

	public Date getUpdated_Date() {
		return this.updated_Date;
	}

	public void setUpdated_Date(Date updated_Date) {
		this.updated_Date = updated_Date;
	}

}