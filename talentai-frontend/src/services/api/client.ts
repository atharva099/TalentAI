import type { ApiErrorResponse, ApiResponse } from '../../types/api'

export const API_BASE_URL = 'http://localhost:8081/api/v1'

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
): Promise<ApiResponse<TResponse>> {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(request),
  })

  return parseResponse<TResponse>(response)
}
