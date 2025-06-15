# Traffic Light Simulation System

## Overview

This project implements a simple traffic intersection simulation system with multiple traffic management algorithms. The system simulates a four-way intersection with configurable lane widths.

## Architecture

- **Simulation**: Manages the overall simulation state, config and execution
- **Algorithm**: Algorithms
- **Model**: Represents vehicles with start/end roads and lane assignments

## Traffic Control Algorithms

### 1. First In First Out (FIFO)
**Type**: Multi-lane compatible

The basic FIFO algorithm processes vehicles based on arrival order using a priority system. It operates in 6 distinct steps:

1. SOUTH ↔ EAST traffic
2. SOUTH ↔ NORTH traffic
3. SOUTH ↔ WEST traffic
4. EAST ↔ WEST traffic
5. WEST ↔ NORTH traffic
6. NORTH ↔ EAST traffic

**Key Features**:
- Priority is given to the longest-waiting vehicle
- Processes complementary directions simultaneously

### 2. First In First Out Smart
**Type**: Single-lane only (roadWidth = 1)

An enhanced version of FIFO that uses collision detection to maximize intersection throughput while maintaining fairness.

**Key Features**:
- Priority is given to the longest-waiting vehicle
- Collision detection between vehicle paths
- Simultaneous processing of non-conflicting movements

### 3. Max Throughput
**Type**: Multi-lane compatible

Optimizes for maximum vehicle throughput by greedy-selecting the traffic step that can process the most vehicles.

**Key Features**:
- Selects a step with the highest vehicle count
- Processes complementary directions simultaneously


### 4. Dynamic Algorithm
**Type**: Multi-lane compatible

Adaptive algorithm that switches between FIFO and Max Throughput based on traffic density.

**Key Features**:
- Low traffic: Uses FIFO for fairness
- High traffic: Uses Max Throughput for efficiency
- Traffic density threshold: default is 10 vehicles per 4 lanes


## Emergency Vehicle Support

Every algorithm provides special handling for emergency vehicles:
- Vehicles with IDs starting with "emergency" get immediate priority
- Ensures emergency vehicles' lane is processed until emergency vehicle can pass

## Configuration

### Algorithm Selection
```json
{
  "type": "config",
  "algorithm": "firstInFirstOut|smart|maxThroughput|dynamic",
  "roadWidth": 1
}
```

### Supported Algorithms:
- `firstInFirstOut`: Basic FIFO algorithm
- `smart`: Enhanced FIFO with collision detection (requires roadWidth = 1)
- `maxThroughput`: Throughput-optimized algorithm
- `dynamic`: Adaptive algorithm switching

### Road Width Constraints:
- **Smart Algorithm**: Only supports roadWidth = 1
- **Other Algorithms**: Support any roadWidth ≥ 1
- Invalid roadWidth values are automatically corrected to 1

## Running the project

Java 17+ is required for running this project (I used Microsoft OpenJDK 17.0.5)

Use **runner.sh** with an input JSON file as the first argument and output JSON file as the second argument

### Input Format
```json
{
  "commands": [
    {
      "type": "config",
      "algorithm": "smart",
      "roadWidth": 1
    },
    {
      "type": "addVehicle",
      "vehicleId": "vehicle1",
      "startRoad": "NORTH",
      "endRoad": "SOUTH",
      "laneNumber": 0
    },
    {
      "type": "step"
    }
  ]
}
```

### Output Format
```json
{
  "stepStatuses": [
    {
      "leftVehicles": ["vehicle1", "vehicle2"]
    }
  ]
}
```


