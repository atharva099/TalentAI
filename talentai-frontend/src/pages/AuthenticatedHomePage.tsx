import { useAuth } from '../context/useAuth'

export function AuthenticatedHomePage() {
  const { currentUser } = useAuth()

  return (
    <section className="welcome-card" aria-labelledby="authenticated-title">
      <p className="welcome-card__eyebrow">Authenticated application</p>
      <h1 id="authenticated-title">
        Welcome, {currentUser?.firstName ?? 'back'}.
      </h1>
      <p className="welcome-card__description">
        Your TalentAI session is active for {currentUser?.email}.
      </p>
      <div className="welcome-card__details">
        {currentUser?.roles.map((role) => (
          <span key={role}>{role}</span>
        ))}
      </div>
    </section>
  )
}
