# QwertyMech

A microservices-based e-commerce backend for a mechanical keyboard store.

## Tech Stack

* **Backend:** Spring Boot (Java)
* **Database:** PostgreSQL
* **Caching:** Redis
* **Message Broker:** Kafka
* **Email:** Resend SDK
* **Containerization:** Docker

### Microservice Architecture

The application is decomposed into the following core microservices:

* **API Gateway**: The single entry point for all client requests.
* **Identity Service**: Handles user authentication (JWT, OAuth2) and authorization.
* **Product Service**: Manages the product catalog.
* **Cart Service**: Manages user shopping carts.
* **Order Service**: Manages customer orders.
* **Payment Service**: Processes payments.
* **Notification Service**: Sends asynchronous notifications (e.g., email).

## Getting Started

### Prerequisites

* Docker
* Docker Compose

### 1. Clone the Repository

```bash
git clone https://github.com/ThuatPhan/qwertymech.git
cd QwertyMech
```

### 2. Environment Configuration

You must create `.env` files for the following services. Copy the contents below and place them in the correct service
directory, replacing placeholders with your actual secrets.

#### `identity-service/.env`

```env
# Jwt
JWT_SECRET=your_super_strong_jwt_secret_key_32_bytes
JWT_ISSUER=your_domain
JWT_EXPIRY_SECONDS=3600

# Google OAuth2
IDENTITY_CLIENT_ID=your_google_client_id.apps.googleusercontent.com
IDENTITY_CLIENT_SECRET=your_google_client_secret
IDENTITY_REDIRECT_URI=your_redirect_url
```

#### `payment-service/.env`

```env
# Payment Gateway (e.g., VNPAY)
TMN_CODE=your_payment_tmn_code
SECRET_KEY=your_payment_secret_key
```

#### `notification-service/.env`

```env
# Resend API
RESEND_API_KEY=your_resend_api_key
```

### 3. Run the Application

With Docker running, start the entire microservice stack:

```bash
docker-compose up -d
```

The API Gateway will be available at `http://localhost:8080` (or your configured port).