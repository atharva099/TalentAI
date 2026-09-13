import type {
  AuthResponse,
  CurrentUserResponse,
  LoginRequest,
  RegisterRequest,
} from '../../types/auth'
import { get, post } from './client'

const AUTH_PATHS = {
  login: '/auth/login',
  me: '/auth/me',
  register: '/auth/register',
} as const

export function register(
  request: RegisterRequest,
): Promise<AuthResponse> {
  return post<RegisterRequest, AuthResponse['data']>(
    AUTH_PATHS.register,
    request,
    { authenticated: false },
  )
}

export function login(request: LoginRequest): Promise<AuthResponse> {
  return post<LoginRequest, AuthResponse['data']>(
    AUTH_PATHS.login,
    request,
    { authenticated: false },
  )
}

export function getCurrentUser(): Promise<CurrentUserResponse> {
  return get<CurrentUserResponse['data']>(AUTH_PATHS.me, {
    invalidateSessionOnForbidden: true,
  })
}
