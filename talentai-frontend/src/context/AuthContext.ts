import { createContext } from 'react'
import type { LoginRequest, RegisterRequest } from '../types/auth'

export const AUTH_TOKEN_STORAGE_KEY = 'talentai_access_token'

export type AuthContextValue = {
  isAuthenticated: boolean
  accessToken: string | null
  login: (request: LoginRequest) => Promise<void>
  register: (request: RegisterRequest) => Promise<void>
  logout: () => void
}

export const AuthContext = createContext<AuthContextValue | undefined>(undefined)
