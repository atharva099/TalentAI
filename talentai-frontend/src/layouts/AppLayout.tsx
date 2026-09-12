import type { ReactNode } from 'react'
import { BrandMark } from '../components/BrandMark'

type AppLayoutProps = {
  children: ReactNode
}

export function AppLayout({ children }: AppLayoutProps) {
  return (
    <div className="app-shell">
      <header className="app-header">
        <BrandMark />
        <span className="app-header__status">Frontend foundation</span>
      </header>
      <main className="app-content">{children}</main>
      <footer className="app-footer">TalentAI Recruitment Management System</footer>
    </div>
  )
}
