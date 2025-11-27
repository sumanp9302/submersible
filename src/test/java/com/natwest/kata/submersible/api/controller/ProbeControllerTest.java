package com.natwest.kata.submersible.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.natwest.kata.submersible.api.dto.CoordinateDto;
import com.natwest.kata.submersible.api.dto.GridDto;
import com.natwest.kata.submersible.api.dto.RunRequest;
import com.natwest.kata.submersible.enums.Direction;
import com.natwest.kata.submersible.service.ProbeRunService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProbeController.class)
@Import({ProbeRunService.class})
class ProbeControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper mapper;

    @Test
    void runShouldReturnFinalStateVisitedAndExecutionMetrics() throws Exception {
        var req = new RunRequest();
        req.setGrid(new GridDto(6, 6, 6));
        req.setObstacles(List.of(new CoordinateDto(3, 3, 5)));
        req.setStart(new CoordinateDto(2, 3, 5));
        req.setDirection(Direction.EAST);
        req.setCommands(List.of("F", "F", "L", "B"));

        mvc.perform(post("/api/probe/run")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.finalState.direction").value("NORTH"))
                .andExpect(jsonPath("$.visited[0]").value("(2,3,5)"))
                .andExpect(jsonPath("$.execution.totalCommands").value(4))
                .andExpect(jsonPath("$.execution.invalidCommands").isArray());
    }

    @Test
    void runShouldReturn422ForOutOfBoundsStart() throws Exception {
        var req = new RunRequest();
        req.setGrid(new GridDto(5, 5, 5));
        req.setObstacles(List.of());
        req.setStart(new CoordinateDto(6, 0, 0)); // invalid
        req.setDirection(Direction.NORTH);
        req.setCommands(List.of("F"));

        mvc.perform(post("/api/probe/run")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.error.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.error.message").exists());
    }

    @Test
    void runShouldAcceptInvalidCommandsAndReportThem() throws Exception {
        var req = new RunRequest();
        req.setGrid(new GridDto(5, 5, 5));
        req.setObstacles(List.of());
        req.setStart(new CoordinateDto(0, 0, 0));
        req.setDirection(Direction.NORTH);
        req.setCommands(List.of("F", "X", "R", "Z", "B"));

        mvc.perform(post("/api/probe/run")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.execution.invalidCommands.length()").value(2))
                .andExpect(jsonPath("$.execution.blockedMoves").isNumber());
    }

    @Test
    void run_returnsNonEmptySummary() throws Exception {
        RunRequest req = new RunRequest();
        req.setGrid(new GridDto(5, 5, 5));
        req.setObstacles(List.of());
        req.setStart(new CoordinateDto(0, 0, 0));
        req.setDirection(Direction.NORTH);
        req.setCommands(List.of("F", "R", "F"));

        mvc.perform(post("/api/probe/run")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.summary").isNotEmpty());
    }

    @Test
    void runShouldReturn400ForMalformedJsonRequest() throws Exception {
        // given - intentionally malformed JSON body
        String malformedJson = "{ invalid json";

        mvc.perform(post("/api/probe/run")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.error.message").value("Malformed JSON request"));
    }

    @Test
    void runShouldReturn400WhenMandatoryFieldIsMissing() throws Exception {
        // given - missing grid (violates @NotNull on RunRequest.grid)
        RunRequest req = new RunRequest();
        req.setGrid(null);
        req.setStart(new CoordinateDto(0, 0, 0));
        req.setDirection(Direction.NORTH);
        req.setCommands(List.of("F"));
        req.setObstacles(Collections.emptyList());

        mvc.perform(post("/api/probe/run")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())                       // 400 instead of 422
                .andExpect(jsonPath("$.error.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.error.message").value("Payload validation failed"))
                .andExpect(jsonPath("$.error.details[0].field").value("grid"))
                .andExpect(jsonPath("$.error.details[0].issue").value("must not be null"));
    }

    @Test
    void runShouldReturn422WhenObstacleIsOutOfBounds() throws Exception {
        // given: grid 5x5x5 but obstacle at (10,10,10)
        RunRequest req = new RunRequest();
        req.setGrid(new GridDto(5, 5, 5));
        req.setStart(new CoordinateDto(0, 0, 0));
        req.setDirection(Direction.NORTH);
        req.setCommands(List.of("F"));

        // obstacle clearly out of range
        req.setObstacles(List.of(
                new CoordinateDto(10, 10, 10)
        ));

        // when / then: expect validation error (422) instead of 500
        mvc.perform(post("/api/probe/run")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.error.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.error.message").value("Obstacle out of bounds"));
    }
}
