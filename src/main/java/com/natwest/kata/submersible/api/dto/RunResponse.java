package com.natwest.kata.submersible.api.dto;

import java.util.List;

public class RunResponse {
    private FinalStateDto finalState;
    private List<String> visited;
    private ExecutionDto execution;
    private GridDto grid;
    private String summary;

    public FinalStateDto getFinalState() { return finalState; }
    public void setFinalState(FinalStateDto finalState) { this.finalState = finalState; }

    public List<String> getVisited() { return visited; }
    public void setVisited(List<String> visited) { this.visited = visited; }

    public ExecutionDto getExecution() { return execution; }
    public void setExecution(ExecutionDto execution) { this.execution = execution; }

    public GridDto getGrid() { return grid; }
    public void setGrid(GridDto grid) { this.grid = grid; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
}
