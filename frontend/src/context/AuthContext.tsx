import { createContext, useContext, useEffect, useState, type ReactNode } from "react";
import type { LoginResponse, Role } from "../types";

interface AuthState {
  token: string;
  userId: number;
  username: string;
  name: string;
  role: Role;
}

interface AuthContextValue {
  auth: AuthState | null;
  login: (response: LoginResponse) => void;
  logout: () => void;
}

const STORAGE_KEY = "workout_auth";

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

function decodeRole(token: string): Role {
  try {
    const payload = JSON.parse(atob(token.split(".")[1])) as { role?: string };
    return payload.role === "ADMIN" ? "ADMIN" : "USER";
  } catch {
    return "USER";
  }
}

function readStoredAuth(): AuthState | null {
  const raw = localStorage.getItem(STORAGE_KEY);
  if (!raw) return null;
  try {
    return JSON.parse(raw) as AuthState;
  } catch {
    return null;
  }
}

export function AuthProvider({ children }: { children: ReactNode }) {
  const [auth, setAuth] = useState<AuthState | null>(() => readStoredAuth());

  useEffect(() => {
    if (auth) {
      localStorage.setItem(STORAGE_KEY, JSON.stringify(auth));
    } else {
      localStorage.removeItem(STORAGE_KEY);
    }
  }, [auth]);

  function login(response: LoginResponse) {
    setAuth({
      token: response.token,
      userId: response.userResponse.id,
      username: response.username,
      name: response.userResponse.name,
      role: decodeRole(response.token),
    });
  }

  function logout() {
    setAuth(null);
  }

  return <AuthContext.Provider value={{ auth, login, logout }}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error("useAuth must be used within AuthProvider");
  return ctx;
}
