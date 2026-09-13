import { useState, type FormEvent } from 'react'
import { useNavigate } from 'react-router-dom'
import { ApiClientError } from '../services/api/client'
import { useAuth } from '../context/useAuth'
import type { LoginRequest } from '../types/auth'
import { BrandMark } from '../components/BrandMark'

type LoginFormErrors = Partial<Record<keyof LoginRequest, string>>

function validateForm(form: LoginRequest): LoginFormErrors {
  const errors: LoginFormErrors = {}

  if (!form.email.trim()) {
    errors.email = 'Enter your email address.'
  } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email)) {
    errors.email = 'Enter a valid email address.'
  }

  if (!form.password) {
    errors.password = 'Enter your password.'
  }

  return errors
}

function getErrorMessage(error: unknown) {
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

  return 'Something went wrong while signing in. Please try again.'
}

export function LoginPage() {
  const { login } = useAuth()
  const navigate = useNavigate()
  const [form, setForm] = useState<LoginRequest>({ email: '', password: '' })
  const [errors, setErrors] = useState<LoginFormErrors>({})
  const [formError, setFormError] = useState('')
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [showPassword, setShowPassword] = useState(false)

  function updateField(field: keyof LoginRequest, value: string) {
    setForm((current) => ({ ...current, [field]: value }))
    setErrors((current) => ({ ...current, [field]: undefined }))
    setFormError('')
  }

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    const validationErrors = validateForm(form)

    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors)
      return
    }

    setIsSubmitting(true)
    setFormError('')

    try {
      await login({ ...form, email: form.email.trim() })
      navigate('/', { replace: true })
    } catch (error: unknown) {
      setFormError(getErrorMessage(error))
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <main className="auth-page">
      <section className="auth-hero" aria-labelledby="auth-hero-title">
        <div className="auth-hero__content">
          <BrandMark />
          <div className="auth-hero__copy">
            <p className="auth-hero__eyebrow">Recruitment, refined</p>
            <h1 id="auth-hero-title">
              Build stronger teams with clarity and confidence.
            </h1>
            <p>
              TalentAI brings every hiring decision into focus, giving your
              team the insight to find and grow exceptional talent.
            </p>
          </div>
          <div className="auth-hero__signal" aria-hidden="true">
            <span />
            <span />
            <span />
          </div>
        </div>
      </section>

      <section className="auth-panel" aria-labelledby="login-title">
        <div className="auth-card">
          <div className="auth-card__header">
            <p className="auth-card__eyebrow">Your workspace awaits</p>
            <h2 id="login-title">Welcome back</h2>
            <p>Sign in to continue to TalentAI.</p>
          </div>

          <form className="login-form" onSubmit={handleSubmit} noValidate>
            {formError && (
              <div className="form-alert" role="alert">
                {formError}
              </div>
            )}

            <div className="form-field">
              <label htmlFor="email">Email address</label>
              <input
                id="email"
                name="email"
                type="email"
                autoComplete="email"
                placeholder="you@company.com"
                value={form.email}
                onChange={(event) => updateField('email', event.target.value)}
                aria-describedby={errors.email ? 'email-error' : undefined}
                aria-invalid={Boolean(errors.email)}
                disabled={isSubmitting}
              />
              {errors.email && (
                <span className="field-error" id="email-error">
                  {errors.email}
                </span>
              )}
            </div>

            <div className="form-field">
              <div className="form-field__label-row">
                <label htmlFor="password">Password</label>
              </div>
              <div className="password-input">
                <input
                  id="password"
                  name="password"
                  type={showPassword ? 'text' : 'password'}
                  autoComplete="current-password"
                  placeholder="Enter your password"
                  value={form.password}
                  onChange={(event) => updateField('password', event.target.value)}
                  aria-describedby={errors.password ? 'password-error' : undefined}
                  aria-invalid={Boolean(errors.password)}
                  disabled={isSubmitting}
                />
                <button
                  type="button"
                  className="password-toggle"
                  onClick={() => setShowPassword((visible) => !visible)}
                  aria-label={showPassword ? 'Hide password' : 'Show password'}
                  disabled={isSubmitting}
                >
                  {showPassword ? 'Hide' : 'Show'}
                </button>
              </div>
              {errors.password && (
                <span className="field-error" id="password-error">
                  {errors.password}
                </span>
              )}
            </div>

            <button className="submit-button" type="submit" disabled={isSubmitting}>
              {isSubmitting ? (
                <>
                  <span className="loading-spinner" aria-hidden="true" />
                  Signing in...
                </>
              ) : (
                'Sign in'
              )}
            </button>
          </form>

          <p className="auth-card__footer">
            Don&apos;t have an account? <a href="/register">Create one</a>
          </p>
        </div>
      </section>
    </main>
  )
}
