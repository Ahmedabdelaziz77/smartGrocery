# Smart Grocery — Backend API

A **Spring Boot** REST API for managing a smart grocery list with admin product approval, external food database integration, and user shopping lists.

> **Frontend Repository:** [smart-grocery-frontend](https://github.com/Ahmedabdelaziz77/smart-grocery-frontend)

---

## Tech Stack

| Technology | Version |
|---|---|
| Java | 21 |
| Spring Boot | 3.5 |
| Spring Security | JWT (Access + Refresh Tokens) |
| Spring Data JPA | Hibernate |
| PostgreSQL | Latest (via Docker) |
| MapStruct | 1.5.5 |
| Lombok | Latest |
| SpringDoc OpenAPI | 2.8.6 |
| Maven | Wrapper included |

---

## Features

### Authentication & Authorization
- JWT-based authentication with access & refresh token rotation
- Role-based access control (`ADMIN` / `USER`)
- First registered user is automatically assigned the `ADMIN` role
- Secure logout with server-side token revocation
- Password encryption with BCrypt

### Admin Dashboard
- Search external food products from [Open Food Facts API](https://world.openfoodfacts.org) by keyword
- Import products (single or bulk) into the application database as approved items
- Remove products via soft-delete (preserves data integrity)
- Dashboard analytics — total products, categories, users, recently approved items, and product distribution by category

### User Dashboard
- Browse paginated approved grocery items
- View detailed product information — calories, protein, carbs, fat, brand, estimated price, category, and image
- Search and filter products by name and/or category
- Personal shopping list — add items, update quantities, remove items, or clear the entire list

### Bonus Features
- Bulk import — admins can import multiple products in a single request
- Pagination on all list endpoints with sorting support

---

## API Endpoints

### Authentication — `/api/auth`

| Method | Endpoint | Access |
|---|---|---|
| `POST` | `/api/auth/signup` | Public |
| `POST` | `/api/auth/login` | Public |
| `POST` | `/api/auth/refresh` | Public |
| `POST` | `/api/auth/logout` | Authenticated |

### Admin Products — `/api/admin/products`

| Method | Endpoint | Access |
|---|---|---|
| `GET` | `/api/admin/products/dashboard` | ADMIN |
| `GET` | `/api/admin/products/search?query=&page=&size=` | ADMIN |
| `GET` | `/api/admin/products/approved?name=&category=&page=&size=` | ADMIN |
| `POST` | `/api/admin/products/import` | ADMIN |
| `POST` | `/api/admin/products/import/bulk` | ADMIN |
| `DELETE` | `/api/admin/products/{id}` | ADMIN |

### Products — `/api/products`

| Method | Endpoint | Access |
|---|---|---|
| `GET` | `/api/products?page=&size=` | USER, ADMIN |
| `GET` | `/api/products/{id}` | USER, ADMIN |
| `GET` | `/api/products/categories` | USER, ADMIN |
| `GET` | `/api/products/search?name=&category=&page=&size=` | USER, ADMIN |

### Shopping List — `/api/shopping-list`

| Method | Endpoint | Access |
|---|---|---|
| `GET` | `/api/shopping-list` | USER |
| `POST` | `/api/shopping-list/items` | USER |
| `PUT` | `/api/shopping-list/items/{id}` | USER |
| `DELETE` | `/api/shopping-list/items/{id}` | USER |
| `DELETE` | `/api/shopping-list/items` | USER |

---

## Getting Started

### Prerequisites

- **Java 21** or higher
- **Maven** (or use the included Maven Wrapper)
- **Docker** & **Docker Compose** (for PostgreSQL)

### 1. Start the database

> **Important:** You must create the database before starting the application.

```bash
docker-compose up -d
```

This will start a PostgreSQL container on port `5433` with:
- **Database:** `smart_grocery`
- **Username:** `postgres`
- **Password:** `postgres`

### 2. Install dependencies and compile

> **Very Important:** Always run a clean compile before starting the application. This ensures MapStruct mappers and Lombok annotations are properly generated.

```bash
# Clean compile (REQUIRED — generates MapStruct mappers)
./mvnw clean compile

# Run tests to verify everything works
./mvnw clean verify
```

### 3. Run the application

```bash
./mvnw spring-boot:run
```

The API will be available at: **http://localhost:8080**

---

## API Documentation

Once the application is running, you can access the interactive API documentation at:

- **Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI Spec:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## Testing

The project includes unit tests for core services:

```bash
./mvnw clean verify
```

| Test Class | Coverage |
|---|---|
| `AuthServiceTest` | Signup, login, token refresh, logout |
| `ProductServiceTest` | Product listing, search, filtering |
| `DashboardServiceTest` | Dashboard analytics aggregation |
| `ShoppingListServiceTest` | CRUD operations on shopping list |

---

## Video Demo
