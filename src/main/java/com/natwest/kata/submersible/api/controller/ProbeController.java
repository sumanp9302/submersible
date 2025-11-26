
package com.natwest.kata.submersible.api.controller;

import com.natwest.kata.submersible.api.dto.RunRequest;
import com.natwest.kata.submersible.api.dto.RunResponse;
import com.natwest.kata.submersible.service.ProbeRunService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/probe")
public class ProbeController {

    private final ProbeRunService service;

    public ProbeController(ProbeRunService service) {
        this.service = service;
    }

    @PostMapping("/run")
    public ResponseEntity<RunResponse> run(@RequestBody @Valid RunRequest request) {
        var resp = service.run(request);
        return ResponseEntity.ok(resp);
    }
}
