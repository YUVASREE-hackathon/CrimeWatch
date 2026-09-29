package com.crimewatch;

import com.crimewatch.domain.*;
import com.crimewatch.repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest @AutoConfigureMockMvc @Transactional
class ApiIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Autowired UserRepository users;
    @Autowired RoleRepository roles;
    @Autowired CategoryRepository categories;
    @Autowired ReportRepository reports;
    @Autowired StatusHistoryRepository histories;
    @Autowired PasswordEncoder encoder;

    AppUser citizen;
    AppUser officer;
    AppUser admin;
    CrimeCategory theft;

    @BeforeEach void setUp() {
        RoleDefinition citizenRole = roles.save(RoleDefinition.builder().code(Role.CITIZEN).displayName("Citizen").build());
        RoleDefinition officerRole = roles.save(RoleDefinition.builder().code(Role.OFFICER).displayName("Police Officer").build());
        RoleDefinition adminRole = roles.save(RoleDefinition.builder().code(Role.ADMIN).displayName("Administrator").build());
        citizen = users.save(AppUser.builder().fullName("Test Citizen").email("citizen@test.local").password(encoder.encode("Password@123")).role(Role.CITIZEN).roleDefinition(citizenRole).build());
        officer = users.save(AppUser.builder().fullName("Test Officer").email("officer@test.local").password(encoder.encode("Password@123")).role(Role.OFFICER).roleDefinition(officerRole).build());
        admin = users.save(AppUser.builder().fullName("Test Admin").email("admin@test.local").password(encoder.encode("Password@123")).role(Role.ADMIN).roleDefinition(adminRole).build());
        theft = categories.save(CrimeCategory.builder().name("Test Theft").description("Test category").build());
    }

    @Test void registersCitizenAndReturnsJwt() throws Exception {
        mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content("""
            {"fullName":"New Citizen","email":"new@test.local","password":"StrongPass@123","phone":"9876543210","address":"Test address"}
            """))
            .andExpect(status().isCreated()).andExpect(jsonPath("$.token", not(emptyString())))
            .andExpect(jsonPath("$.user.role").value("CITIZEN"));
    }

    @Test void rejectsDuplicateRegistration() throws Exception {
        mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content("""
            {"fullName":"Duplicate","email":"citizen@test.local","password":"StrongPass@123"}
            """))
            .andExpect(status().isConflict()).andExpect(jsonPath("$.message").value(containsString("already exists")));
    }

    @Test void rejectsInvalidLoginWithoutLeakingDetails() throws Exception {
        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
            .content("{\"email\":\"citizen@test.local\",\"password\":\"wrong-password\"}"))
            .andExpect(status().isUnauthorized()).andExpect(jsonPath("$.message").value("Invalid email or password"))
            .andExpect(jsonPath("$.trace").doesNotExist());
    }

    @Test void createsAndRetrievesOwnReport() throws Exception {
        String token = token("citizen@test.local");
        String response = mvc.perform(post("/api/reports").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON)
            .content(reportJson())).andExpect(status().isCreated()).andExpect(jsonPath("$.status").value("SUBMITTED"))
            .andExpect(jsonPath("$.publicId", startsWith("CR-"))).andReturn().getResponse().getContentAsString();
        String publicId = json.readTree(response).get("publicId").asText();
        mvc.perform(get("/api/reports/{id}", publicId).header("Authorization", "Bearer " + token))
            .andExpect(status().isOk()).andExpect(jsonPath("$.title").value("Test incident report"))
            .andExpect(jsonPath("$.history[0].toStatus").value("SUBMITTED"));
    }

    @Test void validatesInvalidReportRequest() throws Exception {
        mvc.perform(post("/api/reports").header("Authorization", "Bearer " + token("citizen@test.local"))
            .contentType(MediaType.APPLICATION_JSON).content("{\"categoryId\":" + theft.getId() + ",\"title\":\"x\"}"))
            .andExpect(status().isBadRequest()).andExpect(jsonPath("$.fields.title").exists())
            .andExpect(jsonPath("$.fields.description").exists());
    }

    @Test void blocksUnauthenticatedReportAccess() throws Exception {
        mvc.perform(get("/api/reports")).andExpect(status().isUnauthorized());
    }

    @Test void blocksCitizenFromAdminAnalytics() throws Exception {
        mvc.perform(get("/api/analytics/summary").header("Authorization", "Bearer " + token("citizen@test.local")))
            .andExpect(status().isForbidden());
    }

    @Test void assignedOfficerCanAdvanceInvestigationStatus() throws Exception {
        CrimeReport report = reports.save(CrimeReport.builder().publicId("CR-TEST-1001").category(theft).title("Assigned case")
            .description("A sufficiently detailed synthetic incident description for testing.").incidentDate(LocalDate.now()).incidentTime(LocalTime.NOON)
            .location("Test Road").city("Test City").reporter(citizen).assignedOfficer(officer).status(ReportStatus.ASSIGNED).build());
        histories.save(ReportStatusHistory.builder().report(report).toStatus(ReportStatus.ASSIGNED).changedBy(admin).build());
        mvc.perform(put("/api/reports/CR-TEST-1001/status").header("Authorization", "Bearer " + token("officer@test.local"))
            .contentType(MediaType.APPLICATION_JSON).content("{\"status\":\"UNDER_INVESTIGATION\",\"comment\":\"Initial inquiry started\"}"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("UNDER_INVESTIGATION"))
            .andExpect(jsonPath("$.history", hasSize(2)));
    }

    @Test void citizenCannotUpdateReportStatus() throws Exception {
        reports.save(CrimeReport.builder().publicId("CR-TEST-1002").category(theft).title("Citizen case")
            .description("A sufficiently detailed synthetic incident description for testing.").incidentDate(LocalDate.now()).incidentTime(LocalTime.NOON)
            .location("Test Road").city("Test City").reporter(citizen).build());
        mvc.perform(put("/api/reports/CR-TEST-1002/status").header("Authorization", "Bearer " + token("citizen@test.local"))
            .contentType(MediaType.APPLICATION_JSON).content("{\"status\":\"UNDER_REVIEW\"}"))
            .andExpect(status().isForbidden());
    }

    private String token(String email) throws Exception {
        String body = mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
            .content(json.writeValueAsString(java.util.Map.of("email", email, "password", "Password@123"))))
            .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        JsonNode node = json.readTree(body);
        return node.get("token").asText();
    }

    private String reportJson() throws Exception {
        return json.writeValueAsString(java.util.Map.of(
            "categoryId", theft.getId(), "title", "Test incident report",
            "description", "This is a complete synthetic incident description used to verify report creation.",
            "incidentDate", LocalDate.now().toString(), "incidentTime", "12:30",
            "location", "12 Test Street", "city", "Test City", "area", "Central"
        ));
    }
}
