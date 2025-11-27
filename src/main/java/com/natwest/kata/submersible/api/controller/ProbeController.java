package com.natwest.kata.submersible.api.controller;

import com.natwest.kata.submersible.api.dto.RunRequest;
import com.natwest.kata.submersible.api.dto.RunResponse;
import com.natwest.kata.submersible.api.error.ErrorResponse;
import com.natwest.kata.submersible.service.ProbeRunService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/probe")
@Tag(name = "Submersible Probe", description = "Control and simulate a submersible probe moving in a 3D grid with obstacles.")
public class ProbeController {

    private final ProbeRunService service;

    public ProbeController(ProbeRunService service) {
        this.service = service;
    }

    @PostMapping("/run")
    @Operation(summary = "Execute a full probe command run", description = """
            Accepts grid size, starting position, facing direction, command list and obstacles.
            Returns final coordinates, visited path, and execution statistics.
            """)
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Probe run completed successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RunResponse.class))), @ApiResponse(responseCode = "400", description = "Malformed JSON or bean validation failure.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))), @ApiResponse(responseCode = "422", description = "Business validation error (e.g., obstacle out of bounds).", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))})
    public ResponseEntity<RunResponse> run(@RequestBody @Valid RunRequest request) {
        var resp = service.run(request);
        return ResponseEntity.ok(resp);
    }
}
