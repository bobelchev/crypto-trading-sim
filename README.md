### Requirements

- Java 17.0.6 or newer (Java 17+ required)
- Node.js 18.18.2 or newer
- npm 9.8.1 or newer
- **Docker**:
    - Docker Engine `20.10.22` or newer
    - Docker Compose `v2.15.1` or newer
    - At least **4 GB of free system RAM** recommended for Kafka/Zookeeper containers

> ✅ Ensure Docker is running before starting Kafka.
> - On **Windows/macOS**: Start Docker Desktop
> - On **Linux**: Run `sudo systemctl start docker`

### Running the project

1. Navigate to the `krakenservice` folder that contains the `docker-compose.yml` file:

```bash
cd krakenservice
docker-compose up -d
```
2. Navigate to the krakenservice folder and start the Spring Boot Kraken service using PS on Windows:
   ```bash
      cd krakenservice/kraken-ws-service
      .\mvnw spring-boot:run        # PowerShell
      # or
      mvnw spring-boot:run          # CMD
   ```
3. Navigate to the eureka-server folder and start the Spring Boot Eureka discovery service using PS on Windows:
   ```bash
      cd eureka-server/eureka-server
      .\mvnw spring-boot:run        # PowerShell
      # or
      mvnw spring-boot:run          # CMD
   ```
4. Navigate to the marked-data-streamer folder and start the Spring Boot Market Data service using PS on Windows:
   ```bash
      cd market-data-streamer/market-data-streamer
      .\mvnw spring-boot:run        # PowerShell
      # or
      mvnw spring-boot:run          # CMD
   ```
5. Open new terminal, navigate to the backend folder and start the Spring Boot server using PS on Windows:

   ```bash
   cd backend/crypto-trading-sim
   .\mvnw spring-boot:run
   ```
   Or if using CMD:
   ```bash
    mvnw spring-boot:run
   ```
6. Open new terminal, navigate to the gateway-crypto folder and start the Spring Boot server using PS on Windows:

   ```bash
   cd gateway-crypto/gateway-crypto
   .\mvnw spring-boot:run
   ```
   Or if using CMD:
   ```bash
    mvnw spring-boot:run
   ```
7. Open a new terminal, navigate to the frontend folder, and start the React app:

   ```bash
   cd frontend
   npm install    # Only needed the first time
   npm start      # Or: npm run dev
   ```
The eureka server will run on http://localhost:8761

The gateway will run on http://localhost:8080

The backend will run on http://localhost:8081

The market data streamer service  will run on http://localhost:8082

The kraken service will run on http://localhost:8083

The frontend will run on http://localhost:5173 or fallback to http://localhost:5174
### Demo

