
package com.example.examengine.entity;

import javax.persistence.*;

@Entity
@Table(name = "ApplicationPost")
public class ApplicationPost {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "candidate_id")
	private Candidate candidate;

	@Column(name = "applied_post")
	private String appliedPost;

	@Column(name = "allocation_status")
	private String allocationStatus = "PENDING";

	public ApplicationPost() {
	}

	public Long getId() {
		return id;
	}

	public Candidate getCandidate() {
		return candidate;
	}

	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}

	public String getAppliedPost() {
		return appliedPost;
	}

	public void setAppliedPost(String appliedPost) {
		this.appliedPost = appliedPost;
	}

	public String getAllocationStatus() {
		return allocationStatus;
	}

	public void setAllocationStatus(String allocationStatus) {
		this.allocationStatus = allocationStatus;
	}
}
