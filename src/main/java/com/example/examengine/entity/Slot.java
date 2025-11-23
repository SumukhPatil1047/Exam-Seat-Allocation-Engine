
package com.example.examengine.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Slot")
public class Slot {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "slot_time")
	private String slotTime;

	@Column(name = "exam_date")
	private LocalDate examDate;

	public Slot() {
	}

	public Long getId() {
		return id;
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
}
