package com.team.mighty.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.team.mighty.domain.MightyDeviceFirmware;

public interface MightyDeviceFirmwareDAO extends JpaRepository<MightyDeviceFirmware, Serializable> {
	
	@Query("FROM MightyDeviceFirmware")
	List<MightyDeviceFirmware> getDeviceFirmware() throws Exception;

}
