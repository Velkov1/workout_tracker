import { NavLink, Outlet } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

export function Layout() {
  const { auth, logout } = useAuth();

  const linkClass = ({ isActive }: { isActive: boolean }) =>
    `rounded-md px-3 py-2 text-sm font-medium transition-colors ${
      isActive ? "bg-accent text-white" : "text-muted hover:bg-stone-100 hover:text-ink"
    }`;

  return (
    <div className="min-h-screen bg-bg">
      <header className="border-b border-border bg-surface">
        <div className="mx-auto flex max-w-4xl items-center justify-between px-6 py-4">
          <span className="text-lg font-semibold text-ink">Workout Tracker</span>

          <nav className="flex items-center gap-1">
            <NavLink to="/workouts" className={linkClass}>
              Workouts
            </NavLink>
            <NavLink to="/exercises" className={linkClass}>
              Exercises
            </NavLink>
            {auth?.role === "ADMIN" && (
              <NavLink to="/admin" className={linkClass}>
                Admin
              </NavLink>
            )}
          </nav>

          <div className="flex items-center gap-3">
            <span className="text-sm text-muted">{auth?.name}</span>
            <button
              onClick={logout}
              className="rounded-md border border-border px-3 py-1.5 text-sm text-ink transition-colors hover:bg-stone-100"
            >
              Log out
            </button>
          </div>
        </div>
      </header>

      <main className="mx-auto max-w-4xl px-6 py-8">
        <Outlet />
      </main>
    </div>
  );
}
