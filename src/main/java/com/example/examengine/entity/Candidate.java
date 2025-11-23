
package com.example.examengine.entity;

import javax.persistence.*;

@Entity
@Table(name = "Candidate")
public class Candidate {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "registration_number")
	private Integer registrationNumber;

	@Column(name = "candidate_name")
	private String candidateName;

	private String gender;

	@Column(name = "is_pwd")
	private Boolean isPwd;

	public Candidate() {
	}

	public Long getId() {
		return id;
	}

	public Integer getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(Integer registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public String getCandidateName() {
		return candidateName;
	}

	public void setCandidateName(String candidateName) {
		this.candidateName = candidateName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Boolean getIsPwd() {
		return isPwd;
	}

	public void setIsPwd(Boolean isPwd) {
		this.isPwd = isPwd;
	}
}
