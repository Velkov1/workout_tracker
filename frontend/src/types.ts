export type Level = "EASY" | "MEDIUM" | "HARD";
export type Role = "ADMIN" | "USER";

export interface RegisterRequest {
  username: string;
  password: string;
  name: string;
}

export interface RegisterResponse {
  id: number;
  username: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface UserResponse {
  id: number;
  name: string;
  workoutIds: number[];
  createdAt: string;
}

export interface LoginResponse {
  token: string;
  username: string;
  userResponse: UserResponse;
}

export interface WorkoutRequest {
  name: string;
  exerciseIds: number[];
}

export interface WorkoutResponse {
  id: number;
  name: string;
  userId: number;
  createdAt: string;
  exerciseIds: number[];
}

export interface ExerciseRequest {
  name: string;
  level: Level;
}

export interface ExerciseResponse {
  id: number;
  name: string;
  level: Level;
}
