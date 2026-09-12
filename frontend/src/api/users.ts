import { api } from "./client";
import type { UserResponse } from "../types";

export const usersApi = {
  getById: (id: number) => api.get<UserResponse>(`/api/users/${id}`),
  changeName: (id: number, name: string) =>
    api.patch<UserResponse>(`/api/users/${id}/name?name=${encodeURIComponent(name)}`),
  delete: (id: number) => api.delete<void>(`/api/users/${id}`),
  getAll: () => api.get<UserResponse[]>("/api/users/all"),
};
