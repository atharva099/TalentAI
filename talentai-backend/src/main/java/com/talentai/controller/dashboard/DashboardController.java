package com.talentai.controller.dashboard;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.talentai.dto.response.ApiResponse;
import com.talentai.dto.response.DashboardResponse;
import com.talentai.service.dashboard.DashboardService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * Exposes recruiter dashboard summary data.
 */
@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private static final String REQUEST_ID_HEADER = "X-Request-Id";

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<DashboardResponse>> getSummary(HttpServletRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Dashboard summary retrieved successfully.",
                dashboardService.getSummary(), request.getHeader(REQUEST_ID_HEADER)));
    }
}
