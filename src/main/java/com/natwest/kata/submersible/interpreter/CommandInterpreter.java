package com.natwest.kata.submersible.interpreter;

import com.natwest.kata.submersible.domain.Probe;

import java.util.*;
import java.util.function.Consumer;

public class CommandInterpreter {

    // ---- Only F/B are movement commands now ----
    private static final Set<Character> MOVE_CMDS = Set.of('F', 'B');

    // ---- Mapping commands to Probe actions ----
    private static final Map<Character, Consumer<Probe>> EXECUTORS;
    static {

        // MOVEMENT COMMANDS

        EXECUTORS = Map.of('F', Probe::moveForward, 'B', Probe::moveBackward,

                // VERTICAL ROTATION COMMANDS
                'U', Probe::turnUp, 'D', Probe::turnDown,

                // HORIZONTAL ROTATION COMMANDS
                'L', Probe::turnLeft, 'R', Probe::turnRight);
    }

    public ExecutionResult execute(List<String> commands, Probe probe) {

        List<String> cmds = (commands == null) ? Collections.emptyList() : commands;
        List<InvalidCommand> invalids = new ArrayList<>();

        int blocked = 0;

        for (int i = 0; i < cmds.size(); i++) {
            String token = cmds.get(i);

            if (token == null || token.length() != 1) {
                invalids.add(new InvalidCommand(i, token, "UNKNOWN_COMMAND"));
                continue;
            }

            char c = Character.toUpperCase(token.charAt(0));
            Consumer<Probe> action = EXECUTORS.get(c);

            if (action == null) {
                invalids.add(new InvalidCommand(i, token, "UNKNOWN_COMMAND"));
                continue;
            }

            // Movement commands (F and B)
            if (MOVE_CMDS.contains(c)) {
                blocked += tryMove(probe, action);
            }
            else {
                // Rotation commands (L, R, U, D)
                action.accept(probe);
            }
        }

        int total = cmds.size();
        return new ExecutionResult(total, total, blocked, invalids);
    }

    private int tryMove(Probe probe, Consumer<Probe> move) {
        int bx = probe.getX();
        int by = probe.getY();
        int bz = probe.getZ();

        move.accept(probe);

        // Check if probe moved
        return (bx != probe.getX() || by != probe.getY() || bz != probe.getZ()) ? 0 : 1;
    }
}
