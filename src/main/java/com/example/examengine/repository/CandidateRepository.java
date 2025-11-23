
package com.example.examengine.repository;

import com.example.examengine.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
	Optional<Candidate> findByRegistrationNumber(Integer registrationNumber);

	@Transactional(readOnly = true)
	@Procedure(value = "sp_GetCandidateAllocation")
	List<Object[]> getCandidateAllocation(Integer regNo);
}
