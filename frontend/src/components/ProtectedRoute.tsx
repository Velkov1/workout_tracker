import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export function ProtectedRoute({ adminOnly = false }: { adminOnly?: boolean }) {
  const { auth } = useAuth();

  if (!auth) {
    return <Navigate to="/login" replace />;
  }

  if (adminOnly && auth.role !== "ADMIN") {
    return <Navigate to="/workouts" replace />;
  }

  return <Outlet />;
}
