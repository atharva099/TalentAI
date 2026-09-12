import { useCallback, useMemo, useState, type ReactNode } from 'react'
import { login as loginRequest, register as registerRequest } from '../services/api/authService'
import type { LoginRequest, RegisterRequest } from '../types/auth'
import { AUTH_TOKEN_STORAGE_KEY, AuthContext } from './AuthContext'

type AuthProviderProps = {
  children: ReactNode
}

function readStoredToken() {
  return sessionStorage.getItem(AUTH_TOKEN_STORAGE_KEY)
}

export function AuthProvider({ children }: AuthProviderProps) {
  const [accessToken, setAccessToken] = useState<string | null>(readStoredToken)

  const storeToken = useCallback((token: string) => {
    sessionStorage.setItem(AUTH_TOKEN_STORAGE_KEY, token)
    setAccessToken(token)
  }, [])

  const login = useCallback(async (request: LoginRequest) => {
    const response = await loginRequest(request)
    storeToken(response.data.accessToken)
  }, [storeToken])

  const register = useCallback(async (request: RegisterRequest) => {
    const response = await registerRequest(request)
    storeToken(response.data.accessToken)
  }, [storeToken])

  const logout = useCallback(() => {
    sessionStorage.removeItem(AUTH_TOKEN_STORAGE_KEY)
    setAccessToken(null)
  }, [])

  const value = useMemo(
    () => ({
      isAuthenticated: accessToken !== null,
      accessToken,
      login,
      register,
      logout,
    }),
    [accessToken, login, register, logout],
  )

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}
