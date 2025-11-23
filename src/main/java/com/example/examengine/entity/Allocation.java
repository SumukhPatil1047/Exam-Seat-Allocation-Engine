
package com.example.examengine.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Allocation")
public class Allocation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "application_post_id")
	private ApplicationPost applicationPost;

	@ManyToOne
	@JoinColumn(name = "centre_id")
	private Centre centre;

	@ManyToOne
	@JoinColumn(name = "slot_id")
	private Slot slot;

	@Column(name = "allocation_time")
	private LocalDateTime allocationTime;

	public Allocation() {
	}

	public Long getId() {
		return id;
	}

	public ApplicationPost getApplicationPost() {
		return applicationPost;
	}

	public void setApplicationPost(ApplicationPost applicationPost) {
		this.applicationPost = applicationPost;
	}

	public Centre getCentre() {
		return centre;
	}

	public void setCentre(Centre centre) {
		this.centre = centre;
	}

	public Slot getSlot() {
		return slot;
	}

	public void setSlot(Slot slot) {
		this.slot = slot;
	}

	public LocalDateTime getAllocationTime() {
		return allocationTime;
	}

	public void setAllocationTime(LocalDateTime allocationTime) {
		this.allocationTime = allocationTime;
	}
}
