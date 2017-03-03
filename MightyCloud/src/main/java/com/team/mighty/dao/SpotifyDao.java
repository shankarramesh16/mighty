package com.team.mighty.dao;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.team.mighty.domain.SpotifyInfo;

public interface SpotifyDao extends JpaRepository<SpotifyInfo, Serializable> {
	
	@Query("SELECT spotify FROM SpotifyInfo spotify WHERE spotify.id =:id")
	SpotifyInfo getSpotifyInfoByTokenId(@Param("id") Integer id);


}
