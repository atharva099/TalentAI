import type { ApiErrorResponse, ApiResponse } from '../../types/api'
import {
  clearAccessToken,
  getAccessToken,
} from '../auth/authSession'

export const API_BASE_URL = 'http://localhost:8081/api/v1'

export type PostOptions = {
  authenticated?: boolean
  invalidateSessionOnForbidden?: boolean
}

export class ApiClientError extends Error {
  public readonly status: number
  public readonly response: ApiErrorResponse

  constructor(
    status: number,
    response: ApiErrorResponse,
  ) {
    super(response.message)
    this.name = 'ApiClientError'
    this.status = status
    this.response = response
  }
}

async function parseResponse<T>(response: Response): Promise<ApiResponse<T>> {
  const body: unknown = await response.json()

  if (!response.ok) {
    throw new ApiClientError(response.status, body as ApiErrorResponse)
  }

  return body as ApiResponse<T>
}

export async function post<TRequest, TResponse>(
  path: string,
  request: TRequest,
  options: PostOptions = {},
): Promise<ApiResponse<TResponse>> {
  const authenticated = options.authenticated ?? true
  const accessToken = authenticated ? getAccessToken() : null
  const headers: Record<string, string> = {
    'Content-Type': 'application/json',
  }

  if (accessToken) {
    headers.Authorization = `Bearer ${accessToken}`
  }

  const response = await fetch(`${API_BASE_URL}${path}`, {
    method: 'POST',
    headers,
    body: JSON.stringify(request),
  })

  if (response.status === 401 && authenticated) {
    clearAccessToken()

    if (window.location.pathname !== '/login') {
      window.location.assign('/login')
    }
  }

  return parseResponse<TResponse>(response)
}

export async function get<TResponse>(
  path: string,
  options: PostOptions = {},
): Promise<ApiResponse<TResponse>> {
  const authenticated = options.authenticated ?? true
  const accessToken = authenticated ? getAccessToken() : null
  const headers: Record<string, string> = {}

  if (accessToken) {
    headers.Authorization = `Bearer ${accessToken}`
  }

  const response = await fetch(`${API_BASE_URL}${path}`, {
    method: 'GET',
    headers,
  })

  const shouldInvalidateSession = authenticated
    && (response.status === 401
      || (response.status === 403 && options.invalidateSessionOnForbidden === true))

  if (shouldInvalidateSession) {
    clearAccessToken()

    if (window.location.pathname !== '/login') {
      window.location.assign('/login')
    }
  }

  return parseResponse<TResponse>(response)
}
