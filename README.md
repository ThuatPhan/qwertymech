# QwertyMech

**QwertyMech** is a scalable, microservices-based e-commerce backend designed specifically for a mechanical keyboard store.

## ✨ Key Features

  * **Secure Authentication:** Keycloak-based identity management; clients log in with the Authorization Code + PKCE flow and all services act as OAuth2 resource servers (JWT RS256 via JWKS).
  * **Product Catalog:** Efficient management of mechanical keyboards, switches, and accessories.
  * **Shopping Cart:** Real-time cart management for a seamless shopping experience.
  * **Order Processing:** Reliable order lifecycle management from placement to fulfillment.
  * **Integrated Payments:** Secure payment processing integration with **VNPAY**.
  * **Smart Notifications:** Asynchronous email delivery system powered by **Resend**.

## 🛠 Tech Stack

  * **Core:** Java 21, Spring Boot 3
  * **Database & Caching:** PostgreSQL, Redis
  * **Messaging:** Apache Kafka
  * **Infrastructure:** Docker, Docker Compose

## 🚀 Getting Started

### Prerequisites

  * Docker & Docker Compose

### 1\. Clone the Repository

```bash
git clone https://github.com/ThuatPhan/qwertymech.git
cd QwertyMech
```

### 2\. Environment Configuration

You must create **three `.env` files** in the corresponding service directories using the formats below:

#### 🔐 User Service (`user-service/.env`)

```env
# Keycloak (defaults work with docker-compose)
KEYCLOAK_URL=http://keycloak:8180
JWKS_URI=http://keycloak:8180/realms/qwertymech/protocol/openid-connect/certs
KEYCLOAK_ADMIN_USERNAME=admin
KEYCLOAK_ADMIN_PASSWORD=admin
```

#### 💳 Payment Service (`payment-service/.env`)

```env
# VNPAY Configuration
TMN_CODE=your_payment_tmn_code
SECRET_KEY=your_payment_secret_key
```

#### 📧 Notification Service (`notification-service/.env`)

```env
# Resend API
RESEND_API_KEY=your_resend_api_key
```

### 3\. Run the Application

Start the entire microservices stack:

```bash
docker-compose up -d
```

Keycloak runs at `http://localhost:8180` (admin console: `admin` / `admin`). The `qwertymech` realm is imported automatically with `ADMIN`/`USER` roles, the `qwertymech-client` client, and a seeded admin account (`admin@qwertymech.com` / `Admin@123`).

**Kong Gateway** (DB-less, declarative config in `kong.yml`) is the single entry point at `http://localhost:8000` — it terminates routing, CORS, rate limiting and JWT verification (Keycloak RS256 public key). Services are not exposed to the host; only Kong, Keycloak and the databases are.

### 4\. Authentication Flow (Authorization Code + PKCE)

Clients authenticate **directly with Keycloak** — the backend services are pure OAuth2 resource servers and never handle credentials or issue tokens.

```
1. Client generates a random code_verifier and its S256 code_challenge
2. Browser redirects to:
   GET http://localhost:8180/realms/qwertymech/protocol/openid-connect/auth
       ?client_id=qwertymech-client
       &response_type=code
       &scope=openid
       &redirect_uri=http://localhost:3000/callback
       &code_challenge=<challenge>
       &code_challenge_method=S256
3. User logs in on the Keycloak page → redirect back with ?code=...
4. Client exchanges the code (backend-less):
   POST http://localhost:8180/realms/qwertymech/protocol/openid-connect/token
       grant_type=authorization_code & code=... & redirect_uri=...
       & client_id=qwertymech-client & code_verifier=<verifier>
   → access_token + refresh_token
5. Client calls the API with: Authorization: Bearer <access_token>
```

Kong verifies the JWT at the edge (jwt plugin, RS256 public key from the realm) and every service validates it again against the Keycloak JWKS endpoint (`JWKS_URI`) — defense in depth. Roles come from the `realm_access.roles` claim and drive `@PreAuthorize` checks. Direct access grants (password grant) are disabled on `qwertymech-client` — PKCE is enforced.

Note: the Kong jwt credential embeds the realm's current public key. If the realm is re-imported (new key pair), re-fetch the key from `GET http://localhost:8180/realms/qwertymech` and update `kong.yml`.
