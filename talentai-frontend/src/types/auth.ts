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
