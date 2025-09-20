import { getPatientAppointments, getPatientData, filterAppointments } from "./services/patientServices.js";

const tableBody = document.getElementById("patientTableBody");
const token = localStorage.getItem("token");

let allAppointments = [];
let patientId = null; 

document.addEventListener("DOMContentLoaded", initializePage);

async function initializePage() {
  try {
    if (!token) throw new Error("No token found");

    console.log("Initializing page with token:", token.substring(0, 20) + "...");

    const patient = await getPatientData(token);
    if (!patient) throw new Error("Failed to fetch patient details");

    console.log("Patient data:", patient);
    patientId = Number(patient.id);

    const appointmentData = await getPatientAppointments(patientId, token) || [];
    console.log("Raw appointment data:", appointmentData);
    
    allAppointments = Array.isArray(appointmentData) ? appointmentData : appointmentData.appointments || [];
    console.log("Processed appointments:", allAppointments);

    renderAppointments(allAppointments);
  } catch (error) {
    console.error("Error loading appointments:", error);
    alert("Failed to load your appointments.");
  }
}

function renderAppointments(appointments) {
  console.log("Rendering appointments:", appointments);
  
  tableBody.innerHTML = "";

  if (!appointments || !appointments.length) {
    tableBody.innerHTML = `<tr><td colspan="5" style="text-align:center;">No Appointments Found</td></tr>`;
    return;
  }

  appointments.forEach(appointment => {
    console.log("Rendering appointment:", appointment);
    
    // Parsowanie daty i czasu
    const appointmentDate = appointment.appointmentDate || "Unknown Date";
    const appointmentTime = appointment.appointmentTimeOnly || appointment.appointmentTime || "Unknown Time";
    
    // Określenie czy wizyta jest w przyszłości czy przeszłości
    const appointmentDateTime = new Date(appointment.appointmentTime || appointment.appointmentDate + " " + appointmentTime);
    const now = new Date();
    const isPast = appointmentDateTime < now;
    
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${appointment.patientName || "You"}</td>
      <td>${appointment.doctorName || "Unknown Doctor"}</td>
      <td>${appointmentDate}</td>
      <td>${appointmentTime}</td>
      <td>
        ${!isPast && appointment.status == 0 ? 
          `<img src="../assets/images/edit/edit.png" alt="Edit" class="prescription-btn" data-id="${appointment.id}" style="cursor: pointer; width: 20px; height: 20px;">` : 
          (isPast ? "Completed" : "Scheduled")
        }
      </td>
    `;

    // Dodaj event listener tylko dla edytowalnych wizyt
    if (!isPast && appointment.status == 0) {
      const actionBtn = tr.querySelector(".prescription-btn");
      actionBtn?.addEventListener("click", () => redirectToUpdatePage(appointment));
    }

    tableBody.appendChild(tr);
  });
}

function redirectToUpdatePage(appointment) {
  const queryString = new URLSearchParams({
    appointmentId: appointment.id,
    patientId: appointment.patientId || patientId,
    patientName: appointment.patientName || "You",
    doctorName: appointment.doctorName,
    doctorId: appointment.doctorId,
    appointmentDate: appointment.appointmentDate,
    appointmentTime: appointment.appointmentTimeOnly || appointment.appointmentTime,
  }).toString();

  setTimeout(() => {
    window.location.href = `/pages/updateAppointment.html?${queryString}`;
  }, 100);
}

// Event listeners dla filtrów
const searchBar = document.getElementById("searchBar");
const appointmentFilter = document.getElementById("appointmentFilter");

if (searchBar) {
  searchBar.addEventListener("input", handleFilterChange);
}
if (appointmentFilter) {
  appointmentFilter.addEventListener("change", handleFilterChange);
}

async function handleFilterChange() {
  const searchBarValue = searchBar?.value.trim() || "";
  const filterValue = appointmentFilter?.value || "allAppointments";

  console.log("Filter change - search:", searchBarValue, "filter:", filterValue);

  // Jeśli nie ma tekstu wyszukiwania i wybrano "all appointments", pokaż oryginalne dane
  if (!searchBarValue && filterValue === "allAppointments") {
    renderAppointments(allAppointments);
    return;
  }

  const name = searchBarValue || "all";
  const condition = filterValue;

  console.log("Filtering with condition:", condition, "name:", name);

  try {
    const response = await filterAppointments(condition, name, token);
    const appointments = response?.appointments || [];
    
    console.log("Filtered appointments:", appointments);
    
    renderAppointments(appointments);
  } catch (error) {
    console.error("Failed to filter appointments:", error);
    alert("An error occurred while filtering appointments.");
  }
}

window.initializePage = initializePage;