import { useState, type FormEvent } from 'react'
import type {
  Candidate,
  CandidateRequest,
} from '../../types/candidate'

type CandidateFormValues = {
  firstName: string
  lastName: string
  email: string
  phone: string
  skills: string
  experienceYears: string
}

type CandidateFormErrors = Partial<Record<keyof CandidateFormValues, string>>

export type CandidateFormProps = {
  mode: 'create' | 'edit'
  initialCandidate?: Partial<Candidate>
  onSubmit: (request: CandidateRequest) => Promise<void> | void
  isSubmitting: boolean
  onCancel: () => void
  serverError?: string
}

function getInitialValues(candidate?: Partial<Candidate>): CandidateFormValues {
  return {
    firstName: candidate?.firstName ?? '',
    lastName: candidate?.lastName ?? '',
    email: candidate?.email ?? '',
    phone: candidate?.phone ?? '',
    skills: candidate?.skills ?? '',
    experienceYears: candidate?.experienceYears === undefined
      || candidate?.experienceYears === null
      ? ''
      : String(candidate.experienceYears),
  }
}

function validateForm(values: CandidateFormValues): CandidateFormErrors {
  const errors: CandidateFormErrors = {}
  const firstName = values.firstName.trim()
  const lastName = values.lastName.trim()
  const email = values.email.trim()
  const phone = values.phone.trim()
  const skills = values.skills.trim()
  const experienceYears = values.experienceYears.trim()

  if (!firstName) {
    errors.firstName = 'First name is required.'
  } else if (firstName.length > 100) {
    errors.firstName = 'First name must not exceed 100 characters.'
  }

  if (!lastName) {
    errors.lastName = 'Last name is required.'
  } else if (lastName.length > 100) {
    errors.lastName = 'Last name must not exceed 100 characters.'
  }

  if (!email) {
    errors.email = 'Email is required.'
  } else if (email.length > 255) {
    errors.email = 'Email must not exceed 255 characters.'
  } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
    errors.email = 'Email must be a valid email address.'
  }

  if (phone.length > 30) {
    errors.phone = 'Phone must not exceed 30 characters.'
  }

  if (skills.length > 2000) {
    errors.skills = 'Skills must not exceed 2000 characters.'
  }

  if (experienceYears) {
    const parsedExperienceYears = Number(experienceYears)

    if (!Number.isInteger(parsedExperienceYears)
      || parsedExperienceYears < 0
      || parsedExperienceYears > 100) {
      errors.experienceYears = 'Experience years must be a whole number from 0 to 100.'
    }
  }

  return errors
}

function toRequest(values: CandidateFormValues): CandidateRequest {
  const request: CandidateRequest = {
    firstName: values.firstName.trim(),
    lastName: values.lastName.trim(),
    email: values.email.trim(),
  }
  const phone = values.phone.trim()
  const skills = values.skills.trim()
  const experienceYears = values.experienceYears.trim()

  if (phone) {
    request.phone = phone
  }

  if (skills) {
    request.skills = skills
  }

  if (experienceYears) {
    request.experienceYears = Number(experienceYears)
  }

  return request
}

