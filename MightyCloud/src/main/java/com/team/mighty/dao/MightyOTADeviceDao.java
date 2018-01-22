package com.team.mighty.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.team.mighty.domain.Mightyotadevice;

public interface MightyOTADeviceDao extends JpaRepository<Mightyotadevice, Serializable> {

	@Query("SELECT m FROM Mightyotadevice m WHERE m.devices=:devices order by m.version desc")
	List<Mightyotadevice> mightyOTADeviceDao(@Param("devices") String devices);

	@Query("FROM Mightyotadevice")
	List<Mightyotadevice> getExcelUploadMightyInfo();
	
	/*@Query(value="truncate table mightyotadevices",nativeQuery = true)
	void deleteExistingEntryFromMightyOTADev();*/
	

}
