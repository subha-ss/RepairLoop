
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

    public List<RepairRequest> getRequestsByUserId(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found.");
        }

        return repairRequestRepository.findByUserId(userId);
    }
}
