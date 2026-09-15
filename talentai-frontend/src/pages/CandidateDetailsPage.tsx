import { useCallback, useEffect, useRef, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import { getCandidate } from '../services/api/candidateService'
import type { Candidate } from '../types/candidate'

function parseCandidateId(value: string | undefined): number | null {
  if (!value || !/^[1-9]\d*$/.test(value)) {
    return null
  }

  const candidateId = Number(value)
  return Number.isSafeInteger(candidateId) ? candidateId : null
}

function displayOptionalValue(value: string | null | undefined): string {
  return value?.trim() || 'Not provided'
}

function CandidateDetails({ candidate }: { candidate: Candidate }) {
  const fullName = `${candidate.firstName} ${candidate.lastName}`
  const experience = candidate.experienceYears === null
    || candidate.experienceYears === undefined
    ? 'Not provided'
    : `${candidate.experienceYears} years`

  return (
    <section className="candidate-details-card" aria-labelledby="candidate-name">
      <h2 className="candidate-details-card__name" id="candidate-name">{fullName}</h2>
      <dl className="candidate-details-grid">
        <div className="candidate-detail-item">
          <dt className="candidate-detail-label">First name</dt>
          <dd className="candidate-detail-value">{candidate.firstName}</dd>
        </div>
        <div className="candidate-detail-item">
          <dt className="candidate-detail-label">Last name</dt>
          <dd className="candidate-detail-value">{candidate.lastName}</dd>
        </div>
        <div className="candidate-detail-item">
          <dt className="candidate-detail-label">Full name</dt>
          <dd className="candidate-detail-value">{fullName}</dd>
        </div>
        <div className="candidate-detail-item">
          <dt className="candidate-detail-label">Email</dt>
          <dd className="candidate-detail-value">{candidate.email}</dd>
        </div>
        <div className="candidate-detail-item">
          <dt className="candidate-detail-label">Phone</dt>
          <dd className="candidate-detail-value">{displayOptionalValue(candidate.phone)}</dd>
        </div>
        <div className="candidate-detail-item candidate-detail-item--wide">
          <dt className="candidate-detail-label">Skills</dt>
          <dd className="candidate-detail-value">{displayOptionalValue(candidate.skills)}</dd>
        </div>
        <div className="candidate-detail-item">
          <dt className="candidate-detail-label">Experience years</dt>
          <dd className="candidate-detail-value">{experience}</dd>
        </div>
      </dl>
    </section>
  )
}

export function CandidateDetailsPage() {
  const { candidateId: candidateIdParam } = useParams<{ candidateId: string }>()
  const candidateId = parseCandidateId(candidateIdParam)
  const [candidate, setCandidate] = useState<Candidate | null>(null)
  const [isLoading, setIsLoading] = useState(true)
  const [hasError, setHasError] = useState(false)
  const isMounted = useRef(true)

  const loadCandidate = useCallback(async () => {
    if (candidateId === null || !isMounted.current) {
      return
    }

    setIsLoading(true)
    setHasError(false)

    try {
      const response = await getCandidate(candidateId)

      if (isMounted.current) {
        setCandidate(response.data)
      }
    } catch {
      if (isMounted.current) {
        setHasError(true)
      }
    } finally {
      if (isMounted.current) {
        setIsLoading(false)
      }
    }
  }, [candidateId])

  useEffect(() => {
    isMounted.current = true

    if (candidateId === null) {
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
          setHasError(true)
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
  }, [candidateId])

  if (candidateId === null) {
    return (
      <section className="dashboard-state dashboard-state--error" role="alert">
        <h1>Invalid candidate</h1>
        <p>Please select a valid candidate to view their details.</p>
        <Link className="candidate-back-link" to="/candidates">
          Back to Candidates
        </Link>
      </section>
    )
  }

  if (isLoading) {
    return (
      <section className="dashboard-state" aria-live="polite">
        Loading candidate details...
      </section>
    )
  }

  if (hasError || candidate === null) {
    return (
      <section className="dashboard-state dashboard-state--error" role="alert">
        <h1>Unable to load candidate</h1>
        <p>We could not retrieve this candidate&apos;s details.</p>
        <button type="button" onClick={() => void loadCandidate()}>
          Retry
        </button>
        <p>
          <Link className="candidate-back-link" to="/candidates">
            Back to Candidates
          </Link>
        </p>
      </section>
    )
  }

  return (
    <section className="dashboard-page" aria-labelledby="candidate-details-title">
      <div className="dashboard-page__intro">
        <p className="dashboard-page__eyebrow">Candidate management</p>
        <h1 id="candidate-details-title">Candidate Details</h1>
        <p>Review the candidate information in your TalentAI workspace.</p>
      </div>
      <CandidateDetails candidate={candidate} />
      <p>
        <Link className="candidate-back-link" to="/candidates">
          Back to Candidates
        </Link>
      </p>
    </section>
  )
}
