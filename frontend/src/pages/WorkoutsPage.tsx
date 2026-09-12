import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { workoutsApi } from "../api/workouts";
import { useAuth } from "../context/AuthContext";
import type { WorkoutResponse } from "../types";

export function WorkoutsPage() {
  const { auth } = useAuth();
  const [workouts, setWorkouts] = useState<WorkoutResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!auth) return;
    workoutsApi
      .getAllForUser(auth.userId)
      .then(setWorkouts)
      .catch(() => setError("Could not load your workouts."))
      .finally(() => setLoading(false));
  }, [auth]);

  return (
    <div>
      <div className="mb-6 flex items-center justify-between">
        <h1 className="text-2xl font-semibold text-ink">Your workouts</h1>
        <Link
          to="/workouts/new"
          className="rounded-md bg-accent px-4 py-2 text-sm font-medium text-white transition-colors hover:bg-accent-hover"
        >
          + New Workout
        </Link>
      </div>

      {loading && <p className="text-sm text-muted">Loading...</p>}
      {error && <p className="text-sm text-danger">{error}</p>}

      {!loading && !error && workouts.length === 0 && (
        <p className="text-sm text-muted">No workouts yet — create your first one.</p>
      )}

      <ul className="space-y-2">
        {workouts.map((workout) => (
          <li key={workout.id}>
            <Link
              to={`/workouts/${workout.id}`}
              className="flex items-center justify-between rounded-lg border border-border bg-surface px-4 py-3 shadow-sm transition-colors hover:border-accent"
            >
              <span className="font-medium text-ink">{workout.name}</span>
              <span className="text-sm text-muted">{workout.exerciseIds.length} exercise(s)</span>
            </Link>
          </li>
        ))}
      </ul>
    </div>
  );
}
