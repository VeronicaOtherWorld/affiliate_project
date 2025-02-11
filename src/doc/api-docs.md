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