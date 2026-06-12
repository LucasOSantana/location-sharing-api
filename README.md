# Location Sharing API

A scalable, real-time location sharing service built with Spring Boot, RabbitMQ, and WebSockets. This project mimics the "live tracking" feature found in ride-sharing apps like Uber.

## 🚀 Architecture & Flow

The project follows an event-driven architecture to ensure high availability and responsiveness:

1.  **Ingestion (REST)**: A provider (e.g., a driver) sends coordinates to `/collect-location`.
2.  **Buffering (RabbitMQ)**: The request is immediately queued in RabbitMQ, and the producer receives a `200 OK`. This prevents processing bottlenecks from affecting the mobile client.
3.  **Processing (Worker)**: A background listener consumes messages from the queue.
4.  **Broadcasting (WebSockets)**: The worker pushes the location update to a specific WebSocket topic based on the entity's ID.
5.  **Consumption (Frontend)**: Subscribers (e.g., passengers) receive real-time updates on their maps without polling the server.

## 🛠️ Technology Stack

*   **Java 17**
*   **Spring Boot 4.x**
*   **RabbitMQ** (Messaging Broker)
*   **Spring WebSocket (STOMP)** (Real-time broadcasting)
*   **Lombok & Jakarta Validation**

## 📖 How to Use

### 1. Prerequisites
*   Java 17+
*   RabbitMQ instance running (Expected at `rabbitmq:5672` per `application.properties`)

### 2. Running the Project
```bash
./mvnw spring-boot:run
```

### 3. Sending Location Data (Producer)
Send a `POST` request to `http://localhost:8080/collect-location`:

**Body:**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "latitude": "-23.5505",
  "longitude": "-46.6333",
  "timestamp": "2026-06-12T10:00:00Z"
}
```

### 4. Receiving Live Updates (Consumer)
Connect to the WebSocket endpoint and subscribe to the specific ID topic.

*   **Endpoint:** `ws://localhost:8080/ws-location`
*   **Topic to Subscribe:** `/topic/location/{id}`

#### Frontend Integration Example (stompjs):
```javascript
const socket = new SockJS('http://localhost:8080/ws-location');
const stompClient = Stomp.over(socket);

stompClient.connect({}, () => {
    stompClient.subscribe('/topic/location/550e8400-e29b-41d4-a716-446655440000', (message) => {
        const data = JSON.parse(message.body);
        console.log("New Position:", data.latitude, data.longitude);
    });
});
```

## ⚠️ Security Warning: CORS

Currently, the project is configured with permissive CORS for development:
*   `setAllowedOriginPatterns("*")` in `WebsocketConfig.java`.

**IMPORTANT:** Before deploying to production, you **MUST** update the CORS configuration to allow only your specific domain (e.g., `https://yourapp.com`) to prevent unauthorized cross-origin WebSocket connections.

## 🧪 Testing
Run unit tests to verify the service logic:
```bash
./mvnw test
```
