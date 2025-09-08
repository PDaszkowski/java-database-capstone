## MySQL Database Design
### Table: appointments
- id: INT, Primary Key, Auto Increment
- doctor_id: INT, Foreign Key -> doctors(id)
- patient_id: INT, Foreign Key -> patients(id)
- appointment_time: DATETIME, Not Null
- status: INT (0 = Scheduled, 1 = Completed, 2 = Cancelled)

### Table: patients
- id: INT, Primary Key, Auto Increment
- first_name: VARCHAR(100), Not null
- last_name: VARCHAR(100), Not null
- date_of_birth: DATE, Not Null
- gender: ENUM('M','F'), Not Null
- phone: VARCHAR(20), Unique
- email: VARCHAR(100), Unique
- username: VARCHAR(100), Unique, Not Null
- password_hash: VARCHAR(255), Not Null

### Table: doctors
- id: INT, Primary Key, Auto Increment
- first_name: VARCHAR(100), Not null
- last_name: VARCHAR(100), Not null
- specialization: VARCHAR(100), Not null
- clinic_location_id: INT, Foreign Key -> clinic_locations(id)
- phone: VARCHAR(20), Unique
- email: VARCHAR(100), Unique

### Table: admin
- id: INT, Primary Key, auto Increment
- username: VARCHAR(100), Unique, Not Null
- password_hash: VARCHAR(255), Not Null

### Table: clinic_locations
- id: INT, Primary Key, Auto Increment
- name: VARCHAR(100), Not Null
- address: VARCHAR(150), Not Null
- phone: VARCHAR(20)
- email: VARCHAR(100)

## MongoDB Collection Design

### Collection: prescriptions
```json
{
  "_id": "ObjectId('64abc123456')",
  "patientId": 554,             // -> MySQL patients.id
  "appointmentId": 51,          // -> MySQL appointments.id
  "doctorId": 42,               // -> MySQL doctors.id
  "medications": [
    {
      "name": "Paracetamol",
      "dosage": "500mg",
      "instructions": "Take 1 tablet every 6 hours"
    },
    {
      "name": "Ibuprofen",
      "dosage": "200mg",
      "instructions": "Take after meals, max 3 times a day"
    }
  ],
  "refillCount": 2,
  "pharmacy": "Walgreens SF, Market Street",
}
