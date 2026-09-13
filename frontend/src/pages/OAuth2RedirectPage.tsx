import { useEffect, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import { authApi } from "../api/auth";
import { useAuth } from "../context/AuthContext";

const STORAGE_KEY = "workout_auth";

export function OAuth2RedirectPage() {
  const [searchParams] = useSearchParams();
  const { login } = useAuth();
  const navigate = useNavigate();
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const token = searchParams.get("token");
    if (!token) {
      setError("Missing token from Google sign-in.");
      return;
    }

    localStorage.setItem(STORAGE_KEY, JSON.stringify({ token }));
    authApi
      .me()
      .then((response) => {
        login(response);
        navigate("/workouts", { replace: true });
      })
      .catch(() => {
        localStorage.removeItem(STORAGE_KEY);
        setError("Could not complete Google sign-in.");
      });
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return (
    <div className="flex min-h-screen items-center justify-center bg-bg px-6">
      {error ? (
        <p className="text-sm text-danger">{error}</p>
      ) : (
        <p className="text-sm text-muted">Signing you in...</p>
      )}
    </div>
  );
}
