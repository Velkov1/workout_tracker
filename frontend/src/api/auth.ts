import { api } from "./client";
import type { LoginRequest, LoginResponse, RegisterRequest, RegisterResponse } from "../types";

export const authApi = {
  register: (data: RegisterRequest) => api.post<RegisterResponse>("/api/auth/register", data),
  login: (data: LoginRequest) => api.post<LoginResponse>("/api/auth/login", data),
  me: () => api.get<LoginResponse>("/api/auth/me"),
};
