import { useEffect, useMemo, useState } from "react";
import { useParams } from "react-router-dom";
import { workoutsApi } from "../api/workouts";
import { exercisesApi } from "../api/exercises";
import { ApiError } from "../api/client";
import { ExerciseSearch } from "../components/ExerciseSearch";
import type { ExerciseResponse, WorkoutResponse } from "../types";

export function WorkoutDetailPage() {
  const { workoutId } = useParams();
  const id = Number(workoutId);

  const [workout, setWorkout] = useState<WorkoutResponse | null>(null);
  const [allExercises, setAllExercises] = useState<ExerciseResponse[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);

  async function reload() {
    const [workoutData, exerciseData] = await Promise.all([
      workoutsApi.getById(id),
      exercisesApi.getAll(),
    ]);
    setWorkout(workoutData);
    setAllExercises(exerciseData);
  }

  useEffect(() => {
    setLoading(true);
    reload()
      .catch(() => setError("Could not load this workout."))
      .finally(() => setLoading(false));
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [id]);

  const exerciseMap = useMemo(
    () => new Map(allExercises.map((exercise) => [exercise.id, exercise])),
    [allExercises],
  );

  const availableToAdd = useMemo(
    () => allExercises.filter((exercise) => !workout?.exerciseIds.includes(exercise.id)),
    [allExercises, workout],
  );

  async function handleAdd(exerciseId: number) {
    try {
      const updated = await workoutsApi.addExercise(id, exerciseId);
      setWorkout(updated);
    } catch (err) {
      setError(err instanceof ApiError ? err.message : "Could not add exercise.");
    }
  }

  function handleExerciseCreated(exercise: ExerciseResponse) {
    setAllExercises((prev) => [...prev, exercise]);
    handleAdd(exercise.id);
  }

  async function handleRemove(exerciseId: number) {
    try {
      const updated = await workoutsApi.removeExercise(id, exerciseId);
      setWorkout(updated);
    } catch (err) {
      setError(err instanceof ApiError ? err.message : "Could not remove exercise.");
    }
  }

  if (loading) return <p className="text-sm text-muted">Loading...</p>;
  if (error && !workout) return <p className="text-sm text-danger">{error}</p>;
  if (!workout) return null;

  return (
    <div className="max-w-lg">
      <h1 className="mb-1 text-2xl font-semibold text-ink">{workout.name}</h1>
      <p className="mb-6 text-sm text-muted">
        Created {new Date(workout.createdAt).toLocaleDateString()}
      </p>

      {error && <p className="mb-4 text-sm text-danger">{error}</p>}

      <div className="mb-6 space-y-2">
        {workout.exerciseIds.length === 0 && (
          <p className="text-sm text-muted">No exercises in this workout yet.</p>
        )}
        {workout.exerciseIds.map((exerciseId) => {
          const exercise = exerciseMap.get(exerciseId);
          return (
            <div
              key={exerciseId}
              className="flex items-center justify-between rounded-lg border border-border bg-surface px-4 py-3 shadow-sm"
            >
              <div>
                <p className="font-medium text-ink">{exercise?.name ?? `Exercise #${exerciseId}`}</p>
                {exercise && <p className="text-xs text-muted">{exercise.level}</p>}
              </div>
              <button
                onClick={() => handleRemove(exerciseId)}
                className="text-sm text-danger hover:underline"
              >
                Remove
              </button>
            </div>
          );
        })}
      </div>

      <p className="mb-2 text-sm font-medium text-ink">Add an exercise</p>
      <ExerciseSearch exercises={availableToAdd} onCreated={handleExerciseCreated}>
        {(filtered) => (
          <div className="max-h-72 space-y-1 overflow-y-auto rounded-md border border-border p-2">
            {filtered.length === 0 && <p className="p-2 text-sm text-muted">No matches.</p>}
            {filtered.map((exercise) => (
              <div
                key={exercise.id}
                className="flex items-center justify-between rounded-md px-2 py-2 text-sm hover:bg-stone-50"
              >
                <span className="flex items-center gap-2">
                  {exercise.name}
                  <span className="text-xs text-muted">{exercise.level}</span>
                </span>
                <button
                  onClick={() => handleAdd(exercise.id)}
                  className="text-sm font-medium text-accent hover:underline"
                >
                  Add
                </button>
              </div>
            ))}
          </div>
        )}
      </ExerciseSearch>
    </div>
  );
}
