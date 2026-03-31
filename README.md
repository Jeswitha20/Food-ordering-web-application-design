# Food Ordering (Swiggy/Zomato Clone) - Clean Architecture Scaffold

## Stack
- Backend: Spring Boot (Java, Maven), MySQL, Spring Security + JWT (access token only), JavaMailSender (HTML template)
- Frontend: React (Vite)

## Backend Setup
1. Open `backend/src/main/resources/application.properties`
2. Configure these environment variables (or edit defaults):
   - `JWT_SECRET` (must be long/random)
   - `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`
   - `MAIL_HOST`, `MAIL_PORT`, `MAIL_USERNAME`, `MAIL_PASSWORD`, `MAIL_FROM` (optional)
3. Run the backend:
   - `mvn -f backend/pom.xml spring-boot:run`

The backend exposes:
- `POST /api/v1/auth/register`
- `POST /api/v1/auth/login`
- `GET /api/v1/restaurants`
- `GET /api/v1/restaurants/{restaurantId}`
- `GET /api/v1/cart`, `POST /api/v1/cart/items`, `PATCH /api/v1/cart/items/{menuItemId}`, `DELETE /api/v1/cart/items/{menuItemId}`
- `POST /api/v1/orders`, `GET /api/v1/orders/me`

## Frontend Setup
1. Install dependencies:
   - `cd frontend && npm install`
2. Create `.env` from `.env.example` (optional):
   - `cp .env.example .env`
3. Run:
   - `npm run dev`

Frontend default runs on `http://localhost:5173` and proxies `/api` to `http://localhost:8080`.

