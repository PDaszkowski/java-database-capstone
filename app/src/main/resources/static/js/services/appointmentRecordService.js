import { API_BASE_URL } from "../config/config.js";
const APPOINTMENT_API = `${API_BASE_URL}/appointments`;


//This is for the doctor to get all the patient Appointments
export async function getAllAppointments(date, patientName, token) {
  console.log("=== getAllAppointments called ===");
  console.log("date:", date);
  console.log("patientName:", patientName);
  console.log("token:", token ? token.substring(0, 20) + "..." : "NO TOKEN");
  
  const url = `${APPOINTMENT_API}/${date}/${patientName}/${token}`;
  console.log("Full URL:", url);
  console.log("APPOINTMENT_API:", APPOINTMENT_API);
  
  try {
    console.log("Making fetch request...");
    const response = await fetch(url);
    console.log("Response received:", response.status, response.statusText);
    
    if (!response.ok) {
      const errorText = await response.text();
      console.error("Response error body:", errorText);
      throw new Error(`Failed to fetch appointments: ${response.status} - ${errorText}`);
    }
    
    const data = await response.json();
    console.log("Response data:", data);
    return data;
  } catch (error) {
    console.error("=== getAllAppointments ERROR ===", error);
    throw error;
  }
}

export async function bookAppointment(appointment, token) {
  try {
    const response = await fetch(`${APPOINTMENT_API}/${token}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(appointment)
    });

    const data = await response.json();
    return {
      success: response.ok,
      message: data.message || "Something went wrong"
    };
  } catch (error) {
    console.error("Error while booking appointment:", error);
    return {
      success: false,
      message: "Network error. Please try again later."
    };
  }
}

export async function updateAppointment(appointment, token) {
  try {
    const response = await fetch(`${APPOINTMENT_API}/${token}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(appointment)
    });

    const data = await response.json();
    return {
      success: response.ok,
      message: data.message || "Something went wrong"
    };
  } catch (error) {
    console.error("Error while booking appointment:", error);
    return {
      success: false,
      message: "Network error. Please try again later."
    };
  }
}