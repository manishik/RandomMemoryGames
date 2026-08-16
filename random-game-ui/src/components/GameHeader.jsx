const STOPPABLE_PHASES = new Set(['loading', 'showing', 'guessing', 'submitting', 'result'])

export default function GameHeader({ mark, title, phase, onBack, onStop }) {
  const canStop = STOPPABLE_PHASES.has(phase)

  return (
    <div className="game-header">
      <header className="brand">
        <span className="brand-mark">{mark}</span>
        <div>
          <p className="eyebrow">Memory training</p>
          <h1>{title}</h1>
        </div>
      </header>

      <div className="header-actions">
        <button className="back-button" type="button" onClick={onBack}>
          All games
        </button>
        {canStop && (
          <button className="stop-button" type="button" onClick={onStop}>
            Stop game
          </button>
        )}
      </div>
    </div>
  )
}
