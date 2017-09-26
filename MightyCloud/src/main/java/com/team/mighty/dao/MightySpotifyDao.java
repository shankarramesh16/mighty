package com.team.mighty.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.team.mighty.domain.MightySpotify;

public interface MightySpotifyDao extends JpaRepository<MightySpotify, Serializable> {

	@Query("Select ms From MightySpotify ms where ms.deviceId=:deviceId")
	List<MightySpotify> getMightySpotifyDetails(@Param("deviceId") String deviceId);

}
