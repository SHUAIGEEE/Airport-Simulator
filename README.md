# Airport Simulator

This project simulates the operation of Asia Pacific Airport, from landing requests to take-off, using concurrency concepts. Developed in Java using IntelliJ IDEA, this simulator manages multiple activities concurrently to replicate real-world airport operations.

## Project Overview

The simulation models nine key events in the airport operation process, from when an aircraft is airborne until it takes off again. It leverages Java's concurrency features to simulate these activities simultaneously, ensuring safe and efficient handling of airport operations.

## Features
- **Concurrency Simulation**: Utilizes Java concurrency to simulate multiple processes such as landing, taxiing, refueling, and take-off.
- **Event-Driven Architecture**: Models the flow of events involved in managing airport operations.
- **Safety Considerations**: Ensures safe operations using synchronization techniques to avoid conflicts in shared resources (e.g., runways).
- **Java-Based**: Built using Java and developed in IntelliJ IDEA, ensuring high compatibility and performance.

## Activity Flow
1. **Landing Request**: The aircraft requests permission to land.
2. **Landing**: The aircraft lands on the runway.
3. **Docking**: The aircraft docks at one of the three gates.
4. **Disembarking**: Passengers disembark at the gate.
5. **Refilling and Cleaning**: The aircraft requests for refilling the supplies and cleaning.
6. **Refueling**: The aircraft refuels in preparation for the next flight.
7. **Embarking**: New passengers board the aircraft.
8. **Undocking**: The aircraft undocks from the gate to the runway.
9. **Take-off**: The aircraft takes off and exits the system.

## Concurrency Concepts Used
- **Thread Management**: Each activity (e.g., landing, docking) is handled by separate threads running concurrently.
- **Synchronization**: Runway and gates are shared resources, managed using locks to prevent conflicts.
- **Mutexes and Semaphores**: Used to control access to critical sections, ensuring safe and conflict-free operations.
- **Deadlock Prevention**: Designed to avoid deadlock scenarios through careful resource allocation and release.
