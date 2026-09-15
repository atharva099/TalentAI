import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom'
import { AppLayout } from '../layouts/AppLayout'
import { CandidateDetailsPage } from '../pages/CandidateDetailsPage'
import { CandidateFormPage } from '../pages/CandidateFormPage'
import { CandidatesPage } from '../pages/CandidatesPage'
import { DashboardPage } from '../pages/DashboardPage'
import { AuthenticatedHomePage } from '../pages/AuthenticatedHomePage'
import { HomePage } from '../pages/HomePage'
import { LoginPage } from '../pages/LoginPage'
import {
  ProtectedRoute,
  PublicOnlyRoute,
  RoleProtectedRoute,
} from './RouteGuards'

export function AppRoutes() {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<PublicOnlyRoute />}>
          <Route path="/login" element={<LoginPage />} />
        </Route>
        <Route element={<ProtectedRoute />}>
          <Route element={<AppLayout />}>
            <Route path="/" element={<AuthenticatedHomePage />} />
            <Route element={<RoleProtectedRoute allowedRoles={['RECRUITER', 'PLATFORM_ADMIN']} />}>
              <Route path="/dashboard" element={<DashboardPage />} />
              <Route path="/candidates" element={<CandidatesPage />} />
              <Route path="/candidates/new" element={<CandidateFormPage />} />
              <Route path="/candidates/:candidateId/edit" element={<CandidateFormPage />} />
              <Route path="/candidates/:candidateId" element={<CandidateDetailsPage />} />
            </Route>
            <Route path="/foundation" element={<HomePage />} />
          </Route>
        </Route>
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </BrowserRouter>
  )
}
