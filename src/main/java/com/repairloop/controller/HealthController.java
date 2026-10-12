
package com.repairloop.controller;

import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealthController {

    private final JdbcTemplate jdbcTemplate;

    public HealthController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of(
            "application", "RepairLoop",
            "status", "UP"
        );
    }

    @GetMapping("/db-check")
    public Map<String, Object> databaseCheck() {
        Integer result = jdbcTemplate.queryForObject(
            "SELECT 1",
            Integer.class
        );

        return Map.of(
            "database", "connected",
            "queryResult", result
        );
    }
}
