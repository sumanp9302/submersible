package com.natwest.kata.submersible.interpreter;

import com.natwest.kata.submersible.domain.Probe;

import java.util.*;
import java.util.function.Consumer;

public class CommandInterpreter {

    private static final Map<Character, Consumer<Probe>> EXECUTORS;
    private static final Set<Character> MOVE_CMDS = new HashSet<>(Arrays.asList('F', 'B', 'U', 'D'));

    static {
        Map<Character, Consumer<Probe>> m = new HashMap<>();
        // Movement
        m.put('F', Probe::moveForward);
        m.put('B', Probe::moveBackward);
        m.put('U', Probe::moveUp);
        m.put('D', Probe::moveDown);
        // Turns
        m.put('L', Probe::turnLeft);
        m.put('R', Probe::turnRight);
        EXECUTORS = Collections.unmodifiableMap(m);
    }

    public ExecutionResult execute(List<String> commands, Probe probe) {
        final List<String> cmds = (commands == null) ? Collections.emptyList() : commands;
        final List<InvalidCommand> invalids = new ArrayList<>();
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
            if (MOVE_CMDS.contains(c)) {
                blocked += tryMove(probe, action);
            } else {
                action.accept(probe); // turn commands never blocked
            }
        }

        int total = cmds.size();
        return new ExecutionResult(total, total, blocked, invalids);
    }


    private int tryMove(Probe probe, Consumer<Probe> move) {
        final int bx = probe.getX(), by = probe.getY(), bz = probe.getZ();
        move.accept(probe);
        final int ax = probe.getX(), ay = probe.getY(), az = probe.getZ();
        // Success if any coordinate changed; otherwise blocked
        return (bx != ax || by != ay || bz != az) ? 0 : 1;
    }

}
