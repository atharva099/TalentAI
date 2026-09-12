import { BrandMark } from '../components/BrandMark'

export function HomePage() {
  return (
    <section className="welcome-card" aria-labelledby="welcome-title">
      <BrandMark compact />
      <p className="welcome-card__eyebrow">Frontend foundation</p>
      <h1 id="welcome-title">TalentAI is ready for integration.</h1>
      <p className="welcome-card__description">
        The React and TypeScript frontend is configured and ready for the next
        application modules.
      </p>
      <div className="welcome-card__details">
        <span>React + TypeScript</span>
        <span>Vite</span>
        <span>API base: /api/v1</span>
      </div>
    </section>
  )
}
