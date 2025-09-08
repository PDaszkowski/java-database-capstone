1. User accesses AdminDashboard or Appointment pages.
2. The action is routed to the appropriate Thymeleaf or REST controller.
3. The controller calls the service layer
4. Service layer uses both MongoDB and MySQL repositories
5. MySQL repository accesses MySQL database
6. MySQL retrieves data as Entities, which are mapped as MySQL Models
7. MySQL Models contains entities such as: Patient, Doctor, Appointment and Admin, which represents date in application
