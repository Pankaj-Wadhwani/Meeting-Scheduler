# Meeting Scheduler Application

## Overview

The Meeting Scheduler Application is a system for scheduling and managing meetings. It allows users to create meetings, specify participants, and assign rooms. Users can also retrieve meeting details by ID.

## Features

- Create meetings with a title, start time, end time, participants, and room.
- Retrieve meeting details by ID.
- Store meeting information in a relational database.

## Architecture

The application follows a layered architecture with the following components:

- **API Layer:** Handles HTTP requests and responses.
- **Service Layer:** Contains business logic and operations.
- **Data Access Layer:** Interacts with the database to perform CRUD operations.
- **Database:** Stores meeting, user, and room data.

## Technologies

- **Back-end:** Java, Spring Boot
- **Database:** MySQL (or any other relational database)
- **Testing:** JUnit 4, Spring Boot Test
- **Build Tool:** Maven 

## Installation

### Prerequisites

- Java 11 or later
- Maven
- OracleDB

### Clone the Repository

```bash
git clone https://github.com/yourusername/meeting-scheduler.git

