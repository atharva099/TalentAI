type BrandMarkProps = {
  compact?: boolean
}

export function BrandMark({ compact = false }: BrandMarkProps) {
  return (
    <span className={`brand-mark${compact ? ' brand-mark--compact' : ''}`}>
      <span className="brand-mark__symbol" aria-hidden="true">
        T
      </span>
      <span>TalentAI</span>
    </span>
  )
}
