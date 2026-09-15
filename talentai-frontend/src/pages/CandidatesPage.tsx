import { useCallback, useEffect, useRef, useState } from 'react'
import { Link } from 'react-router-dom'
import {
  deleteCandidate,
  getCandidates,
} from '../services/api/candidateService'
import type { Candidate } from '../types/candidate'

function displayOptionalValue(value: string | null | undefined): string {
  return value?.trim() || 'Not provided'
}

type CandidateCardProps = {
  candidate: Candidate
  isDeleting: boolean
  onDelete: (candidate: Candidate) => void
}

function CandidateCard({
  candidate,
  isDeleting,
  onDelete,
}: CandidateCardProps) {
  const fullName = `${candidate.firstName} ${candidate.lastName}`

  return (
    <article className="dashboard-summary-card">
      <h2>{fullName}</h2>
      <p>{candidate.email}</p>
      <p>Phone: {displayOptionalValue(candidate.phone)}</p>
      <p>Skills: {displayOptionalValue(candidate.skills)}</p>
      <p>
        Experience: {candidate.experienceYears === null
          || candidate.experienceYears === undefined
          ? 'Not provided'
          : `${candidate.experienceYears} years`}
      </p>
      <div className="candidate-card__actions" aria-label={`Actions for ${fullName}`}>
        <Link className="candidate-action candidate-action--secondary" to={`/candidates/${candidate.id}`}>
          View
        </Link>
        <Link className="candidate-action candidate-action--secondary" to={`/candidates/${candidate.id}/edit`}>
          Edit
        </Link>
        <button
          className="candidate-action candidate-action--danger"
          type="button"
          onClick={() => onDelete(candidate)}
          disabled={isDeleting}
        >
          {isDeleting ? 'Deleting...' : 'Delete'}
        </button>
      </div>
    </article>
  )
}

export function CandidatesPage() {
  const [candidates, setCandidates] = useState<Candidate[] | null>(null)
  const [isLoading, setIsLoading] = useState(true)
  const [hasError, setHasError] = useState(false)
  const [deletingCandidateIds, setDeletingCandidateIds] = useState<Set<number>>(
    () => new Set(),
  )
  const [deleteError, setDeleteError] = useState('')
  const isMounted = useRef(true)

  const loadCandidates = useCallback(async () => {
    if (!isMounted.current) {
      return
    }

    setIsLoading(true)
    setHasError(false)

    try {
      const response = await getCandidates()

      if (isMounted.current) {
        setCandidates(response.data)
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
  }, [])

  useEffect(() => {
    isMounted.current = true

    getCandidates()
      .then((response) => {
        if (isMounted.current) {
          setCandidates(response.data)
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
  }, [loadCandidates])

  const handleDelete = useCallback(async (candidate: Candidate) => {
    if (deletingCandidateIds.has(candidate.id) || !isMounted.current) {
      return
    }

    const fullName = `${candidate.firstName} ${candidate.lastName}`
    const shouldDelete = window.confirm(
      `Are you sure you want to delete ${fullName}?`,
    )

    if (!shouldDelete) {
      return
    }

    setDeletingCandidateIds((current) => {
      const next = new Set(current)
      next.add(candidate.id)
      return next
    })
    setDeleteError('')

    try {
      await deleteCandidate(candidate.id)

      if (isMounted.current) {
        setCandidates((current) => (
          current?.filter((currentCandidate) => currentCandidate.id !== candidate.id)
          ?? current
        ))
      }
    } catch {
      if (isMounted.current) {
        setDeleteError(`We could not delete ${fullName}. Please try again.`)
      }
    } finally {
      if (isMounted.current) {
        setDeletingCandidateIds((current) => {
          const next = new Set(current)
          next.delete(candidate.id)
          return next
        })
      }
    }
  }, [deletingCandidateIds])

  if (isLoading) {
    return (
      <section className="dashboard-state" aria-live="polite">
        Loading candidates...
      </section>
    )
  }

  if (hasError || candidates === null) {
    return (
      <section className="dashboard-state dashboard-state--error" role="alert">
        <h1>Unable to load candidates</h1>
        <p>We could not retrieve the candidate list.</p>
        <button type="button" onClick={() => void loadCandidates()}>
          Retry
        </button>
      </section>
    )
  }

  return (
    <section className="dashboard-page" aria-labelledby="candidates-title">
      <div className="dashboard-page__intro candidates-page__intro">
        <p className="dashboard-page__eyebrow">Candidate management</p>
        <h1 id="candidates-title">Candidates</h1>
        <p>Review the candidates in your TalentAI workspace.</p>
        <Link className="submit-button candidates-page__add-button" to="/candidates/new">
          Add Candidate
        </Link>
      </div>

      {candidates.length === 0 ? (
        <section className="dashboard-state" aria-live="polite">
          <h1>No candidates yet</h1>
          <p>There are no candidates in your workspace yet.</p>
        </section>
      ) : (
        <>
          {deleteError && (
            <div className="form-alert" role="alert">
              {deleteError}
            </div>
          )}
          <div className="dashboard-summary-grid">
          {candidates.map((candidate) => (
              <CandidateCard
                key={candidate.id}
                candidate={candidate}
                isDeleting={deletingCandidateIds.has(candidate.id)}
                onDelete={(candidateToDelete) => void handleDelete(candidateToDelete)}
              />
            ))}
          </div>
        </>
      )}
    </section>
  )
}
