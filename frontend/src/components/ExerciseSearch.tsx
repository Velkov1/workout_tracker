import { useMemo, useState, type ReactNode } from "react";
import { exercisesApi } from "../api/exercises";
import { ApiError } from "../api/client";
import type { ExerciseResponse, Level } from "../types";

const LEVELS: Level[] = ["EASY", "MEDIUM", "HARD"];

interface ExerciseSearchProps {
  exercises: ExerciseResponse[];
  onCreated: (exercise: ExerciseResponse) => void;
  children: (filtered: ExerciseResponse[]) => ReactNode;
}

export function ExerciseSearch({ exercises, onCreated, children }: ExerciseSearchProps) {
  const [query, setQuery] = useState("");
  const [level, setLevel] = useState<Level>("EASY");
  const [creating, setCreating] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const trimmedQuery = query.trim();

  const filtered = useMemo(() => {
    if (!trimmedQuery) return exercises;
    const q = trimmedQuery.toLowerCase();
    return exercises.filter((exercise) => exercise.name.toLowerCase().includes(q));
  }, [exercises, trimmedQuery]);

  const exactMatch = useMemo(
    () => exercises.some((exercise) => exercise.name.toLowerCase() === trimmedQuery.toLowerCase()),
    [exercises, trimmedQuery],
  );

  const canOfferCreate = trimmedQuery !== "" && !exactMatch;

  async function handleCreate() {
    setError(null);
    setCreating(true);
    try {
      const exercise = await exercisesApi.create({ name: trimmedQuery, level });
      onCreated(exercise);
      setQuery("");
      setLevel("EASY");
    } catch (err) {
      setError(err instanceof ApiError ? err.message : "Could not add exercise.");
    } finally {
      setCreating(false);
    }
  }

  return (
    <div>
      <input
        value={query}
        onChange={(event) => setQuery(event.target.value)}
        placeholder="Search exercises..."
        className="w-full rounded-md border border-border px-3 py-2 text-sm outline-none focus:border-accent focus:ring-1 focus:ring-accent"
      />

      {canOfferCreate && (
        <div className="mt-2 flex flex-wrap items-center gap-2 rounded-md border border-dashed border-border p-2">
          <span className="text-sm text-muted">No exercise named "{trimmedQuery}" yet.</span>
          <select
            value={level}
            onChange={(event) => setLevel(event.target.value as Level)}
            className="rounded-md border border-border px-2 py-1 text-sm outline-none focus:border-accent focus:ring-1 focus:ring-accent"
          >
            {LEVELS.map((l) => (
              <option key={l} value={l}>
                {l}
              </option>
            ))}
          </select>
          <button
            type="button"
            onClick={handleCreate}
            disabled={creating}
            className="rounded-md bg-accent px-3 py-1.5 text-sm font-medium text-white transition-colors hover:bg-accent-hover disabled:opacity-60"
          >
            {creating ? "Creating..." : `Create "${trimmedQuery}"`}
          </button>
        </div>
      )}
      {error && <p className="mt-1 text-sm text-danger">{error}</p>}

      <div className="mt-3">{children(filtered)}</div>
    </div>
  );
}
