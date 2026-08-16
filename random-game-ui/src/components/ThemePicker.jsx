const THEMES = [
  { id: 'light', label: 'Light' },
  { id: 'dark', label: 'Dark' },
]

export default function ThemePicker({ theme, onChange }) {
  return (
    <div className="theme-picker">
      <span className="theme-picker-label">Appearance</span>
      <div className="theme-options" role="group" aria-label="Color theme">
        {THEMES.map((option) => (
          <button
            className={`theme-option ${theme === option.id ? 'is-active' : ''}`}
            type="button"
            aria-pressed={theme === option.id}
            onClick={() => onChange(option.id)}
            key={option.id}
          >
            {option.label}
          </button>
        ))}
      </div>
    </div>
  )
}
