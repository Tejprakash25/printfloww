import { apiRequest } from "./apiClient";

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  fullName: string;
  email: string;
  password: string;
  role: string;
}

export async function login(data: LoginRequest) {
  const response = await apiRequest("/auth/login", {
    method: "POST",
    body: JSON.stringify(data),
  });

  if (response?.token) {
    localStorage.setItem("printflow_token", response.token);
  }

  if (response?.user) {
    localStorage.setItem("printflow_user", JSON.stringify(response.user));
  }

  return response;
}

export async function register(data: RegisterRequest) {
  return apiRequest("/auth/register", {
    method: "POST",
    body: JSON.stringify(data),
  });
}

export function logout() {
  localStorage.removeItem("printflow_token");
  localStorage.removeItem("printflow_user");
}

export function getToken() {
  return localStorage.getItem("printflow_token");
}

export function getStoredUser() {
  const user = localStorage.getItem("printflow_user");

  return user ? JSON.parse(user) : null;
}