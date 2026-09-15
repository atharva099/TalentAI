export type ApiResponse<T> = {
  success: boolean
  message: string
  data: T
  timestamp: string
  requestId: string | null
}

export type ApiErrorResponse = {
  success: false
  errorCode: string
  message: string
  timestamp: string
  requestId: string | null
  validationErrors?: Record<string, string[]>
}

export type DashboardSummary = {
  candidateCount: number
  jobCount: number
  resumeCount: number
}
