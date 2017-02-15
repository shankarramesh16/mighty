package com.team.mighty.dao;
import java.io.Serializable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.team.mighty.domain.MightyUserInfo;

/**
 * 
 * @author Shankara
 *
 */
@Repository
public interface ConsumerInstrumentDAO extends JpaRepository<MightyUserInfo, Serializable>{

}
