
package com.example.examengine.service;

import java.util.List;
import java.util.Map;

import com.example.examengine.dto.CapacityDTO;
import com.example.examengine.dto.DailyReportDTO;

public interface AllocationService {
	String runAllocation();

	List<DailyReportDTO> getDailyReport(String date);

	List<Map<String, Object>> getCandidateStatus(Integer registrationNumber);

	Map<String, Object> health();

	Map<String, String> resetAllocations();

	List<CapacityDTO> getCapacity();

	List<Map<String, Object>> getCandidateAllocationDetails(Integer regNo);
}