Watch the demo video on [Google Drive](https://drive.google.com/file/d/1k9GvioiWBQe2OWV7eZ--5DymZnJuiDkN/view?usp=drive_link)

### Screenshots


<h4>Initial Application Screen (Top 20 Crypto Prices)</h4>
<img src="img/initialScreen.png" alt="Top 20 Crypto Prices" width="400"/>

<hr/>

<h4>Buy Interface</h4>
<img src="img/buyInterface.png" alt="Buy Interface" width="400"/>

<hr/>

<h4>Sell Interface</h4>
<img src="img/sellInterface.png" alt="Sell Interface" width="400"/>

<hr/>

<h4>Updated Balance After Transaction</h4>
<img src="img/initialScreen.png" alt="Before Transaction" width="300"/>
<img src="img/buyInterface.png" alt="Transaction In Progress" width="300"/>
<img src="img/interfaceAfterBuy.png" alt="After Transaction" width="300"/>
### Development Process

1. **Test-Driven Development (TDD) for the Backend**
    Applied TDD for the backend - first stub implementation was defined and then corresponding unit tests were implemented. Aimed for 100% (public) method coverage.
    First repositories and model classes were implemented and tested. Then followed by the service and controller layer.
   > **TDD Commit Examples:**  
   > [Commit 1: Define User & Transaction Repositories](https://github.com/bobelchev/crypto-sim/commit/dbb4594e9dcd0279473dcf389898fae826f77aa6)  
   > [Commit 2: Define CryptoHoldingRepository](https://github.com/bobelchev/crypto-sim/commit/45a4adcafc9b0eeae463841c47786c4f11b55096)  
   > [Commit 3: Implement Unit tests](https://github.com/bobelchev/crypto-sim/commit/45a4adcafc9b0eeae463841c47786c4f11b55096)  
   > [Commit 4: Implement User Repository class](https://github.com/bobelchev/crypto-sim/commit/f1f4e654f1178b225294f874bbf8788089c3d2b3)  
   > [Commit 5: Implement Transaction Repository class](https://github.com/bobelchev/crypto-sim/commit/52f4d371d29f2f8f312774ada86c312b9bc22c83)

2. **Branches**
    Once a stable minimally functional backend was developed the project branched out to develop the fronted and the WS connections.
   > **Branches:**
   > - [`frontend`](https://github.com/bobelchev/crypto-trading-sim/tree/frontend) – Implements the React frontend interface
   > - [`feature/kraken-ws-client`](https://github.com/bobelchev/crypto-trading-sim/tree/feature/kraken-ws-client) – Connects to Kraken WebSocket API and exposes real-time data

### 3. **Pull Requests**

| Feature                                               | Branch                     | PR Link                                                      |
|--------------------------------------------------------|----------------------------|--------------------------------------------------------------|
| Kraken WebSocket Integration                          | `feature/kraken-ws-client` | [#5](https://github.com/bobelchev/crypto-trading-sim/pull/5) |
| Frontend UI                                           | `frontend`                 | [#6](https://github.com/bobelchev/crypto-trading-sim/pull/6) |
| Extract Kraken WebSocket to Kafka Producer Service    | `service/kraken-ws`        | [#7](https://github.com/bobelchev/crypto-trading-sim/pull/7) |
| Introduce API Gateway using Spring Cloud Gateway      | `gateway/api-gateway`      | [#8](https://github.com/bobelchev/crypto-trading-sim/pull/8) |
| Decouple WebSocket Market Data Streaming to New Service | `service/market-data-streamer` | [#9](https://github.com/bobelchev/crypto-trading-sim/pull/9) |


### 4. **Refactoring Code Smells**

Several core design and maintainability issues were addressed:

| Code Smell Resolved     | Solution Implemented                                           | Example Commit |
|-------------------------|----------------------------------------------------------------|----------------|
| 🔍 Hidden Dependencies  | Replaced field injection with **constructor injection**        | [Commit #e3a2675](https://github.com/bobelchev/crypto-trading-sim/commit/e3a26750546df5a8dad161da4cc48967cfa47068) |
| 🔢 Long Parameter Lists | Grouped method arguments into DTOs or value objects            | [Commit #de231ea](https://github.com/bobelchev/crypto-trading-sim/commit/de231eadfe55c0aca34b59b4bb0c28c75ec205d7) |
| 📏 Long Methods         | Extracted smaller helper methods to follow SRP                | [Commit #58b097d](https://github.com/bobelchev/crypto-trading-sim/commit/58b097df18f132dfc875aedf93157eb3b43b46fc) |
| 🚨 Divergent Change     | Introduced a centralized `TransactionValidator` class          | [Commit #24b26d8](https://github.com/bobelchev/crypto-trading-sim/commit/24b26d83e23b689edab956e2c5f136dcc34c68d2) |
| ❗ Poor Error Handling   | Defined domain-specific **custom exceptions** (e.g., `InvalidTransactionException`) | [Commit #6936db8](https://github.com/bobelchev/crypto-trading-sim/commit/6936db8e57ecfbc40294b9a5388f24ecc2b3ae9a) |

>   **Note:** For more information about common code smells and best practices for refactoring, visit [Refactoring Guru](https://refactoring.guru/refactoring/smells).

## 🏗️ Architecture Evolution

### 🔹 First Iteration: Monolithic Architecture

In the initial version of the project, all responsibilities were handled by a **single Spring Boot application**:

- It connected directly to Kraken’s WebSocket API.
- It parsed and processed live market data.
- It also maintained WebSocket sessions with all frontend users.

📸 **Diagram: Monolithic Architecture**

![Monolithic Architecture](img/monolith.png)

> As more backend instances were added (e.g., to handle more users), **each instance would open a new subscription to Kraken**, leading to:
> - Redundant traffic to the Kraken API
> - Inefficient use of system and network resources
> - Increased risk of hitting API limits or rate caps

---

### 🔸 Second Iteration: Decoupled Kafka-Based Architecture

To solve this scalability issue and improve overall system design, the project was refactored into an **event-driven architecture** using **Apache Kafka**.

#### 🔁 Key Changes:

- ✅ Introduced a dedicated `kraken-ws-service` that connects to Kraken **once** and acts as a **Kafka producer**
- ✅ Backend (`crypto-trading-sim`) becomes a **Kafka consumer**, only responsible for frontend communication
- ✅ Kafka serves as the **single source of truth**, decoupling live data ingestion from distribution

📸 **Diagram: Kafka-Based Microservices Architecture**

![Kafka Microservices Architecture](img/microservice1.png)

### 🔹 Third Iteration: API Gateway

Introduced a simple API Gateway using Spring WebFlux. At this point to provide simple routing and CORS policy management to the monolith service.
 
Now that the gateway is in place:

- 🧩 The backend can be gradually decomposed into services like `user-service`, `transaction-service`, `marketdata-service`, etc.
- 🔄 Gateway will be updated to route requests using service names instead of static ports
- 🛡 Authentication and rate limiting can be handled globally
- 📊 Observability (e.g., using Sleuth or OpenTelemetry) can be added to trace requests across the system
![API Gateway](img/gateway.png)


### 🔹 Fourth Iteration: Decoupling the Frontend WebSocket Push Server from the Monolith

To further isolate concerns and improve scalability, a new service called `market-data-streamer` was introduced. This service is responsible solely for **streaming real-time market data to frontend clients over WebSocket**.

#### 🔁 Key Changes:

- ✅ The monolith no longer manages WebSocket sessions for market data.
- ✅ `market-data-streamer` acts as a **Kafka consumer**, receiving market data from the `kraken-ws-service`.
- ✅ It maintains WebSocket sessions with connected frontend clients and pushes updates in real time.
- ✅ This decoupling allows both the market data ingestion and WebSocket streaming layers to scale independently.

#### 🌟 Benefits:

- 📦 **Better separation of concerns** – data ingestion and client communication are now handled by dedicated services.
- 📈 **Scalability** – the streamer service can be horizontally scaled based on user demand without impacting Kraken API usage.
- 🧪 **Easier testing and deployment** – changes to WebSocket logic don’t affect the main trading simulator backend.
- 🧩 **Modularity** – sets the foundation for additional streaming channels or protocol support in the future (e.g., SSE, gRPC streams).

📸 **Diagram: Market Data Streamer Architecture**

![Market Data Streamer](img/marketDataStreamer.png)

### 🔹 Fifth Iteration: Service Discovery with Eureka Server

To reduce configuration complexity and enable dynamic service lookup, the system introduced **service discovery** using **Spring Cloud Netflix Eureka**.

#### 🔁 Key Changes:

- ✅ Added a **dedicated Eureka Server** to act as a **central registry** for all services
- ✅ `API Gateway`, `market-data-streamer`, and `crypto-trading-sim` now **register themselves** with Eureka upon startup
- ✅ The API Gateway **routes requests** using logical service names (`lb://service-name`) instead of hardcoded IPs or ports
- ✅ Services can now **scale horizontally** and remain discoverable without modifying configuration files

📸 **Diagram: Service Discovery Integration with Eureka**

![Service Discovery with Eureka](img/eureka.png)
### References

- **Java-WebSocket Library**  
  Used the WS client example to enable WebSocket communication with Kraken's WebSocket V2 API.
   - **Original Repository**: [Java-WebSocket](https://github.com/TooTallNate/Java-WebSocket)
   - **Author**: [TooTallNate](https://github.com/TooTallNate)
   - **License**: [MIT License](https://github.com/TooTallNate/Java-WebSocket/blob/master/LICENSE)

- **WebSocket Server Management (Spring without STOMP)**  
  Referenced for implementing raw Spring WebSocket message broadcasting without STOMP.
    - **Source**: [Stack Overflow Thread](https://stackoverflow.com/questions/33910639/how-to-broadcast-a-message-using-raw-spring-4-websockets-without-stomp)
    - **Answer by**: [novax](https://stackoverflow.com/users/943686/novax)

- **WebSocket + React Tutorial**  
  Used code from the blog to implement the React WS client.
   - **Title**: [How to Use WebSockets with React](https://ably.com/blog/websockets-react-tutorial)
   - **Author**: [Alex Booker](https://github.com/bookercodes)
   - **Published by**: [Ably](https://ably.com)

- **Kafka Docker Setup Guide**  
  Used as a reference for setting up Apache Kafka and Zookeeper using Docker and Docker Compose.
    - **Article**: [Kafka With Docker](https://www.baeldung.com/ops/kafka-docker-setup)
    - **Published by**: [Baeldung](https://www.baeldung.com)

- **Spring Cloud Gateway with Spring WebFlux**  
  Used to understand routing and WebFlux configuration in Spring Cloud Gateway.
    - **Article**: [Spring Cloud Gateway with Spring WebFlux](https://www.geeksforgeeks.org/spring-cloud-gateway-with-spring-webflux/)
    - **Published by**: [GeeksforGeeks](https://www.geeksforgeeks.org)

- **Spring Cloud Service Registration and Discovery Guide**  
  Used to understand how services register and discover each other using Spring Cloud Eureka.
    - **Guide**: [Service Registration and Discovery](https://spring.io/guides/gs/service-registration-and-discovery)
    - **Published by**: [Spring.io](https://spring.io)