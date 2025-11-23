
package com.example.examengine.repository;

import com.example.examengine.entity.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface SlotRepository extends JpaRepository<Slot, Long> {
	List<Slot> findByExamDate(LocalDate date);
}
