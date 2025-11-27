package com.natwest.kata.submersible.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Full result of a probe run, including final state, path and execution metrics.")
public class RunResponse {

    @Schema(description = "Final state of the probe.")
    private FinalStateDto finalState;

    @Schema(description = "Execution statistics for the run.")
    private ExecutionDto execution;

    @Schema(description = "Grid configuration used for this run.")
    private GridDto grid;

    @Schema(
            description = "List of all positions the probe visited, in order, formatted as '(x,y,z)'.",
            example = "[\"(0,0,0)\", \"(0,1,0)\", \"(1,1,0)\"]"
    )
    private List<String> visited;

    @Schema(
            description = "Human-readable summary describing the run outcome.",
            example = "Visited 4 positions on a 5x5x5 grid. Final position: (1,1,1) facing NORTH. Total commands: 5, executed: 5, blocked moves: 0, invalid commands: 1."
    )
    private String summary;

    public FinalStateDto getFinalState() { return finalState; }
    public void setFinalState(FinalStateDto finalState) { this.finalState = finalState; }

    public ExecutionDto getExecution() { return execution; }
    public void setExecution(ExecutionDto execution) { this.execution = execution; }

    public GridDto getGrid() { return grid; }
    public void setGrid(GridDto grid) { this.grid = grid; }

    public List<String> getVisited() { return visited; }
    public void setVisited(List<String> visited) { this.visited = visited; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
}
