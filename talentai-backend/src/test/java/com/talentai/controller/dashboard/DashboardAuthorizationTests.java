package com.talentai.controller.dashboard;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ActiveProfiles("test")
class DashboardAuthorizationTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(roles = "RECRUITER")
    void recruiterCanAccessDashboardSummary() throws Exception {
        mockMvc.perform(get("/api/v1/dashboard/summary")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "PLATFORM_ADMIN")
    void platformAdminCanAccessDashboardSummary() throws Exception {
        mockMvc.perform(get("/api/v1/dashboard/summary")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CANDIDATE")
    void candidateCannotAccessDashboardSummary() throws Exception {
        mockMvc.perform(get("/api/v1/dashboard/summary")).andExpect(status().isForbidden());
    }
}
