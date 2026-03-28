# Simple Clinic Management System

This project was a part of IBM Java Developer course on Coursera.

In short words -> Web app to manage clinic - it enables to manage patients, doctors and prescriptions.

---

## Tech Stack
- **Java 17** + **Spring Boot**
- **MySQL** — database (doctors, patients, visits, admin)
- **MongoDB** — prescriptions
- **Hibernate / Spring Data JPA**
- **JWT** — authentication
- **Docker / Docker Compose**
- **Maven**

---

requirements:
- Docker Desktop

---

```bash
# Clone repository
git clone https://github.com/PDaszkowski/java-database-capstone.git
cd java-database-capstone/app
 
# Launch conteners
docker compose up --build
```
Application available under: [http://localhost:8080](http://localhost:8080)

### Stop and data reset
 
```bash
docker compose down -v
```

---

### A few screenshots of the application:

Doctors:
<img width="2547" height="1349" alt="image" src="https://github.com/user-attachments/assets/b15189a0-78d5-4ce6-a882-9ac0e330f5fc" />

Booking appointment:
<img width="1357" height="1126" alt="image" src="https://github.com/user-attachments/assets/da1c68c5-7198-4af1-b3b8-ae1266369b4d" />

Patient view:
<img width="2556" height="1348" alt="image" src="https://github.com/user-attachments/assets/1f06eeb9-dfd2-4e77-b86b-9ab89068269a" />

Admin view:
<img width="2548" height="1306" alt="image" src="https://github.com/user-attachments/assets/544a1aee-2441-4a6a-ae43-271326c9637b" />

Doctor view:
<img width="2558" height="1308" alt="image" src="https://github.com/user-attachments/assets/bade706a-7300-4ee0-ab2d-0e850a8efe2a" />

Adding prescription:
<img width="673" height="695" alt="image" src="https://github.com/user-attachments/assets/8c0f59ca-1318-47fa-adaf-b99b4363326a" />

## License
 
[Apache 2.0](LICENSE)
