import { api } from "./client";
import type { WorkoutRequest, WorkoutResponse } from "../types";

export const workoutsApi = {
  create: (data: WorkoutRequest) => api.post<WorkoutResponse>("/api/workouts", data),
  getById: (id: number) => api.get<WorkoutResponse>(`/api/workouts/${id}`),
  addExercise: (workoutId: number, exerciseId: number) =>
    api.patch<WorkoutResponse>(`/api/workouts/${workoutId}/exercises/${exerciseId}/add`),
  removeExercise: (workoutId: number, exerciseId: number) =>
    api.patch<WorkoutResponse>(`/api/workouts/${workoutId}/exercises/${exerciseId}/remove`),
  getAllForUser: (userId: number) => api.get<WorkoutResponse[]>(`/api/users/${userId}/workouts`),
};
