export default function CountdownBar({
  secondsLeft,
  durationSeconds,
  isPaused,
  onTogglePause,
}) {
  return (
    <div className="countdown-row">
      <div className="progress-track">
        <div
          className={`progress-fill ${isPaused ? 'is-paused' : ''}`}
          style={{ animationDuration: `${durationSeconds}s` }}
        />
      </div>
      <span className="countdown-time">{secondsLeft}s</span>
      <button
        className="countdown-toggle"
        type="button"
        aria-pressed={isPaused}
        onClick={onTogglePause}
      >
        {isPaused ? 'Resume' : 'Pause'}
      </button>
    </div>
  )
}
