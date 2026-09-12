import { useEffect, useMemo, useState, type FormEvent } from "react";
import { exercisesApi } from "../api/exercises";
import { ApiError } from "../api/client";
import type { ExerciseResponse, Level } from "../types";

const LEVELS: Level[] = ["EASY", "MEDIUM", "HARD"];

export function ExercisesPage() {
  const [exercises, setExercises] = useState<ExerciseResponse[]>([]);
  const [filter, setFilter] = useState<Level | "ALL">("ALL");
  const [name, setName] = useState("");
  const [level, setLevel] = useState<Level>("EASY");
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);

  function load() {
    exercisesApi
      .getAll()
      .then(setExercises)
      .catch(() => setError("Could not load exercises."))
      .finally(() => setLoading(false));
  }

  useEffect(load, []);

  const visible = useMemo(
    () => (filter === "ALL" ? exercises : exercises.filter((exercise) => exercise.level === filter)),
    [exercises, filter],
  );

  async function handleCreate(event: FormEvent) {
    event.preventDefault();
    setError(null);
    try {
      await exercisesApi.create({ name, level });
      setName("");
      load();
    } catch (err) {
      setError(err instanceof ApiError ? err.message : "Could not add exercise.");
    }
  }

  return (
    <div className="max-w-2xl">
      <h1 className="mb-6 text-2xl font-semibold text-ink">Exercise catalog</h1>

      <form
        onSubmit={handleCreate}
        className="mb-8 flex flex-wrap items-end gap-3 rounded-lg border border-border bg-surface p-4 shadow-sm"
      >
        <div className="min-w-[10rem] flex-1">
          <label className="mb-1 block text-sm font-medium text-ink">New exercise name</label>
          <input
            value={name}
            onChange={(event) => setName(event.target.value)}
            required
            placeholder="e.g. Bulgarian Split Squat"
            className="w-full rounded-md border border-border px-3 py-2 text-sm outline-none focus:border-accent focus:ring-1 focus:ring-accent"
          />
        </div>
        <div>
          <label className="mb-1 block text-sm font-medium text-ink">Level</label>
          <select
            value={level}
            onChange={(event) => setLevel(event.target.value as Level)}
            className="rounded-md border border-border px-3 py-2 text-sm outline-none focus:border-accent focus:ring-1 focus:ring-accent"
          >
            {LEVELS.map((l) => (
              <option key={l} value={l}>
                {l}
              </option>
            ))}
          </select>
        </div>
        <button
          type="submit"
          className="rounded-md bg-accent px-4 py-2 text-sm font-medium text-white transition-colors hover:bg-accent-hover"
        >
          Add
        </button>
      </form>

      {error && <p className="mb-4 text-sm text-danger">{error}</p>}

      <div className="mb-4 flex gap-1">
        {(["ALL", ...LEVELS] as const).map((l) => (
          <button
            key={l}
            onClick={() => setFilter(l)}
            className={`rounded-md px-3 py-1.5 text-sm font-medium transition-colors ${
              filter === l ? "bg-accent text-white" : "text-muted hover:bg-stone-100"
            }`}
          >
            {l === "ALL" ? "All" : l}
          </button>
        ))}
      </div>

      {loading && <p className="text-sm text-muted">Loading...</p>}

      <ul className="space-y-2">
        {visible.map((exercise) => (
          <li
            key={exercise.id}
            className="flex items-center justify-between rounded-lg border border-border bg-surface px-4 py-3 shadow-sm"
          >
            <span className="font-medium text-ink">{exercise.name}</span>
            <span className="text-xs text-muted">{exercise.level}</span>
          </li>
        ))}
      </ul>
    </div>
  );
}
