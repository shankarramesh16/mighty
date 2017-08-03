package com.team.mighty.dao;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team.mighty.domain.MightyUpload;

public interface MightyUploadDao extends JpaRepository<MightyUpload, Serializable> {
	

}
