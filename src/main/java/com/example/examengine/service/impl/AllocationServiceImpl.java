
package com.example.examengine.service.impl;

import com.example.examengine.service.AllocationService;
import com.example.examengine.dto.CapacityDTO;
import com.example.examengine.dto.DailyReportDTO;
import com.example.examengine.entity.*;
import com.example.examengine.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AllocationServiceImpl implements AllocationService {

	@Autowired
	private ApplicationPostRepository applicationPostRepository;

	@Autowired
	private CandidateRepository candidateRepository;

	@Autowired
	private CentreRepository centreRepository;

	@Autowired
	private SlotRepository slotRepository;

	@Autowired
	private AllocationRepository allocationRepository;

	private final Object lock = new Object();

	@Override
	@Transactional
	public String runAllocation() {
		synchronized (lock) {
			List<ApplicationPost> posts = applicationPostRepository.findByAllocationStatus("PENDING");
			if (posts == null || posts.isEmpty()) {
				return "NO_PENDING_POSTS";
			}

			List<Centre> centres = centreRepository.findAll();
			List<Slot> slots = slotRepository.findAll().stream().sorted(Comparator.comparing(Slot::getSlotTime))
					.collect(Collectors.toList());

			// Group posts by candidate registration number
			Map<Integer, List<ApplicationPost>> grouped = posts.stream()
					.collect(Collectors.groupingBy(p -> p.getCandidate().getRegistrationNumber()));

			int centreIdx = 0;
			for (Map.Entry<Integer, List<ApplicationPost>> entry : grouped.entrySet()) {
				List<ApplicationPost> appls = entry.getValue();
				Candidate cand = appls.get(0).getCandidate();

				List<Slot> allowedSlots = new ArrayList<>(slots);
				if ("F".equalsIgnoreCase(cand.getGender())) {
					// remove late slot containing 16:00
					allowedSlots = allowedSlots.stream().filter(s -> !s.getSlotTime().contains("16:00"))
							.collect(Collectors.toList());
				}

				List<Centre> allowedCentres = centres;
				if (Boolean.TRUE.equals(cand.getIsPwd())) {
					allowedCentres = centres.stream().filter(Centre::getIsPwdFriendly).collect(Collectors.toList());
				}

				boolean allAllocated = true;

				if (appls.size() == 1) {
					ApplicationPost ap = appls.get(0);
					boolean allocated = false;
					for (int i = 0; i < allowedCentres.size() && !allocated; i++) {
						Centre c = allowedCentres.get((centreIdx + i) % allowedCentres.size());
						for (Slot s : allowedSlots) {
							long count = allocationRepository.countBySlotAndCentre(s, c);
							if (count < c.getCapacity()) {
								Allocation alloc = new Allocation();
								alloc.setApplicationPost(ap);
								alloc.setCentre(c);
								alloc.setSlot(s);
								alloc.setAllocationTime(LocalDateTime.now());
								allocationRepository.save(alloc);
								ap.setAllocationStatus("ALLOCATED");
								applicationPostRepository.save(ap);
								allocated = true;
								break;
							}
						}
					}
					if (!allocated)
						allAllocated = false;
					centreIdx = (centreIdx + 1) % Math.max(1, allowedCentres.size());
				} else {
					boolean allocatedGroup = false;
					for (int i = 0; i < allowedCentres.size() && !allocatedGroup; i++) {
						Centre c = allowedCentres.get((centreIdx + i) % allowedCentres.size());
						// find free slots in centre
						List<Slot> freeSlots = new ArrayList<>();
						for (Slot s : allowedSlots) {
							long count = allocationRepository.countBySlotAndCentre(s, c);
							if (count < c.getCapacity())
								freeSlots.add(s);
						}
						if (freeSlots.size() >= appls.size()) {
							for (int k = 0; k < appls.size(); k++) {
								ApplicationPost ap = appls.get(k);
								Slot s = freeSlots.get(k);
								Allocation alloc = new Allocation();
								alloc.setApplicationPost(ap);
								alloc.setCentre(c);
								alloc.setSlot(s);
								alloc.setAllocationTime(LocalDateTime.now());
								allocationRepository.save(alloc);
								ap.setAllocationStatus("ALLOCATED");
								applicationPostRepository.save(ap);
							}
							allocatedGroup = true;
						}
					}
					if (!allocatedGroup)
						allAllocated = false;
					centreIdx = (centreIdx + 1) % Math.max(1, allowedCentres.size());
				}

				if (!allAllocated) {
					for (ApplicationPost ap : appls) {
						ap.setAllocationStatus("PENDING");
						applicationPostRepository.save(ap);
					}
				}
			}

			return "ALLOCATION_RUN_COMPLETED";
		}
	}

	// 2. Daily Report
	@Override
	public List<DailyReportDTO> getDailyReport(String date) {
		List<Allocation> allocs = allocationRepository.findAll();
		List<DailyReportDTO> list = new ArrayList<>();

		for (Allocation a : allocs) {
			if (a.getSlot().getExamDate().toString().equals(date)) {
				DailyReportDTO dto = new DailyReportDTO();
				dto.setCandidateName(a.getApplicationPost().getCandidate().getCandidateName());
				dto.setRegistrationNumber(a.getApplicationPost().getCandidate().getRegistrationNumber());
				dto.setAppliedPost(a.getApplicationPost().getAppliedPost());
				dto.setCentreName(a.getCentre().getCenterName());
				dto.setSlotTime(a.getSlot().getSlotTime());
				list.add(dto);
			}
		}
		return list;
	}

	// 3. Candidate Status
	@Override
	public List<Map<String, Object>> getCandidateStatus(Integer regNo) {
		Candidate c = candidateRepository.findByRegistrationNumber(regNo).orElse(null);

		List<Map<String, Object>> resp = new ArrayList<>();
		if (c == null)
			return resp;

		List<ApplicationPost> posts = applicationPostRepository.findByCandidate(c);
		for (ApplicationPost ap : posts) {
			Map<String, Object> m = new HashMap<>();
			m.put("appliedPost", ap.getAppliedPost());
			m.put("allocationStatus", ap.getAllocationStatus());
			resp.add(m);
		}
		return resp;
	}

	// 4. Health Check
	@Override
	public Map<String, Object> health() {
		Map<String, Object> h = new HashMap<>();
		h.put("status", "UP");
		h.put("candidates", candidateRepository.count());
		h.put("centres", centreRepository.count());
		h.put("slots", slotRepository.count());
		h.put("allocations", allocationRepository.count());
		return h;
	}

	// 5. Reset Allocations via SP
	@Override
	@Transactional(readOnly = true)
	public Map<String, String> resetAllocations() {
		Map<String, String> m = new HashMap<>();
		try {
			allocationRepository.resetAllocationsSP();
			m.put("status", "RESET_DONE");
		} catch (Exception e) {
			m.put("error", e.getMessage());
		}
		return m;
	}

	// 6. Capacity Report via SP
	@Override
	@Transactional(readOnly = true)
	public List<CapacityDTO> getCapacity() {
		List<Object[]> rows = centreRepository.getCentreSlotCapacity();
		List<CapacityDTO> list = new ArrayList<>();

		for (Object[] r : rows) {
			CapacityDTO dto = new CapacityDTO();
			dto.setCenterName((String) r[0]);
			dto.setSlotTime((String) r[1]);
			dto.setExamDate(r[2].toString());
			dto.setCapacity((Integer) r[3]);
			dto.setAllocated((Integer) r[4]);
			dto.setRemaining((Integer) r[5]);
			list.add(dto);
		}
		return list;
	}

	// 7. Candidate Allocation Details via SP
	@Override
	@Transactional(readOnly = true)
	public List<Map<String, Object>> getCandidateAllocationDetails(Integer regNo) {
		List<Object[]> rows = candidateRepository.getCandidateAllocation(regNo);
		List<Map<String, Object>> resp = new ArrayList<>();

		for (Object[] r : rows) {
			Map<String, Object> m = new HashMap<>();
			m.put("candidateName", r[0]);
			m.put("appliedPost", r[1]);
			m.put("allocationStatus", r[2]);
			m.put("centreName", r[3]);
			m.put("slotTime", r[4]);
			m.put("examDate", r[5]);
			resp.add(m);
		}
		return resp;
	}
}
