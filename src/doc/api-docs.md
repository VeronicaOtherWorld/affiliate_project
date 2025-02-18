# 📌 Affiliate Project API documentation

## 0️⃣ Introduction
## 1️⃣ Banners API
### ✅ get all active banners
- **URL:** `GET /api/banners`
- **description:** get all active `banners`
- **example request:**
```bash
curl -X GET http://localhost:8080/api/banners
```
- **example response:**
```json
[
  {
    "id": 1,
    "title": "Homepage Banner",
    "imageUrl": "https://example.com/banner1.jpg",
    "description": "Main homepage banner",
    "status": true
  }
]
```
### ✅ get a specific banner


## 1️⃣ person information API
### ✅ Register a New User
- **URL:** `POST /api/person/register`
- **description:** Registers a new user with their email, password, and additional profile information.
- **example request:**
```bash
curl -X POST http://localhost:8080/api/person/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "pwd": "123456",
    "status": 1
  }'
```


### ✅ User Login
- **URL:** `POST /api/person/login`
- **description:** Logs a user in and returns a JWT token.
- **example request:**
```bash
curl -X POST http://localhost:8080/api/person/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "pwd": "123456"
  }'
```