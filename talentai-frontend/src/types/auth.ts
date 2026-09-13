import type { ApiResponse } from './api'

export type RegisterRequest = {
  firstName: string
  lastName: string
  email: string
  password: string
}

export type LoginRequest = {
  email: string
  password: string
}

export type AuthData = {
  accessToken: string
  tokenType: 'Bearer'
  expiresInSeconds: number
}

export type AuthResponse = ApiResponse<AuthData>

export type CurrentUserRole =
  | 'CANDIDATE'
  | 'RECRUITER'
  | 'COMPANY_ADMIN'
  | 'PLATFORM_ADMIN'

export type CurrentUser = {
  id: number
  firstName: string
  lastName: string
  email: string
  roles: CurrentUserRole[]
}

export type CurrentUserResponse = ApiResponse<CurrentUser>
