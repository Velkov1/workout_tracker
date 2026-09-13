import { Navigate, Route, Routes } from "react-router-dom";
import { AuthProvider, useAuth } from "./context/AuthContext";
import { ProtectedRoute } from "./components/ProtectedRoute";
import { Layout } from "./components/Layout";
import { LoginPage } from "./pages/LoginPage";
import { RegisterPage } from "./pages/RegisterPage";
import { OAuth2RedirectPage } from "./pages/OAuth2RedirectPage";
import { WorkoutsPage } from "./pages/WorkoutsPage";
import { CreateWorkoutPage } from "./pages/CreateWorkoutPage";
import { WorkoutDetailPage } from "./pages/WorkoutDetailPage";
import { ExercisesPage } from "./pages/ExercisesPage";
import { AdminPage } from "./pages/AdminPage";

function AppRoutes() {
  const { auth } = useAuth();

  return (
    <Routes>
      <Route path="/login" element={auth ? <Navigate to="/workouts" replace /> : <LoginPage />} />
      <Route path="/register" element={auth ? <Navigate to="/workouts" replace /> : <RegisterPage />} />
      <Route path="/oauth2/redirect" element={<OAuth2RedirectPage />} />

      <Route element={<ProtectedRoute />}>
        <Route element={<Layout />}>
          <Route path="/workouts" element={<WorkoutsPage />} />
          <Route path="/workouts/new" element={<CreateWorkoutPage />} />
          <Route path="/workouts/:workoutId" element={<WorkoutDetailPage />} />
          <Route path="/exercises" element={<ExercisesPage />} />
          <Route element={<ProtectedRoute adminOnly />}>
            <Route path="/admin" element={<AdminPage />} />
          </Route>
        </Route>
      </Route>

      <Route path="*" element={<Navigate to={auth ? "/workouts" : "/login"} replace />} />
    </Routes>
  );
}

export default function App() {
  return (
    <AuthProvider>
      <AppRoutes />
    </AuthProvider>
  );
}
