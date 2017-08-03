package com.team.mighty.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.team.mighty.domain.Mightydlauditlog;

public interface MightydlauditlogDao extends JpaRepository<Mightydlauditlog, Serializable> {

	@Query("FROM Mightydlauditlog")
	List<Mightydlauditlog> getMightyDlAuditLog();

}
