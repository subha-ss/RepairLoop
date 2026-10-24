
package com.repairloop.controller;

import com.repairloop.dto.CreateRepairRequest;
import com.repairloop.dto.UpdateRepairStatusRequest;
import com.repairloop.entity.RepairRequest;
import com.repairloop.service.RepairRequestService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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

    // Create a repair request for the authenticated customer
    @PostMapping
    public ResponseEntity<?> createRepairRequest(
            @Valid @RequestBody CreateRepairRequest request,
            Authentication authentication) {

        Long userId = getAuthenticatedUserId(authentication);

        RepairRequest repairRequest = new RepairRequest();
        repairRequest.setUserId(userId);
        repairRequest.setDeviceType(request.getDeviceType());
        repairRequest.setBrand(request.getBrand());
        repairRequest.setModel(request.getModel());
        repairRequest.setProblemDescription(
                request.getProblemDescription());

        try {
            RepairRequest saved =
                    repairRequestService.createRepairRequest(repairRequest);

            return ResponseEntity.status(HttpStatus.CREATED).body(saved);

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", exception.getMessage()));
        }
    }

    // Customers can view only their own repair requests
    @GetMapping
    public ResponseEntity<List<RepairRequest>> getMyRepairRequests(
            Authentication authentication) {

        Long userId = getAuthenticatedUserId(authentication);

        List<RepairRequest> requests =
                repairRequestService.getRequestsByUserId(userId);

        return ResponseEntity.ok(requests);
    }

    // Only staff or administrators can update repair status
    @PatchMapping("/{repairId}/status")
    public ResponseEntity<?> updateRepairStatus(
            @PathVariable Long repairId,
            @Valid @RequestBody UpdateRepairStatusRequest request,
            Authentication authentication) {

        boolean authorized = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role ->
                        role.equals("ROLE_STAFF")
                        || role.equals("ROLE_ADMIN"));

        if (!authorized) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                            "error",
                            "Only authorized staff can update repair status."));
        }

        try {
            RepairRequest updated =
                    repairRequestService.updateRepairStatus(
                            repairId, request.getStatus());

            return ResponseEntity.ok(updated);

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", exception.getMessage()));
        }
    }

    private Long getAuthenticatedUserId(Authentication authentication) {
        return (Long) authentication.getPrincipal();
    }
}
