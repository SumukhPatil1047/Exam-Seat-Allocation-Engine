
package com.example.examengine.repository;

import com.example.examengine.entity.Allocation;
import com.example.examengine.entity.Slot;
import com.example.examengine.entity.Centre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AllocationRepository extends JpaRepository<Allocation, Long> {
	List<Allocation> findByApplicationPostId(Long applicationPostId);

	List<Allocation> findBySlotAndCentre(Slot slot, Centre centre);

	long countBySlotAndCentre(Slot slot, Centre centre);

	List<Allocation> findByApplicationPost_Candidate_RegistrationNumber(Integer registrationNumber);

	List<Allocation> findBySlot(Slot slot);

	@Transactional(readOnly = true)
	@Procedure(value = "sp_ResetAllocations")
	void resetAllocationsSP();
}
