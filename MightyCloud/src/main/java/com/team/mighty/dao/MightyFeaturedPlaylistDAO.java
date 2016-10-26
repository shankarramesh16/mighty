package com.team.mighty.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.team.mighty.domain.MightyFeaturedPlaylist;

public interface MightyFeaturedPlaylistDAO extends JpaRepository<MightyFeaturedPlaylist, Serializable> {
	
	
	@Query("FROM MightyFeaturedPlaylist")
	List<MightyFeaturedPlaylist> getMightyFeaturedPlaylist();

}
