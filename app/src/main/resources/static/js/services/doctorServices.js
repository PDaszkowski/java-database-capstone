// doctorServices.js
import { API_BASE_URL } from "../config/config.js";
const DOCTOR_API = API_BASE_URL + '/doctor'
export async function getDoctors() {
    try {
      const response = await fetch(DOCTOR_API);
      const data = await response.json();
      return data.doctors;
    } catch (error) {
      console.error("Error fetching doctors:", error);
      return [];
    }
  }

  export async function deleteDoctor(id, token) {
    try {
      const response = await fetch(`${DOCTOR_API}/${id}/${token}`, {
        method: "DELETE",
      });
  
      const result = await response.json();
      return { success: response.ok, message: result.message };
    } catch (error) {
      console.error("Error deleting doctor:", error);
      return { success: false, message: "Server error" };
    }
  }

  export async function saveDoctor(doctor , token){
    try {
      const response = await fetch(`${DOCTOR_API}/${token}`,{
        method : "POST",
        headers : {
          "Content-type" : "application/json"
        },
        body:JSON.stringify(doctor)
      });
      const result = await response.json();
      return {success : response.ok , message : result.message}
    }
    catch(error) {
      console.error("Error :: saveDoctor :: " , error)
      return { success : false , message : result.message}
    }
  }

export async function filterDoctors(name, time, specialty) {
  try {
    // Dodaj wartości domyślne dla parametrów
    const nameParam = name || "all";
    const timeParam = time || "all";
    const specialtyParam = specialty || "all";
    
    console.log(`Filtering doctors with params: name=${nameParam}, time=${timeParam}, specialty=${specialtyParam}`); // Debug log
    
    const response = await fetch(`${DOCTOR_API}/filter/${nameParam}/${timeParam}/${specialtyParam}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    });

    if (response.ok) {
      const data = await response.json();
      console.log("Filtered doctors response:", data); // Debug log
      return data;
    } else {
      console.error("Failed to fetch doctors:", response.statusText);
      return { doctors: [] };
    }
  } catch (error) {
    console.error("Error filtering doctors:", error);
    return { doctors: [] };
  }
}

  
