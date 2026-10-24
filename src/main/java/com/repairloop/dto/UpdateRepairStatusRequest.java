
package com.repairloop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class UpdateRepairStatusRequest {

    @NotBlank(message = "Status is required")
    @Pattern(
        regexp = "DIAGNOSING|IN_PROGRESS|COMPLETED",
        message = "Status must be DIAGNOSING, IN_PROGRESS, or COMPLETED"
    )
    private String status;

    public UpdateRepairStatusRequest() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
