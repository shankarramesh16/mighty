package com.team.mighty.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.team.mighty.domain.Setting;

public interface SettingDao extends JpaRepository<Setting, Long> {
			
	@Query("from Setting ")
	List<Setting> getUtilityLastUpdatedDate();
}
