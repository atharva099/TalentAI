import { createContext } from 'react'
import type { CurrentUser, LoginRequest, RegisterRequest } from '../types/auth'

export type AuthContextValue = {
  isAuthenticated: boolean
  accessToken: string | null
  currentUser: CurrentUser | null
  isInitializing: boolean
  login: (request: LoginRequest) => Promise<void>
  register: (request: RegisterRequest) => Promise<void>
  logout: () => void
}

export const AuthContext = createContext<AuthContextValue | undefined>(undefined)
