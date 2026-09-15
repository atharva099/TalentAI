import type { ApiResponse } from '../../types/api'
import type {
  Candidate,
  CreateCandidateRequest,
  UpdateCandidateRequest,
} from '../../types/candidate'
import {
  del,
  get,
  post,
  put,
} from './client'

const CANDIDATES_PATH = '/candidates'

function candidatePath(candidateId: number): string {
  return `${CANDIDATES_PATH}/${encodeURIComponent(String(candidateId))}`
}

export function getCandidates(): Promise<ApiResponse<Candidate[]>> {
  return get<Candidate[]>(CANDIDATES_PATH)
}

export function getCandidate(
  candidateId: number,
): Promise<ApiResponse<Candidate>> {
  return get<Candidate>(candidatePath(candidateId))
}

export function createCandidate(
  request: CreateCandidateRequest,
): Promise<ApiResponse<Candidate>> {
  return post<CreateCandidateRequest, Candidate>(
    CANDIDATES_PATH,
    request,
  )
}

export function updateCandidate(
  candidateId: number,
  request: UpdateCandidateRequest,
): Promise<ApiResponse<Candidate>> {
  return put<UpdateCandidateRequest, Candidate>(
    candidatePath(candidateId),
    request,
  )
}

export function deleteCandidate(
  candidateId: number,
): Promise<ApiResponse<void>> {
  return del<void>(candidatePath(candidateId))
}
