import ThemePicker from './ThemePicker.jsx'

const GAMES = [
  {
    id: 'number',
    cardClassName: 'number-choice',
    icon: '123',
    kicker: 'Digits and sequences',
    title: 'Random Number Game',
    description: 'Memorize increasingly long random numbers.',
    linkText: 'Choose number game →',
  },
  {
    id: 'name',
    cardClassName: 'name-choice',
    icon: 'Aa',
    kicker: 'People and names',
    title: 'Random Name Game',
    description: 'Memorize first names, last names, or full names.',
    linkText: 'Choose name game →',
  },
]

export default function GamePicker({ theme, onThemeChange, onSelect }) {
  return (
    <section className="game-layout picker-layout">
      <header className="picker-header">
        <p className="eyebrow">Memory training</p>
        <h1>Choose a memory game</h1>
        <p className="picker-description">Practice recalling numbers or names at your own pace.</p>
        <ThemePicker theme={theme} onChange={onThemeChange} />
      </header>

      <div className="game-choice-grid">
        {GAMES.map((game) => (
          <button
            className={`game-choice-card ${game.cardClassName}`}
            type="button"
            onClick={() => onSelect(game.id)}
            key={game.id}
          >
            <span className="choice-icon" aria-hidden="true">{game.icon}</span>
            <span className="choice-kicker">{game.kicker}</span>
            <strong>{game.title}</strong>
            <span className="choice-description">{game.description}</span>
            <span className="choice-link">{game.linkText}</span>
          </button>
        ))}
      </div>
    </section>
  )
}
