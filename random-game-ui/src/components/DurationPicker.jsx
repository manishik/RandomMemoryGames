import { useState } from 'react'
import { isIntegerInRange } from '../utils/numberValidation.js'

export default function DurationPicker({ children, onStart }) {
  const [durationChoice, setDurationChoice] = useState('3')
  const [customSeconds, setCustomSeconds] = useState('')
  const [durationError, setDurationError] = useState('')

  function handleStart(event) {
    event.preventDefault()

    const seconds = durationChoice === 'custom'
      ? Number(customSeconds)
      : Number(durationChoice)

    if (!isIntegerInRange(seconds, 1, 300)) {
      setDurationError('Enter a whole number between 1 and 300 seconds.')
      return
    }

    onStart(seconds)
  }

  return (
    <form className="start-settings" onSubmit={handleStart}>
      {children}

      <div className="duration-field">
        <label htmlFor="duration-choice">How long should each challenge stay visible?</label>
        <select
          id="duration-choice"
          value={durationChoice}
          onChange={(event) => {
            setDurationChoice(event.target.value)
            setDurationError('')
          }}
        >
          <option value="3">3 seconds</option>
          <option value="6">6 seconds</option>
          <option value="custom">Custom time</option>
        </select>
      </div>

      {durationChoice === 'custom' && (
        <div className="duration-field">
          <label htmlFor="custom-seconds">Custom seconds</label>
          <input
            id="custom-seconds"
            type="number"
            inputMode="numeric"
            min="1"
            max="300"
            step="1"
            value={customSeconds}
            onChange={(event) => {
              setCustomSeconds(event.target.value)
              setDurationError('')
            }}
            placeholder="For example, 10"
            autoFocus
          />
        </div>
      )}

      {durationError && <p className="form-error">{durationError}</p>}
      <button type="submit">Start game</button>
    </form>
  )
}
