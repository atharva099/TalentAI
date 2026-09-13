import { Navigate, Outlet } from 'react-router-dom'
import { useAuth } from '../context/useAuth'

function RouteLoadingState() {
  return (
    <main className="route-loading" aria-live="polite">
      Restoring your TalentAI session...
    </main>
  )
}

export function ProtectedRoute() {
  const { isAuthenticated, isInitializing } = useAuth()

  if (isInitializing) {
    return <RouteLoadingState />
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />
  }

  return <Outlet />
}

export function PublicOnlyRoute() {
  const { isAuthenticated, isInitializing } = useAuth()

  if (isInitializing) {
    return <RouteLoadingState />
  }

  if (isAuthenticated) {
    return <Navigate to="/" replace />
  }

  return <Outlet />
}
