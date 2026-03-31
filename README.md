# Food Ordering 

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

<img width="1898" height="895" alt="Screenshot 2026-03-31 172257" src="https://github.com/user-attachments/assets/761bbefb-32a1-4b95-a4de-ccd8956d5739" />

<img width="1912" height="896" alt="Screenshot 2026-03-31 172358" src="https://github.com/user-attachments/assets/93ec31da-bd3c-40a6-b731-a3f40bc986ad" />

<img width="1407" height="888" alt="Screenshot 2026-03-31 172423" src="https://github.com/user-attachments/assets/d7896b6e-fd6f-40e9-9d8f-b4a634ef0ca1" />

<img width="1419" height="491" alt="Screenshot 2026-03-31 172444" src="https://github.com/user-attachments/assets/34925e98-a703-4614-b099-5eb96fcbf500" />

<img width="1422" height="891" alt="Screenshot 2026-03-31 172453" src="https://github.com/user-attachments/assets/cd5a277a-482b-4ef2-9916-07ba5d877110" />

<img width="922" height="514" alt="Screenshot 2026-03-31 172532" src="https://github.com/user-attachments/assets/3d85d8f1-1710-41aa-a8c5-ad170c78d1d6" />









