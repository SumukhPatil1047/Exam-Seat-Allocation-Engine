
package com.example.examengine.dto;

import java.time.LocalDate;

public class AllocationResponseDTO {
	private String appliedPost;
	private String centreName;
	private String slotTime;
	private LocalDate examDate;
	private String allocationStatus;

	public AllocationResponseDTO() {
	}

	public String getAppliedPost() {
		return appliedPost;
	}

	public void setAppliedPost(String appliedPost) {
		this.appliedPost = appliedPost;
	}

	public String getCentreName() {
		return centreName;
	}

	public void setCentreName(String centreName) {
		this.centreName = centreName;
	}

	public String getSlotTime() {
		return slotTime;
	}

	public void setSlotTime(String slotTime) {
		this.slotTime = slotTime;
	}

	public LocalDate getExamDate() {
		return examDate;
	}

	public void setExamDate(LocalDate examDate) {
		this.examDate = examDate;
	}

	public String getAllocationStatus() {
		return allocationStatus;
	}

	public void setAllocationStatus(String allocationStatus) {
		this.allocationStatus = allocationStatus;
	}
}
