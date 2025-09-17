package com.project.back_end.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.back_end.DTO.Login;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;



@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    private final AppointmentRepository appointmentRepository;

    private final TokenService tokenService;

    public DoctorService(DoctorRepository doctorRepository, AppointmentRepository appointmentRepository, TokenService tokenService)
    {
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    @Transactional(readOnly = true)
    public List<String> getDoctorAvailability(Long doctorId, LocalDate date)
    {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);
        
        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(doctorId, start, end);

        Set<Integer> bookedHours = appointments.stream().map(a -> a.getAppointmentTime().getHour()).collect(Collectors.toSet());

        List<String> availableSlots = new ArrayList<>();
        for(int hour = 9; hour<17; hour++)
        {
            if(!bookedHours.contains(hour)){
                availableSlots.add(String.format("%02d:00", hour));
            }
        }
        return availableSlots;
    }

    @Transactional
    public int saveDoctor(Doctor doctor){
        if(doctorRepository.findByEmail(doctor.getEmail()) != null)
        {
            System.out.println("Doctor already exists");
            return -1;
        }
        try {
            doctorRepository.save(doctor);
            System.out.println("Doctor saved successfully!");
            return 1;
        } catch (Exception e) {
            System.out.println("Error has occured during saving doctor: " +e.getMessage());
            return 0;
        }
    }

    @Transactional
    public int updateDoctor(Doctor doctor)
    {
        if(!doctorRepository.existsById(doctor.getId()))
        {
            System.out.println("Doctor not found");
            return -1;
        }
        try {
            doctorRepository.save(doctor);
            System.out.println("Doctor updated successfully");
            return 1;
        } catch (Exception e) {
            System.out.println("Error has occured during updating doctor: " +e.getMessage());
            return 0;
        }
    }

    @Transactional(readOnly = true)
    public List<Doctor> getDoctors()
    {
        List<Doctor> doctors = doctorRepository.findAll();
        doctors.forEach(doc -> doc.getAvailableTimes().size());
        return doctorRepository.findAll();
    }

    @Transactional
    public int deleteDoctor(Long id)
    {
        if(!doctorRepository.existsById(id))
        {
            System.out.println("Doctor not found");
            return -1;
        }
        try {
            appointmentRepository.deleteAllByDoctorId(id);
            doctorRepository.deleteById(id);
            System.out.println("Doctor and appointments deleted successfully!");
            return 1;
        } catch (Exception e) {
            System.out.println("Error has occured during deleting doctor: " +e.getMessage());
            return 0;
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Map<String, String>> validateDoctor(Login login)
    {
        Doctor doctor = doctorRepository.findByEmail(login.getEmail());
        if(doctor == null || !doctor.getPassword().equals(login.getPassword()))
        {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid credentials"));
        }

        String token = tokenService.generateToken(doctor.getEmail());
        return ResponseEntity.ok(Map.of("token", token));
    }

    @Transactional(readOnly = true)
    public Map<String, Object> findDoctorByName(String name){
        List<Doctor> doctors = doctorRepository.findByNameLike(name);
        return Map.of("doctors", doctors);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> filterDoctorsByNameSpecialityandTime(String name, String speciality, String amOrPm)
    {
        List<Doctor> doctors = doctorRepository.findByNameContainingIgnoreCaseAndSpecialityIgnoreCase(name, speciality);
        doctors = filterDoctorByTime(doctors, amOrPm);
        return Map.of("doctors", doctors);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> filterDoctorByNameAndTime(String name, String amOrPm)
    {
        List<Doctor> doctors = doctorRepository.findByNameLike(name);
        doctors = filterDoctorByTime(doctors, amOrPm);
        return Map.of("doctors", doctors);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> filterDoctorByNameAndSpeciality(String name, String speciality)
    {
        List<Doctor> doctors = doctorRepository.findByNameContainingIgnoreCaseAndSpecialityIgnoreCase(name, speciality);
        return Map.of("doctors", doctors);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> filterDoctorByTimeAndSpeciality(String speciality, String amOrPm)
    {
        List<Doctor> doctors = doctorRepository.findBySpecialityIgnoreCase(speciality);
        doctors = filterDoctorByTime(doctors, amOrPm);
        return Map.of("doctors", doctors);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> filterDoctorBySpeciality(String speciality)
    {
        List<Doctor> doctors = doctorRepository.findBySpecialityIgnoreCase(speciality);
        return Map.of("doctors", doctors);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> filterDoctorsByTime(String amOrPm)
    {
        List<Doctor> doctors = doctorRepository.findAll();
        doctors = filterDoctorByTime(doctors, amOrPm);
        return Map.of("doctors", doctors);
    }

    @Transactional(readOnly = true)
    public List<Doctor> filterDoctorByTime(List<Doctor> doctors, String amOrPm)
    {
        return doctors.stream().filter(d -> d.getAvailableTimes().stream().anyMatch(time -> {
            int hour = Integer.parseInt(time.split(":")[0]);
            if("AM".equals(amOrPm)) return hour < 12;
            else return hour>=12;
        })).collect(Collectors.toList());
    }
    


// 1. **Add @Service Annotation**:
//    - This class should be annotated with `@Service` to indicate that it is a service layer class.
//    - The `@Service` annotation marks this class as a Spring-managed bean for business logic.
//    - Instruction: Add `@Service` above the class declaration.

// 2. **Constructor Injection for Dependencies**:
//    - The `DoctorService` class depends on `DoctorRepository`, `AppointmentRepository`, and `TokenService`.
//    - These dependencies should be injected via the constructor for proper dependency management.
//    - Instruction: Ensure constructor injection is used for injecting dependencies into the service.

// 3. **Add @Transactional Annotation for Methods that Modify or Fetch Database Data**:
//    - Methods like `getDoctorAvailability`, `getDoctors`, `findDoctorByName`, `filterDoctorsBy*` should be annotated with `@Transactional`.
//    - The `@Transactional` annotation ensures that database operations are consistent and wrapped in a single transaction.
//    - Instruction: Add the `@Transactional` annotation above the methods that perform database operations or queries.

// 4. **getDoctorAvailability Method**:
//    - Retrieves the available time slots for a specific doctor on a particular date and filters out already booked slots.
//    - The method fetches all appointments for the doctor on the given date and calculates the availability by comparing against booked slots.
//    - Instruction: Ensure that the time slots are properly formatted and the available slots are correctly filtered.

// 5. **saveDoctor Method**:
//    - Used to save a new doctor record in the database after checking if a doctor with the same email already exists.
//    - If a doctor with the same email is found, it returns `-1` to indicate conflict; `1` for success, and `0` for internal errors.
//    - Instruction: Ensure that the method correctly handles conflicts and exceptions when saving a doctor.

// 6. **updateDoctor Method**:
//    - Updates an existing doctor's details in the database. If the doctor doesn't exist, it returns `-1`.
//    - Instruction: Make sure that the doctor exists before attempting to save the updated record and handle any errors properly.

// 7. **getDoctors Method**:
//    - Fetches all doctors from the database. It is marked with `@Transactional` to ensure that the collection is properly loaded.
//    - Instruction: Ensure that the collection is eagerly loaded, especially if dealing with lazy-loaded relationships (e.g., available times). 

// 8. **deleteDoctor Method**:
//    - Deletes a doctor from the system along with all appointments associated with that doctor.
//    - It first checks if the doctor exists. If not, it returns `-1`; otherwise, it deletes the doctor and their appointments.
//    - Instruction: Ensure the doctor and their appointments are deleted properly, with error handling for internal issues.

// 9. **validateDoctor Method**:
//    - Validates a doctor's login by checking if the email and password match an existing doctor record.
//    - It generates a token for the doctor if the login is successful, otherwise returns an error message.
//    - Instruction: Make sure to handle invalid login attempts and password mismatches properly with error responses.

// 10. **findDoctorByName Method**:
//    - Finds doctors based on partial name matching and returns the list of doctors with their available times.
//    - This method is annotated with `@Transactional` to ensure that the database query and data retrieval are properly managed within a transaction.
//    - Instruction: Ensure that available times are eagerly loaded for the doctors.


// 11. **filterDoctorsByNameSpecilityandTime Method**:
//    - Filters doctors based on their name, specialty, and availability during a specific time (AM/PM).
//    - The method fetches doctors matching the name and specialty criteria, then filters them based on their availability during the specified time period.
//    - Instruction: Ensure proper filtering based on both the name and specialty as well as the specified time period.

// 12. **filterDoctorByTime Method**:
//    - Filters a list of doctors based on whether their available times match the specified time period (AM/PM).
//    - This method processes a list of doctors and their available times to return those that fit the time criteria.
//    - Instruction: Ensure that the time filtering logic correctly handles both AM and PM time slots and edge cases.


// 13. **filterDoctorByNameAndTime Method**:
//    - Filters doctors based on their name and the specified time period (AM/PM).
//    - Fetches doctors based on partial name matching and filters the results to include only those available during the specified time period.
//    - Instruction: Ensure that the method correctly filters doctors based on the given name and time of day (AM/PM).

// 14. **filterDoctorByNameAndSpecility Method**:
//    - Filters doctors by name and specialty.
//    - It ensures that the resulting list of doctors matches both the name (case-insensitive) and the specified specialty.
//    - Instruction: Ensure that both name and specialty are considered when filtering doctors.


// 15. **filterDoctorByTimeAndSpecility Method**:
//    - Filters doctors based on their specialty and availability during a specific time period (AM/PM).
//    - Fetches doctors based on the specified specialty and filters them based on their available time slots for AM/PM.
//    - Instruction: Ensure the time filtering is accurately applied based on the given specialty and time period (AM/PM).

// 16. **filterDoctorBySpecility Method**:
//    - Filters doctors based on their specialty.
//    - This method fetches all doctors matching the specified specialty and returns them.
//    - Instruction: Make sure the filtering logic works for case-insensitive specialty matching.

// 17. **filterDoctorsByTime Method**:
//    - Filters all doctors based on their availability during a specific time period (AM/PM).
//    - The method checks all doctors' available times and returns those available during the specified time period.
//    - Instruction: Ensure proper filtering logic to handle AM/PM time periods.

   
}
