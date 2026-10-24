
package com.repairloop.service;

import com.repairloop.entity.RepairRequest;
import com.repairloop.repository.RepairRequestRepository;
import com.repairloop.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepairRequestService {

    private final RepairRequestRepository repairRequestRepository;
    private final UserRepository userRepository;

    public RepairRequestService(
            RepairRequestRepository repairRequestRepository,
            UserRepository userRepository) {
        this.repairRequestRepository = repairRequestRepository;
        this.userRepository = userRepository;
    }

    // Create a new repair request
    public RepairRequest createRepairRequest(RepairRequest request) {

        if (!userRepository.existsById(request.getUserId())) {
            throw new IllegalArgumentException(
                    "User not found. Please register or log in first."
            );
        }

        request.setId(null);
        request.setStatus("SUBMITTED");

        return repairRequestRepository.save(request);
    }

    // Get all repair requests for a particular user
    public List<RepairRequest> getRequestsByUserId(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found.");
        }

        return repairRequestRepository.findByUserId(userId);
    }

    // Update the status of an existing repair request
    public RepairRequest updateRepairStatus(
            Long repairId, String newStatus) {

        RepairRequest request = repairRequestRepository.findById(repairId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Repair request not found."
                        )
                );

        if (!"IN_PROGRESS".equals(newStatus)
                && !"COMPLETED".equals(newStatus)) {
            throw new IllegalArgumentException(
                    "Status must be IN_PROGRESS or COMPLETED."
            );
        }

        if ("COMPLETED".equals(request.getStatus())) {
            throw new IllegalArgumentException(
                    "A completed repair cannot be updated."
            );
        }

        request.setStatus(newStatus);

        return repairRequestRepository.save(request);
    }
}
