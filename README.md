# Diamond Circle

## Project Motivation

The primary motivation behind the "DiamondCircle" game project is to explore and demonstrate concurrent programming concepts using threads in Java. By implementing a multi-player board game with automated figure movements and dynamic interactions, the project aims to showcase the power and challenges of concurrency in game development.

Using threads in Java, the project will enable the simultaneous movement of multiple figures on the game board, reflecting real-time gameplay interactions among players. Concurrent programming techniques will be employed to handle figure movements, diamond placements, and card drawing concurrently, providing a seamless and interactive gaming experience.

## Getting started

### Key Dependencies & Platforms

- [Java 11](https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html): Make sure you have Java 11 installed on your machine. You can download and install it from the official Oracle website or use a Java Development Kit (JDK) distribution suitable for your operating system.

- [Eclipse IDE](https://www.eclipse.org/ide/): I recommend using Eclipse IDE for Java development. Make sure you have Eclipse IDE installed on your machine. You can download it from the Eclipse website and follow the installation instructions.

## Key Features

- `Thread Management`: Utilizing threads in Java, the project will demonstrate how to efficiently manage multiple concurrent tasks, such as updating player positions, ghost figure movements, and diamond placements.

- `Synchronization`: To ensure data integrity and avoid race conditions, the game will employ synchronization mechanisms to coordinate shared resources, such as the game board and player information.

- `Parallelism`: The implementation of concurrent threads will enable parallel processing, allowing multiple actions to occur simultaneously, enhancing the game's responsiveness and user experience.

- `Thread Safety`: By focusing on thread safety, the project will showcase how to design classes and methods to be thread-safe, preventing data corruption and inconsistencies during concurrent operations.

- `Real-time Interactions`: Concurrent programming will enable real-time interactions between players, as each figure moves independently based on the drawn cards and user inputs, making the gameplay engaging and interactive.