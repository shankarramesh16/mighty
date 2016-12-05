package com.team.mighty.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team.mighty.domain.AdminUser;

public interface AdminDao extends JpaRepository<AdminUser, Integer> {

}
