
package com.repairloop.controller;

import com.repairloop.dto.CreateRepairRequest;
import com.repairloop.entity.RepairRequest;
import com.repairloop.service.RepairRequestService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/repairs")
public class RepairRequestController {

    private final RepairRequestService repairRequestService;

    public RepairRequestController(
            RepairRequestService repairRequestService) {
        this.repairRequestService = repairRequestService;
    }

    @PostMapping
    public ResponseEntity<?> createRepairRequest(
            @Valid @RequestBody CreateRepairRequest request) {

        RepairRequest repairRequest = new RepairRequest();
        repairRequest.setUserId(request.getUserId());
        repairRequest.setDeviceType(request.getDeviceType());
        repairRequest.setBrand(request.getBrand());
        repairRequest.setModel(request.getModel());
        repairRequest.setProblemDescription(
                request.getProblemDescription()
        );

        try {
            RepairRequest saved =
                    repairRequestService.createRepairRequest(repairRequest);

            return ResponseEntity.status(HttpStatus.CREATED).body(saved);

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", exception.getMessage())
            );
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getRequestsByUser(
            @PathVariable Long userId) {

        try {
            List<RepairRequest> requests =
                    repairRequestService.getRequestsByUserId(userId);

            return ResponseEntity.ok(requests);

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", exception.getMessage())
            );
        }
    }
}
