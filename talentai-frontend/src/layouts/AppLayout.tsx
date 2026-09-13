import { BrandMark } from '../components/BrandMark'
import { Outlet } from 'react-router-dom'

export function AppLayout() {
  return (
    <div className="app-shell">
      <header className="app-header">
        <BrandMark />
        <span className="app-header__status">Frontend foundation</span>
      </header>
      <main className="app-content">
        <Outlet />
      </main>
      <footer className="app-footer">TalentAI Recruitment Management System</footer>
    </div>
  )
}
