## E-Commerce Backend built with Microservices Architecture:

## Introduction:

This project demonstrates the backend architecture of an e-commerce platform 
implemented using a microservices-based approach. It serves as a reference for 
developers to understand common challenges encountered while designing e-commerce 
systems and the architectural solutions used to address them.

Additionally, the project showcases the implementation of key microservices 
patterns, including service discovery, API gateway, asynchronous messaging, the 
outbox pattern, and the database-per-service pattern. These patterns are reusable 
and can be applied when designing other microservices-based systems.

## Technology Stack:
- Language: Java
- Framework: Spring Boot
- Database: MySQL
- ORM Framework: Hibernate
- Messaging: Apache Kafka
- Caching: Redis
- Scripting: Lua
- Build Tool: Maven
- Data Format: JSON for API communication
- Version Control: Git

## List of Microservices:
- Discovery Service
- Gateway Service
- User Service
- Cart Service
- Product Service
- Inventory Service
- Order Service

## Service Details:

** Service Name: Discovery Service **

** Responsibility: **
- Acts as a centralized registry for service locations.
- Allows services to discover each other without hardcoding IP address or port.
- Maintains up-to-date map of active services through periodic hearbeats sent by 
the registered services.

** Technology Used: **
- Netflix Eureka Server

___

** Service Name: Gateway Service **

** Responsibility: **
- Acts as a single entry point for all client requests to the services.
- Inspects and route requests to the correct downstream service based on route 
configuration.

** Technology Used: **
- Spring Cloud Gateway
- Netflix Eureka Client

___

** Service Name: User Service **

** Responsibility: **
- Manages basic user profile information.
- Maintains user status (active / inactive).
- Provides user existence and status validation for downstream services.

** Technology Used: **
- Netflix Eureka Client
- MySQL
- Spring MVC
- Spring Data JPA

[User Service API Documentation](./ecomm-user-service/user-service-api-details.txt)
___

** Service Name: Cart Service **

** Responsibility: **
- Tracks and manages the cart state for users.
- Persists cart data in Redis to maintain state across sessions and logins.
- Automatically cleans up expired carts.

** Technology Used: **
- Netflix Eureka Client
- Spring MVC
- Spring Data Redis

[Cart Service API Documentation](./ecomm-cart-service/cart-service-api-details.txt)
___

** Service Name: Product Service **

** Responsibility: **
- Manages the product catalog and product pricing.
- Provides products price validation to downstream services.
- Tracks and persists product price history.
- Publishes product-related domain events to notify downstream services 
asynchronously.

** Technology Used: **
- Netflix Eureka Client
- MySQL
- Spring MVC
- Spring Data JPA
- Apache Kafka

[Product Service API Documentation](./ecomm-product-service/product-service-api-details.txt)
___

** Service Name: Inventory Service **

** Responsibility: **
- Manages product inventory levels and reservation lifecycle.
- Provides near real-time product stock availability using Redis.
- Reserves inventory items upon requests from the Order Service.
- Cleans up expired and unused reservations to free available stock.
- Tracks and persists inventory level changes.
- Consumes product-related domain events (e.g., product creation) to 
initialize inventory records.
- Consumes order-related domain events (eg., order confirmation, 
order cancellation) to adjust reservation status and inventory stock 
levels.

** Technology Used: **
- Netflix Eureka Client
- MySQL
- Spring MVC
- Spring Data JPA
- Apache Kafka
- Spring Data Redis

___

** Service Name: Order  **

** Responsibility: **
- Manages order creation and cancellation lifecycle.
- Synchronously coordinates with the Inventory Service to reserve stock 
before confirming an order.
- Publishes order-related domain events (e.g., order created, order cancelled) 
to notify downstream services asynchronously.

** Technology Used: **
- Netflix Eureka Client
- MySQL
- Spring MVC
- Spring Data JPA
- Apache Kafka
