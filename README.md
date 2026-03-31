# Food Ordering web application

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
<img width="1910" height="885" alt="Screenshot 2026-03-31 172309" src="https://github.com/user-attachments/assets/2840183c-c618-4a89-97d2-f55e1034f0c8" />

<img width="1478" height="966" alt="image" src="https://github.com/user-attachments/assets/e4fcdccf-a964-4531-883c-0a6cf532aaab" />

<img width="1500" height="958" alt="image" src="https://github.com/user-attachments/assets/79d86236-ecde-4b1f-a1be-baa94022852e" />

<img width="1480" height="915" alt="image" src="https://github.com/user-attachments/assets/5337129a-3994-4cc5-a170-584c9c9ec9cd" />

<img width="1485" height="954" alt="image" src="https://github.com/user-attachments/assets/473bee17-d95d-45fd-b024-15b200ad06a8" />

<img width="926" height="532" alt="image" src="https://github.com/user-attachments/assets/82c951dc-de76-40a2-b63c-b6442fc85b09" />














