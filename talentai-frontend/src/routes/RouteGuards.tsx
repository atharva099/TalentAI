import { Navigate, Outlet } from 'react-router-dom'
import { useAuth } from '../context/useAuth'
import type { CurrentUserRole } from '../types/auth'

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

type RoleProtectedRouteProps = {
  allowedRoles: CurrentUserRole[]
}

export function RoleProtectedRoute({ allowedRoles }: RoleProtectedRouteProps) {
  const { currentUser } = useAuth()

  if (!currentUser || !currentUser.roles.some((role) => allowedRoles.includes(role))) {
    return <Navigate to="/" replace />
  }

  return <Outlet />
}