export function CandidateForm({
  mode,
  initialCandidate,
  onSubmit,
  isSubmitting,
  onCancel,
  serverError,
}: CandidateFormProps) {
  const [values, setValues] = useState(() => getInitialValues(initialCandidate))
  const [errors, setErrors] = useState<CandidateFormErrors>({})

  function updateField(field: keyof CandidateFormValues, value: string) {
    setValues((current) => ({ ...current, [field]: value }))
    setErrors((current) => ({ ...current, [field]: undefined }))
  }

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()
    const validationErrors = validateForm(values)

    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors)
      return
    }

    await onSubmit(toRequest(values))
  }

  const submitLabel = mode === 'create' ? 'Create candidate' : 'Save changes'

  return (
    <form className="login-form" onSubmit={(event) => void handleSubmit(event)} noValidate>
      {serverError && (
        <div className="form-alert" role="alert">
          {serverError}
        </div>
      )}

      <div className="form-field">
        <label htmlFor="candidate-first-name">First Name</label>
        <input
          id="candidate-first-name"
          name="firstName"
          type="text"
          maxLength={100}
          value={values.firstName}
          onChange={(event) => updateField('firstName', event.target.value)}
          aria-describedby={errors.firstName ? 'candidate-first-name-error' : undefined}
          aria-invalid={Boolean(errors.firstName)}
          disabled={isSubmitting}
        />
        {errors.firstName && (
          <span className="field-error" id="candidate-first-name-error">
            {errors.firstName}
          </span>
        )}
      </div>

      <div className="form-field">
        <label htmlFor="candidate-last-name">Last Name</label>
        <input
          id="candidate-last-name"
          name="lastName"
          type="text"
          maxLength={100}
          value={values.lastName}
          onChange={(event) => updateField('lastName', event.target.value)}
          aria-describedby={errors.lastName ? 'candidate-last-name-error' : undefined}
          aria-invalid={Boolean(errors.lastName)}
          disabled={isSubmitting}
        />
        {errors.lastName && (
          <span className="field-error" id="candidate-last-name-error">
            {errors.lastName}
          </span>
        )}
      </div>

      <div className="form-field">
        <label htmlFor="candidate-email">Email</label>
        <input
          id="candidate-email"
          name="email"
          type="email"
          maxLength={255}
          value={values.email}
          onChange={(event) => updateField('email', event.target.value)}
          aria-describedby={errors.email ? 'candidate-email-error' : undefined}
          aria-invalid={Boolean(errors.email)}
          disabled={isSubmitting}
        />
        {errors.email && (
          <span className="field-error" id="candidate-email-error">
            {errors.email}
          </span>
        )}
      </div>

      <div className="form-field">
        <label htmlFor="candidate-phone">Phone</label>
        <input
          id="candidate-phone"
          name="phone"
          type="text"
          maxLength={30}
          value={values.phone}
          onChange={(event) => updateField('phone', event.target.value)}
          aria-describedby={errors.phone ? 'candidate-phone-error' : undefined}
          aria-invalid={Boolean(errors.phone)}
          disabled={isSubmitting}
        />
        {errors.phone && (
          <span className="field-error" id="candidate-phone-error">
            {errors.phone}
          </span>
        )}
      </div>

      <div className="form-field">
        <label htmlFor="candidate-skills">Skills</label>
        <textarea
          id="candidate-skills"
          name="skills"
          maxLength={2000}
          value={values.skills}
          onChange={(event) => updateField('skills', event.target.value)}
          aria-describedby={errors.skills ? 'candidate-skills-error' : undefined}
          aria-invalid={Boolean(errors.skills)}
          disabled={isSubmitting}
        />
        {errors.skills && (
          <span className="field-error" id="candidate-skills-error">
            {errors.skills}
          </span>
        )}
      </div>

      <div className="form-field">
        <label htmlFor="candidate-experience-years">Experience Years</label>
        <input
          id="candidate-experience-years"
          name="experienceYears"
          type="number"
          min={0}
          max={100}
          step={1}
          value={values.experienceYears}
          onChange={(event) => updateField('experienceYears', event.target.value)}
          aria-describedby={
            errors.experienceYears ? 'candidate-experience-years-error' : undefined
          }
          aria-invalid={Boolean(errors.experienceYears)}
          disabled={isSubmitting}
        />
        {errors.experienceYears && (
          <span className="field-error" id="candidate-experience-years-error">
            {errors.experienceYears}
          </span>
        )}
      </div>

      <div className="candidate-form-actions">
        <button className="submit-button" type="submit" disabled={isSubmitting}>
          {isSubmitting ? 'Saving...' : submitLabel}
        </button>
        <button
          className="candidate-cancel-button"
          type="button"
          onClick={onCancel}
          disabled={isSubmitting}
        >
          Cancel
        </button>
      </div>
    </form>
  )
}
