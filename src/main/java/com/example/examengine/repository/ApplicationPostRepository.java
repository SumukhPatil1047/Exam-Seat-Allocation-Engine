
package com.example.examengine.repository;

import com.example.examengine.entity.ApplicationPost;
import com.example.examengine.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ApplicationPostRepository extends JpaRepository<ApplicationPost, Long> {
	List<ApplicationPost> findByCandidate(Candidate candidate);

	List<ApplicationPost> findByAllocationStatus(String status);
}
