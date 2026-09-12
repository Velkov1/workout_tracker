import { api } from "./client";
import type { ExerciseRequest, ExerciseResponse, Level } from "../types";

export const exercisesApi = {
  getAll: () => api.get<ExerciseResponse[]>("/api/exercises"),
  create: (data: ExerciseRequest) => api.post<ExerciseResponse>("/api/exercises", data),
  delete: (id: number) => api.delete<void>(`/api/exercises/${id}`),
  changeName: (id: number, name: string) =>
    api.patch<ExerciseResponse>(`/api/exercises/${id}/name?name=${encodeURIComponent(name)}`),
  changeLevel: (id: number, level: Level) =>
    api.patch<ExerciseResponse>(`/api/exercises/${id}/level?level=${level}`),
};
