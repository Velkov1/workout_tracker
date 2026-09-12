import { useEffect, useState } from "react";
import { usersApi } from "../api/users";
import { exercisesApi } from "../api/exercises";
import { ApiError } from "../api/client";
import type { ExerciseResponse, Level, UserResponse } from "../types";

const LEVELS: Level[] = ["EASY", "MEDIUM", "HARD"];

export function AdminPage() {
  const [tab, setTab] = useState<"users" | "exercises">("users");

  return (
    <div>
      <h1 className="mb-6 text-2xl font-semibold text-ink">Admin</h1>
      <div className="mb-6 flex gap-1">
        <button
          onClick={() => setTab("users")}
          className={`rounded-md px-3 py-1.5 text-sm font-medium ${
            tab === "users" ? "bg-accent text-white" : "text-muted hover:bg-stone-100"
          }`}
        >
          Users
        </button>
        <button
          onClick={() => setTab("exercises")}
          className={`rounded-md px-3 py-1.5 text-sm font-medium ${
            tab === "exercises" ? "bg-accent text-white" : "text-muted hover:bg-stone-100"
          }`}
        >
          Exercises
        </button>
      </div>
      {tab === "users" ? <AdminUsers /> : <AdminExercises />}
    </div>
  );
}

function AdminUsers() {
  const [users, setUsers] = useState<UserResponse[]>([]);
  const [error, setError] = useState<string | null>(null);

  function load() {
    usersApi.getAll().then(setUsers).catch(() => setError("Could not load users."));
  }

  useEffect(load, []);

  async function handleDelete(id: number) {
    if (!confirm("Delete this user? This cannot be undone.")) return;
    try {
      await usersApi.delete(id);
      load();
    } catch (err) {
      setError(err instanceof ApiError ? err.message : "Could not delete user.");
    }
  }

  return (
    <div>
      {error && <p className="mb-4 text-sm text-danger">{error}</p>}
      <ul className="space-y-2">
        {users.map((user) => (
          <li
            key={user.id}
            className="flex items-center justify-between rounded-lg border border-border bg-surface px-4 py-3 shadow-sm"
          >
            <div>
              <p className="font-medium text-ink">{user.name}</p>
              <p className="text-xs text-muted">{user.workoutIds.length} workout(s)</p>
            </div>
            <button onClick={() => handleDelete(user.id)} className="text-sm text-danger hover:underline">
              Delete
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}

function AdminExercises() {
  const [exercises, setExercises] = useState<ExerciseResponse[]>([]);
  const [error, setError] = useState<string | null>(null);

  function load() {
    exercisesApi.getAll().then(setExercises).catch(() => setError("Could not load exercises."));
  }

  useEffect(load, []);

  async function handleRename(id: number, currentName: string) {
    const name = prompt("New name", currentName);
    if (!name || name === currentName) return;
    try {
      await exercisesApi.changeName(id, name);
      load();
    } catch (err) {
      setError(err instanceof ApiError ? err.message : "Could not rename exercise.");
    }
  }

  async function handleLevelChange(id: number, level: Level) {
    try {
      await exercisesApi.changeLevel(id, level);
      load();
    } catch (err) {
      setError(err instanceof ApiError ? err.message : "Could not update level.");
    }
  }

  async function handleDelete(id: number) {
    if (!confirm("Delete this exercise? It will be removed from every workout using it.")) return;
    try {
      await exercisesApi.delete(id);
      load();
    } catch (err) {
      setError(err instanceof ApiError ? err.message : "Could not delete exercise.");
    }
  }

  return (
    <div>
      {error && <p className="mb-4 text-sm text-danger">{error}</p>}
      <ul className="space-y-2">
        {exercises.map((exercise) => (
          <li
            key={exercise.id}
            className="flex items-center justify-between rounded-lg border border-border bg-surface px-4 py-3 shadow-sm"
          >
            <button
              onClick={() => handleRename(exercise.id, exercise.name)}
              className="font-medium text-ink hover:underline"
            >
              {exercise.name}
            </button>
            <div className="flex items-center gap-3">
              <select
                value={exercise.level}
                onChange={(event) => handleLevelChange(exercise.id, event.target.value as Level)}
                className="rounded-md border border-border px-2 py-1 text-xs outline-none focus:border-accent"
              >
                {LEVELS.map((l) => (
                  <option key={l} value={l}>
                    {l}
                  </option>
                ))}
              </select>
              <button onClick={() => handleDelete(exercise.id)} className="text-sm text-danger hover:underline">
                Delete
              </button>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
}
