Step 1: Decide the state model for the probe REST API


The options
1) Stateless (one-shot execution)
•	Flow: Client sends the entire scenario in one request—grid (dims + obstacles), start (x,y,z), direction, and the command batch.
•	Server: Executes the commands immediately and returns the final state + visited coordinates; no server-side session retained.
•	Pros
o	Simple, deterministic, and easy to test (mirrors your current unit tests).
o	No concurrency/session management, no memory leak risks.
o	Naturally idempotent if the same payload is retried.
•	Cons
o	If a client wants to run commands incrementally, they must resend the whole scenario each time.
o	No built-in notion of a “long-running probe” or historical state on the server.
2) Stateful (probe sessions)
•	Flow: Client initializes a probe via /init → gets probeId; then sends command batches to /probes/{id}/commands over time.
•	Server: Retains probe state (position, direction, visited path, obstacles) in-memory (or persisted) per probeId.
•	Pros
o	Supports incremental control; multiple batches over time.
o	Allows querying current state and history without resending the grid/obstacles.
•	Cons
o	Requires session storage, lifecycle (TTL/cleanup), concurrency control, and idempotency handling.
o	More moving parts to test and operate.
3) Hybrid (ephemeral state with TTL)
•	Same as stateful, but sessions auto-expire after a short period (e.g., 10–30 minutes). Useful if clients prefer incremental commands but you want bounded resource usage.
________________________________________
Recommendation for your current scope
Given your existing tests and domain (which already validate start, movement, bounds, obstacles, and visited path) and the requirement to “print a summary of the coordinates visited,” the Stateless (one-shot) model is the most appropriate starting point:
•	It aligns perfectly with your current unit tests and expected behavior.
•	It’s simpler, enabling rapid completion of steps 2–5 (API contract, DTOs, interpreter, service, controller) via TDD.
•	You can easily extend to Stateful later if stakeholders need incremental control or multiple probes managed simultaneously.
Decision: Proceed with a Stateless API first, then add a Stateful extension behind a feature flag or a second endpoint contract if needed.
________________________________________
Scope boundaries (for the stateless decision)
•	Server retains no state: Every request is self-contained—grid + obstacles + start + direction + command list.
•	Response includes:
o	Final (x,y,z,direction)
o	Full visited coordinates (including start; only successful moves)
o	Blocked moves count (attempts rejected due to bounds/obstacles)
o	(Optional) List of invalid commands encountered
•	Idempotency: Same request → same response; safe to retry.
•	Concurrency: No session-level concurrency concerns (each request is isolated).
________________________________________
How this affects the next steps
•	Step 2 (API contract): We’ll design a single primary endpoint:
POST /api/probe/run
Body: { grid: {width,height,depth}, obstacles: [...], start: {x,y,z}, direction, commands: [...] }
Response: { finalState, visited, blockedMoves, invalidCommands }
•	Optional Future (Stateful): If required later:
o	POST /api/probe/init → returns probeId
o	POST /api/probe/{id}/commands → incremental execution
o	GET /api/probe/{id} / GET /api/probe/{id}/visited
o	Add in-memory store, TTL cleanup, concurrency locking per probeId, and idempotency via requestId.
________________________________________
Acceptance criteria for Step 1 (decision)
•	Decision documented: Stateless model chosen.
•	Clear statement of response contents and error semantics (blocked moves, invalid commands).
•	Agreement that subsequent steps will implement the stateless contract first, with the option to extend to stateful later.

