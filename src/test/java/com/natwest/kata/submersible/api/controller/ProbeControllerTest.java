
package com.natwest.kata.submersible.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.natwest.kata.submersible.api.dto.*;
import com.natwest.kata.submersible.enums.Direction;
import com.natwest.kata.submersible.service.ProbeRunService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProbeController.class)
@Import({ProbeRunService.class})
class ProbeControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;

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
        req.setGrid(new GridDto(5,5,5));
        req.setObstacles(List.of());
        req.setStart(new CoordinateDto(0,0,0));
        req.setDirection(Direction.NORTH);
        req.setCommands(List.of("F", "X", "R", "Z", "B"));

        mvc.perform(post("/api/probe/run")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.execution.invalidCommands.length()").value(2))
                .andExpect(jsonPath("$.execution.blockedMoves").isNumber());
    }
}
