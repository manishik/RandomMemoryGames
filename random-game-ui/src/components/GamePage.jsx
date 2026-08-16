import GameHeader from './GameHeader.jsx'

export default function GamePage({
  mark,
  title,
  phase,
  onBack,
  onStop,
  roundMeta,
  children,
  tip,
}) {
  return (
    <section className="game-layout" aria-live="polite">
      <GameHeader
        mark={mark}
        title={title}
        phase={phase}
        onBack={onBack}
        onStop={onStop}
      />

      <div className="game-card">
        {phase !== 'ready' && roundMeta}
        {children}
      </div>

      <p className="game-tip">{tip}</p>
    </section>
  )
}
