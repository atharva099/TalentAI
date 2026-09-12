import type { AuthResponse, LoginRequest, RegisterRequest } from '../../types/auth'
import { post } from './client'

const AUTH_PATHS = {
  login: '/auth/login',
  register: '/auth/register',
} as const

export function register(
  request: RegisterRequest,
): Promise<AuthResponse> {
  return post<RegisterRequest, AuthResponse['data']>(AUTH_PATHS.register, request)
}

export function login(request: LoginRequest): Promise<AuthResponse> {
  return post<LoginRequest, AuthResponse['data']>(AUTH_PATHS.login, request)
}
