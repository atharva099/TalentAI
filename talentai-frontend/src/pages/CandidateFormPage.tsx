import { useCallback, useEffect, useRef, useState } from 'react'
import { Link, useNavigate, useParams } from 'react-router-dom'
import { CandidateForm } from '../components/candidates/CandidateForm'
import {
  createCandidate,
  getCandidate,
  updateCandidate,
} from '../services/api/candidateService'
import { ApiClientError } from '../services/api/client'
import type { Candidate, CandidateRequest } from '../types/candidate'

function parseCandidateId(value: string | undefined): number | null {
  if (!value || !/^[1-9]\d*$/.test(value)) {
    return null
  }

  const candidateId = Number(value)
  return Number.isSafeInteger(candidateId) ? candidateId : null
}

function getErrorMessage(error: unknown, fallback: string): string {
  if (error instanceof ApiClientError) {
    const validationErrors = error.response.validationErrors
    const firstValidationError = validationErrors
      ? Object.values(validationErrors).flat()[0]
      : undefined

    return firstValidationError ?? error.response.message
  }

  if (error instanceof TypeError) {
    return 'We could not reach TalentAI. Check your connection and try again.'
  }

  return fallback
}

export function CandidateFormPage() {
  const { candidateId: candidateIdParam } = useParams<{ candidateId: string }>()
  const candidateId = parseCandidateId(candidateIdParam)
  const isEditMode = candidateId !== null
  const navigate = useNavigate()
  const [candidate, setCandidate] = useState<Candidate | null>(null)
  const [isLoading, setIsLoading] = useState(isEditMode)
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [loadError, setLoadError] = useState(false)
  const [serverError, setServerError] = useState('')
  const isMounted = useRef(true)

  const loadCandidate = useCallback(async () => {
    if (candidateId === null || !isMounted.current) {
      return
    }

    setIsLoading(true)
    setLoadError(false)

    try {
      const response = await getCandidate(candidateId)

      if (isMounted.current) {
        setCandidate(response.data)
      }
    } catch {
      if (isMounted.current) {
        setLoadError(true)
      }
    } finally {
      if (isMounted.current) {
        setIsLoading(false)
      }
    }
  }, [candidateId])

  useEffect(() => {
    isMounted.current = true

    if (!isEditMode || candidateId === null) {
      return () => {
        isMounted.current = false
      }
    }

    getCandidate(candidateId)
      .then((response) => {
        if (isMounted.current) {
          setCandidate(response.data)
        }
      })
      .catch(() => {
        if (isMounted.current) {
          setLoadError(true)
        }
      })
      .finally(() => {
        if (isMounted.current) {
          setIsLoading(false)
        }
      })

    return () => {
      isMounted.current = false
    }
  }, [candidateId, isEditMode])

  const handleSubmit = useCallback(async (request: CandidateRequest) => {
    if (isSubmitting || !isMounted.current) {
      return
    }

    setIsSubmitting(true)
    setServerError('')

    try {
      if (isEditMode && candidateId !== null) {
        await updateCandidate(candidateId, request)
        navigate(`/candidates/${candidateId}`)
      } else {
        await createCandidate(request)
        navigate('/candidates')
      }
    } catch (error: unknown) {
      if (isMounted.current) {
        setServerError(getErrorMessage(
          error,
          isEditMode
            ? 'We could not update this candidate. Please try again.'
            : 'We could not create this candidate. Please try again.',
        ))
      }
    } finally {
      if (isMounted.current) {
        setIsSubmitting(false)
      }
    }
  }, [candidateId, isEditMode, isSubmitting, navigate])

  if (candidateIdParam !== undefined && candidateId === null) {
    return (
      <section className="dashboard-state dashboard-state--error" role="alert">
        <h1>Invalid candidate</h1>
        <p>Please select a valid candidate to edit.</p>
        <Link to="/candidates">Back to Candidates</Link>
      </section>
    )
  }

  if (isEditMode && isLoading) {
    return (
      <section className="dashboard-state" aria-live="polite">
        Loading candidate...
      </section>
    )
  }

  if (isEditMode && (loadError || candidate === null)) {
    return (
      <section className="dashboard-state dashboard-state--error" role="alert">
        <h1>Unable to load candidate</h1>
        <p>We could not retrieve this candidate for editing.</p>
        <button type="button" onClick={() => void loadCandidate()}>
          Retry
        </button>
        <p>
          <Link to="/candidates">Back to Candidates</Link>
        </p>
      </section>
    )
  }

  return (
    <section className="dashboard-page" aria-labelledby="candidate-form-title">
      <div className="dashboard-page__intro">
        <p className="dashboard-page__eyebrow">Candidate management</p>
        <h1 id="candidate-form-title">
          {isEditMode ? 'Edit Candidate' : 'Create Candidate'}
        </h1>
        <p>
          {isEditMode
            ? 'Update candidate information in your TalentAI workspace.'
            : 'Add a candidate to your TalentAI workspace.'}
        </p>
      </div>

      <CandidateForm
        mode={isEditMode ? 'edit' : 'create'}
        initialCandidate={candidate ?? undefined}
        onSubmit={handleSubmit}
        isSubmitting={isSubmitting}
        onCancel={() => navigate('/candidates')}
        serverError={serverError || undefined}
      />
    </section>
  )
}
