# AI Assistant Service with SAP BTP, SAP HANA DB, and Multitenancy

This project is a Spring Boot application that serves as an AI assistant, integrating with the OpenAI API for natural language processing. It stores chat memory in an SAP HANA database and supports multitenancy, leveraging SAP Business Technology Platform (BTP) for seamless cloud integration.

## Table of Contents

- [Features](#features)
- [Setup](#setup)
- [Usage](#usage)
- [Multitenancy Implementation](#multitenancy-implementation)

## Features

- Integration with OpenAI API for AI assistant functionality.
- Persistence of chat history in SAP HANA database.
- Multitenant architecture to support multiple tenants.
- Seamless integration with SAP BTP for cloud deployment.

## Setup

1. **Build MTA archive:**

    ```bash
    mbt build
    ```

2. **Deploy to CF:**

    ```bash
    cf deploy <path_to_archive>
    ```
   
## Usage

- **Chat with the AI Assistant:**

  Send a POST request to `/api/v1/chat` with your message:

    ```bash
    curl -X POST http://localhost:8080/api/v1/chat -H "Content-Type: application/json" -d '{"message": "Hello, AI!"}'
    ```

## Multitenancy Implementation

Multitenancy is achieved by:

1. **Database Schema**: Using separate schemas for each tenant.
2. **Configuration**: Configuring Hibernate and Spring Data to support multitenancy.