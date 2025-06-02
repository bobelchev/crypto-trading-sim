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
3. Open new terminal, navigate to the backend folder and start the Spring Boot server using PS on Windows:

   ```bash
   cd backend/crypto-trading-sim
   .\mvnw spring-boot:run
   ```
   Or if using CMD:
   ```bash
    mvnw spring-boot:run
   ```

4. Open a new terminal, navigate to the frontend folder, and start the React app:

   ```bash
   cd frontend
   npm install    # Only needed the first time
   npm start      # Or: npm run dev
   ```

The backend will run on http://localhost:8080

The kraken service will run on http://localhost:8082

The frontend will run on http://localhost:5173 or fallback to http://localhost:5174
### Demo

Watch the demo video on [Google Drive](https://drive.google.com/file/d/1k9GvioiWBQe2OWV7eZ--5DymZnJuiDkN/view?usp=drive_link)

### Screenshots

#### Initial Application Screen (Top 20 Crypto Prices)
![Top 20 Crypto Prices](img/initialScreen.png)
----
#### Buy Interface
![Buy Interface](img/buyInterface.png)
----
#### Sell Interface
![Sell Interface](img/sellInterface.png)
----
#### Updated Balance After Transaction

![Interface before the transaction](img/initialScreen.png)
----
![Make a sell](img/buyInterface.png)
----
![Interface after the transaction](img/interfaceAfterBuy.png)
----
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

3. **Pull Requests**

| Feature                | Branch                     | PR Link                                                      |
|------------------------|----------------------------|--------------------------------------------------------------|
| Kraken WebSocket Integration | `feature/kraken-ws-client` | [#5](https://github.com/bobelchev/crypto-trading-sim/pull/5) |
| Frontend UI            | `frontend`                 | [#6](https://github.com/bobelchev/crypto-trading-sim/pull/6) |
| Extract Kraken WebSocket to Kafka Producer Service                       | `service/kraken-ws`         | [#7](https://github.com/bobelchev/crypto-trading-sim/pull/7) |


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
