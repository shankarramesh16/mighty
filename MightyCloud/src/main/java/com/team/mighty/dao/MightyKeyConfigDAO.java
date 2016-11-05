package com.team.mighty.dao;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.team.mighty.domain.MightyKeyConfig;

public interface MightyKeyConfigDAO extends JpaRepository<MightyKeyConfig,Serializable>{ 

	@Query("SELECT m FROM MightyKeyConfig m WHERE m.mightyKeyName = :mightyKeyName")
	MightyKeyConfig getKeyConfigValue(@Param("mightyKeyName") String mightyKeyName);
}
