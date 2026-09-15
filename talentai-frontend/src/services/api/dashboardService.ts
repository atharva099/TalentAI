import type { ApiResponse, DashboardSummary } from '../../types/api'
import { get } from './client'

export function getDashboardSummary(): Promise<ApiResponse<DashboardSummary>> {
  return get<DashboardSummary>('/dashboard/summary')
}
