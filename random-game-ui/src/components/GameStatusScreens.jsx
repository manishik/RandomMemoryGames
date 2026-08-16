export function LoadingScreen({ title }) {
  return (
    <div className="center-state">
      <span className="loader" aria-hidden="true" />
      <h2>{title}</h2>
    </div>
  )
}

export function ErrorScreen({ error, onRetry }) {
  return (
    <div className="center-state error-state">
      <span className="result-icon" aria-hidden="true">!</span>
      <h2>Couldn’t reach the game server</h2>
      <p>{error}</p>
      <button type="button" onClick={onRetry}>Try again</button>
    </div>
  )
}

export function ResultScreenLayout({
  correct,
  title,
  nextLevel,
  onNextRound,
  children,
}) {
  return (
    <div className={`result-state ${correct ? 'correct' : 'wrong'}`}>
      <span className="result-icon" aria-hidden="true">
        {correct ? '✓' : '×'}
      </span>
      <p className="stage-kicker">{correct ? 'Great recall' : 'Almost there'}</p>
      <h2>{title}</h2>
      {children}
      <p className="next-level">
        Next challenge <strong>{nextLevel}</strong>
      </p>
      <button type="button" onClick={onNextRound}>
        Start next round — {nextLevel}
      </button>
    </div>
  )
}

export function StoppedScreenLayout({ description, children }) {
  return (
    <div className="center-state stopped-state">
      <span className="stopped-icon" aria-hidden="true">Ⅱ</span>
      <p className="stage-kicker">Game stopped</p>
      <h2>Ready when you are</h2>
      <p>{description}</p>
      <div className="restart-actions">{children}</div>
    </div>
  )
}
