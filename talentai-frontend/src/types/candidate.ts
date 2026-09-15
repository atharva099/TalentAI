import type { ApiResponse } from './api'

export type CandidateRequest = {
  firstName: string
  lastName: string
  email: string
  phone?: string
  skills?: string
  experienceYears?: number
}

export type Candidate = CandidateRequest & {
  id: number
}

export type CandidateListResponse = ApiResponse<Candidate[]>

export type CandidateDetailResponse = ApiResponse<Candidate>

export type CreateCandidateRequest = CandidateRequest

export type UpdateCandidateRequest = CandidateRequest

export type CreateCandidateResponse = ApiResponse<Candidate>

export type UpdateCandidateResponse = ApiResponse<Candidate>
