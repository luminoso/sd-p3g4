# Rope Game Distributed Simulation

## What is Rope Game?

One of the most popular traditional games is the Game of the Rope. Two teams of contestants face each other on a playground trying to decide which one is the strongest by pulling at opposite ends of a rope. If both teams are equally strong, the rope does not move, a standstill occurs and there is a draw; if however one of the teams is stronger than the other, the rope moves in the direction of the stronger team so much faster as the strength difference is larger and this team wins.

A variation of this game will be assumed here. A match is composed of three games and each game may take up to six trials. A game win is declared by asserting the position of a mark placed at the middle of the rope after six trials. The game may end sooner if the produced shift is greater or equal to four length units. We say in this case that the victory was won by knock out, otherwise, it will be a victory by points.

A team has five elements, but only three compete at each trial. Member selection for the trial is carried out by the team's coach. He decides who will join for next trial according to some predefined strategy.
Each contestant will lose one unit of strength when he is pulling the rope and will gain one unit when he is seating at the bench. Somehow the coach perceives the physical state of each team member and may use this information to substantiate his decision.

In order to ensure rules compliance, there is a referee. She has full control of the procedure and
decides when to start a new game or a trial within the game. She also decides when a game is over and declares who has won a game or the match.

One aims for a distributed solution with multiple information sharing regions that has to be written in Java, run and terminate once the game is finished.
A logging file, which describes the evolution of the internal state of the problem in a clear and precise way, must be included.

## About this simulation

There are three solutions presented in this repository

1.	Using java’s reentrant locks for managing shared variables and concurrency. (explicit monitors)
2.	Implemented a client-server approach that active entities trade messages with passive entities (playground, bench, referee site) -  This solution tries to demonstrate a simple simulation how java’s RMI works.
3.	Implementation of a full java RMI solution where shared areas are registered and players

## Requirements
This simulation was tested using Java 8.
## How to run the simulation
(note that each branch has its own specific instructions)

Each simulation produces a logging file with all the details.

1. RopeGame main. (contestants, coaches are spawned automatically)
2. Run RopeGame main with "RF" flag to launch Referee and start the match.

## Threads life cycle (active entities)
### Referee life cycle
![Referee life cycle](https://github.com/luminoso/sd-p3g4/raw/Monitors/doc/rf_lifecycle.png)
### Coach life cycle
![Coach life cycle](https://github.com/luminoso/sd-p3g4/raw/Monitors/doc/coach_lifecycle.png)
### Contestants life cycle
![Contestants life cycle](https://github.com/luminoso/sd-p3g4/raw/Monitors/doc/contestants_lifecycle.png)

## Passive entities 
- Contestants Bench: where contestants sit down and wait for their turn to play
- Referee Site: controls gaming score
- Playground: controls match score and progress
- General Repository Information: logging results and entities

## Project context
This problem as proposed in curricular unit at *Universidade de Aveiro – Sistemas Distribuidos 2016*, original idea from Pedro Mariano. Each solution is documents in the respective branch at the pdf in the doc/ directory.
