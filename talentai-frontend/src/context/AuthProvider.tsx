import { useCallback, useEffect, useMemo, useState, type ReactNode } from 'react'
import {
  getCurrentUser,
  login as loginRequest,
  register as registerRequest,
} from '../services/api/authService'
import {
  clearAccessToken,
  getAccessToken,
  setAccessToken,
} from '../services/auth/authSession'
import type { CurrentUser, LoginRequest, RegisterRequest } from '../types/auth'
import { AuthContext } from './AuthContext'

type AuthProviderProps = {
  children: ReactNode
}

export function AuthProvider({ children }: AuthProviderProps) {
  const [accessToken, setToken] = useState<string | null>(getAccessToken)
  const [currentUser, setCurrentUser] = useState<CurrentUser | null>(null)
  const [isInitializing, setIsInitializing] = useState(accessToken !== null)

  const storeToken = useCallback((token: string) => {
    setAccessToken(token)
    setToken(token)
    setIsInitializing(true)
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
    clearAccessToken()
    setToken(null)
    setCurrentUser(null)
    setIsInitializing(false)
  }, [])

  useEffect(() => {
    if (accessToken === null) {
      return
    }

    let isMounted = true

    getCurrentUser()
      .then((response) => {
        if (isMounted) {
          setCurrentUser(response.data)
        }
      })
      .catch(() => {
        if (isMounted && getAccessToken() === null) {
          setToken(null)
          setCurrentUser(null)
        }
      })
      .finally(() => {
        if (isMounted) {
          setIsInitializing(false)
        }
      })

    return () => {
      isMounted = false
    }
  }, [accessToken])

  const value = useMemo(
    () => ({
      isAuthenticated: accessToken !== null,
      accessToken,
      currentUser,
      isInitializing,
      login,
      register,
      logout,
    }),
    [accessToken, currentUser, isInitializing, login, register, logout],
  )

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}
