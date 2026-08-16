export function ReadyScreenLayout({
  icon,
  iconClassName = '',
  kicker,
  title,
  description,
  children,
}) {
  const iconClasses = ['ready-icon', iconClassName].filter(Boolean).join(' ')

  return (
    <div className="center-state ready-state">
      <span className={iconClasses} aria-hidden="true">{icon}</span>
      <p className="stage-kicker">{kicker}</p>
      <h2>{title}</h2>
      <p>{description}</p>
      {children}
    </div>
  )
}

export function GuessForm({
  className = '',
  title,
  error,
  isSubmitting,
  submitLabel,
  onSubmit,
  children,
}) {
  const classes = ['guess-state', className].filter(Boolean).join(' ')

  function handleSubmit(event) {
    event.preventDefault()
    onSubmit()
  }

  return (
    <form className={classes} onSubmit={handleSubmit}>
      <p className="stage-kicker">Now it’s your turn</p>
      <h2>{title}</h2>
      {children}
      {error && <p className="form-error">{error}</p>}
      <button type="submit" disabled={isSubmitting}>
        {isSubmitting ? 'Checking…' : submitLabel}
      </button>
    </form>
  )
}
