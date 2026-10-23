
package com.repairloop.repository;

import com.repairloop.entity.RepairRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepairRequestRepository
        extends JpaRepository<RepairRequest, Long> {

    List<RepairRequest> findByUserId(Long userId);
}
