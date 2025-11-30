# QwertyMech

**QwertyMech** is a scalable, microservices-based e-commerce backend designed specifically for a mechanical keyboard store.

## ✨ Key Features

  * **Secure Authentication:** Robust user identity management using JWT and Google OAuth2.
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

#### 🔐 Identity Service (`identity-service/.env`)

```env
# JWT Configuration
JWT_SECRET=your_super_strong_jwt_secret_key_32_bytes
JWT_ISSUER=your_domain
JWT_EXPIRY_SECONDS=3600

# Google OAuth2
IDENTITY_CLIENT_ID=your_google_client_id
IDENTITY_CLIENT_SECRET=your_google_client_secret
IDENTITY_REDIRECT_URI=your_redirect_url
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

The API Gateway will be accessible at `http://localhost:8080`.
