import { useCallback, useEffect, useState } from 'react'
import { getDashboardSummary } from '../services/api/dashboardService'
import type { DashboardSummary } from '../types/api'

type SummaryCardProps = {
  label: string
  value: number
}

function SummaryCard({ label, value }: SummaryCardProps) {
  return (
    <article className="dashboard-summary-card">
      <p>{label}</p>
      <strong>{value}</strong>
    </article>
  )
}

export function DashboardPage() {
  const [summary, setSummary] = useState<DashboardSummary | null>(null)
  const [isLoading, setIsLoading] = useState(true)
  const [hasError, setHasError] = useState(false)

  const loadSummary = useCallback(async () => {
    setIsLoading(true)
    setHasError(false)

    try {
      const response = await getDashboardSummary()
      setSummary(response.data)
    } catch {
      setHasError(true)
    } finally {
      setIsLoading(false)
    }
  }, [])

  useEffect(() => {
    let isMounted = true

    getDashboardSummary()
      .then((response) => {
        if (isMounted) {
          setSummary(response.data)
        }
      })
      .catch(() => {
        if (isMounted) {
          setHasError(true)
        }
      })
      .finally(() => {
        if (isMounted) {
          setIsLoading(false)
        }
      })

    return () => {
      isMounted = false
    }
  }, [])

  if (isLoading) {
    return (
      <section className="dashboard-state" aria-live="polite">
        Loading dashboard summary...
      </section>
    )
  }

  if (hasError || summary === null) {
    return (
      <section className="dashboard-state dashboard-state--error" role="alert">
        <h1>Unable to load the dashboard</h1>
        <p>We could not retrieve the latest recruitment summary.</p>
        <button type="button" onClick={() => void loadSummary()}>
          Retry
        </button>
      </section>
    )
  }

  return (
    <section className="dashboard-page" aria-labelledby="dashboard-title">
      <div className="dashboard-page__intro">
        <p className="dashboard-page__eyebrow">Recruitment overview</p>
        <h1 id="dashboard-title">Dashboard</h1>
        <p>Track the key figures across your TalentAI workspace.</p>
      </div>
      <div className="dashboard-summary-grid">
        <SummaryCard label="Candidates" value={summary.candidateCount} />
        <SummaryCard label="Jobs" value={summary.jobCount} />
        <SummaryCard label="Resumes" value={summary.resumeCount} />
      </div>
    </section>
  )
}
