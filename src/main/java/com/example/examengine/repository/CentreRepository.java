
package com.example.examengine.repository;

import com.example.examengine.entity.Centre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CentreRepository extends JpaRepository<Centre, Long> {
    List<Centre> findByIsPwdFriendlyTrue();
    
    @Transactional(readOnly = true)
    @Procedure(value = "sp_CentreSlotCapacity")
    List<Object[]> getCentreSlotCapacity();
}
