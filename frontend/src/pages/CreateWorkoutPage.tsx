import { useEffect, useState, type FormEvent } from "react";
import { useNavigate } from "react-router-dom";
import { exercisesApi } from "../api/exercises";
import { workoutsApi } from "../api/workouts";
import { ApiError } from "../api/client";
import { ExerciseSearch } from "../components/ExerciseSearch";
import type { ExerciseResponse } from "../types";

export function CreateWorkoutPage() {
  const [name, setName] = useState("");
  const [exercises, setExercises] = useState<ExerciseResponse[]>([]);
  const [selected, setSelected] = useState<Set<number>>(new Set());
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    exercisesApi.getAll().then(setExercises).catch(() => setError("Could not load exercises."));
  }, []);

  function toggle(id: number) {
    setSelected((prev) => {
      const next = new Set(prev);
      if (next.has(id)) {
        next.delete(id);
      } else {
        next.add(id);
      }
      return next;
    });
  }

  function handleExerciseCreated(exercise: ExerciseResponse) {
    setExercises((prev) => [...prev, exercise]);
    setSelected((prev) => new Set(prev).add(exercise.id));
  }

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    if (selected.size === 0) {
      setError("Pick at least one exercise.");
      return;
    }
    setError(null);
    setLoading(true);
    try {
      const workout = await workoutsApi.create({ name, exerciseIds: Array.from(selected) });
      navigate(`/workouts/${workout.id}`);
    } catch (err) {
      setError(err instanceof ApiError ? err.message : "Could not create the workout.");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="max-w-lg">
      <h1 className="mb-6 text-2xl font-semibold text-ink">New workout</h1>
      <form onSubmit={handleSubmit} className="space-y-6">
        <div>
          <label className="mb-1 block text-sm font-medium text-ink">Name</label>
          <input
            value={name}
            onChange={(event) => setName(event.target.value)}
            required
            placeholder="e.g. Push Day"
            className="w-full rounded-md border border-border px-3 py-2 text-sm outline-none focus:border-accent focus:ring-1 focus:ring-accent"
          />
        </div>

        <div>
          <p className="mb-2 text-sm font-medium text-ink">Exercises</p>
          <ExerciseSearch exercises={exercises} onCreated={handleExerciseCreated}>
            {(filtered) => (
              <div className="max-h-72 space-y-1 overflow-y-auto rounded-md border border-border p-2">
                {filtered.length === 0 && <p className="p-2 text-sm text-muted">No matches.</p>}
                {filtered.map((exercise) => (
                  <label
                    key={exercise.id}
                    className="flex cursor-pointer items-center justify-between rounded-md px-2 py-2 text-sm hover:bg-stone-50"
                  >
                    <span className="flex items-center gap-2">
                      <input
                        type="checkbox"
                        checked={selected.has(exercise.id)}
                        onChange={() => toggle(exercise.id)}
                        className="h-4 w-4"
                      />
                      {exercise.name}
                    </span>
                    <span className="text-xs text-muted">{exercise.level}</span>
                  </label>
                ))}
              </div>
            )}
          </ExerciseSearch>
        </div>

        {error && <p className="text-sm text-danger">{error}</p>}

        <button
          type="submit"
          disabled={loading}
          className="rounded-md bg-accent px-4 py-2 text-sm font-medium text-white transition-colors hover:bg-accent-hover disabled:opacity-60"
        >
          {loading ? "Creating..." : "Create workout"}
        </button>
      </form>
    </div>
  );
}
