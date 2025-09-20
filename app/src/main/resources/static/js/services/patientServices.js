import { API_BASE_URL } from "../config/config.js";
const PATIENT_API = API_BASE_URL + '/patient';

// Pozostałe funkcje bez zmian...
export async function patientSignup(data) {
  try {
    const response = await fetch(`${PATIENT_API}`, {
      method: "POST",
      headers: {"Content-Type": "application/json"},
      body: JSON.stringify(data)
    });
    const result = await response.json();
    if (!response.ok) {
      throw new Error(result.message);
    }
    return { success: response.ok, message: result.message }
  }
  catch (error) {
    console.error("Error :: patientSignup :: ", error)
    return { success: false, message: error.message }
  }
}

export async function patientLogin(data) {
  console.log("patientLogin :: ", data)
  return await fetch(`${PATIENT_API}/login`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(data)
  });
}

export async function getPatientData(token) {
  try {
    const response = await fetch(`${PATIENT_API}/${token}`);
    const data = await response.json();
    console.log("Patient data response:", data);
    if (response.ok) return data.patient;
    return null;
  } catch (error) {
    console.error("Error fetching patient details:", error);
    return null;
  }
}

export async function getPatientAppointments(id, token) {
  try {
    console.log("Fetching appointments for patient ID:", id, "with token:", token.substring(0, 20) + "...");
    
    const response = await fetch(`${PATIENT_API}/${id}/${token}`);
    const data = await response.json();
    
    console.log("Appointments response:", data);
    
    if (response.ok) {
      return data.appointments || [];
    }
    console.error("Failed to fetch appointments:", data);
    return [];
  }
  catch (error) {
    console.error("Error fetching patient appointments:", error);
    return [];
  }
}

// POPRAWIONA funkcja filterAppointments
export async function filterAppointments(condition, name, token) {
  try {
    // Mapowanie frontend → backend wartości
    let conditionParam;
    let nameParam = name || "all";
    
    // Poprawne mapowanie condition values
    switch(condition) {
      case "allAppointments":
        conditionParam = "all";
        break;
      case "future":
        conditionParam = "upcoming";  // lub "future" - zależy od backend implementacji
        break;
      case "past":
        conditionParam = "past";
        break;
      default:
        conditionParam = "all";
    }
    
    const url = `${PATIENT_API}/appointment/filter/${conditionParam}/${nameParam}/${token}`;
    console.log("Filter URL:", url, "condition:", condition, "→", conditionParam);
    
    const response = await fetch(url, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    });

    const data = await response.json();
    console.log("Filter response status:", response.status, "data:", data);

    if (response.ok) {
      return data;
    } else {
      console.error("Filter failed:", response.status, data);
      return { appointments: [] };
    }
  } catch (error) {
    console.error("Error filtering appointments:", error);
    alert("Something went wrong!");
    return { appointments: [] };
  }
}