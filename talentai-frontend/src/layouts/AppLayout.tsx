import { BrandMark } from '../components/BrandMark'
import { Link, Outlet } from 'react-router-dom'
import { useAuth } from '../context/useAuth'

export function AppLayout() {
  const { currentUser, logout } = useAuth()
  const canViewDashboard = currentUser?.roles.some(
    (role) => role === 'RECRUITER' || role === 'PLATFORM_ADMIN',
  )
  const canManageCandidates = currentUser?.roles.some(
    (role) => role === 'RECRUITER' || role === 'PLATFORM_ADMIN',
  )

  return (
    <div className="app-shell">
      <header className="app-header">
        <BrandMark />
        <nav className="app-header__nav" aria-label="Application navigation">
          <Link to="/">Home</Link>
          {canViewDashboard && <Link to="/dashboard">Dashboard</Link>}
          {canManageCandidates && <Link to="/candidates">Candidates</Link>}
          <button type="button" onClick={logout}>Log out</button>
        </nav>
      </header>
      <main className="app-content">
        <Outlet />
      </main>
      <footer className="app-footer">TalentAI Recruitment Management System</footer>
    </div>
  )
}
