
package com.example.examengine.controller;

import com.example.examengine.dto.*;
import com.example.examengine.entity.*;
import com.example.examengine.repository.*;
import com.example.examengine.service.AllocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class AllocationController {

	@Autowired
	private AllocationService allocationService;

	@Autowired
	private AllocationRepository allocationRepository;

	@Autowired
	private CandidateRepository candidateRepository;

	@Autowired
	private ApplicationPostRepository applicationPostRepository;

	@PostMapping("/allocate")
	public ResponseEntity<?> allocate() {
		try {
			String result = allocationService.runAllocation();
			Map<String, String> m = new HashMap<>();
			m.put("status", result);
			return ResponseEntity.ok(m);
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDTO(ex.getMessage()));
		}
	}

	@GetMapping("/allocation/{registrationNumber}")
	public ResponseEntity<?> getAllocation(@PathVariable Integer registrationNumber) {
		Optional<Candidate> opt = candidateRepository.findByRegistrationNumber(registrationNumber);
		if (!opt.isPresent())
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("Candidate not found"));
		Candidate c = opt.get();
		List<ApplicationPost> posts = applicationPostRepository.findByCandidate(c);
		List<AllocationResponseDTO> resp = new ArrayList<>();
		for (ApplicationPost p : posts) {
			AllocationResponseDTO dto = new AllocationResponseDTO();
			dto.setAppliedPost(p.getAppliedPost());
			dto.setAllocationStatus(p.getAllocationStatus());
			if ("ALLOCATED".equalsIgnoreCase(p.getAllocationStatus())) {
				List<Allocation> allocs = allocationRepository.findByApplicationPostId(p.getId());
				if (!allocs.isEmpty()) {
					Allocation a = allocs.get(0);
					dto.setCentreName(a.getCentre().getCenterName());
					dto.setSlotTime(a.getSlot().getSlotTime());
					dto.setExamDate(a.getSlot().getExamDate());
				}
			}
			resp.add(dto);
		}
		return ResponseEntity.ok(resp);
	}

	@GetMapping("/report/daily")
	public ResponseEntity<?> dailyReport(@RequestParam String date) {
		return ResponseEntity.ok(allocationService.getDailyReport(date));
	}

	@GetMapping("/status/{registrationNumber}")
	public ResponseEntity<?> status(@PathVariable Integer registrationNumber) {
		return ResponseEntity.ok(allocationService.getCandidateStatus(registrationNumber));
	}

	@GetMapping("/health")
	public ResponseEntity<?> health() {
		return ResponseEntity.ok(allocationService.health());
	}

	@PostMapping("/admin/reset")
	@Transactional(readOnly = true)
	public ResponseEntity<?> resetAllocations() {
		return ResponseEntity.ok(allocationService.resetAllocations());
	}

	@GetMapping("/capacity")
	@Transactional(readOnly = true)
	public ResponseEntity<?> capacity() {
		return ResponseEntity.ok(allocationService.getCapacity());
	}

	@GetMapping("/allocation/details/{regNo}")
	@Transactional(readOnly = true)
	public ResponseEntity<?> allocationDetails(@PathVariable Integer regNo) {
		return ResponseEntity.ok(allocationService.getCandidateAllocationDetails(regNo));
	}
}
