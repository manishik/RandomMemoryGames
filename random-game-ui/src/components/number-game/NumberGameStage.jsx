import { useState } from 'react'
import CountdownBar from '../CountdownBar.jsx'
import DurationPicker from '../DurationPicker.jsx'
import { GuessForm, ReadyScreenLayout } from '../GameStageLayouts.jsx'
import {
  ErrorScreen,
  LoadingScreen,
  ResultScreenLayout,
  StoppedScreenLayout,
} from '../GameStatusScreens.jsx'
import { formatQuantity } from '../../utils/formatQuantity.js'
import { isIntegerInRange } from '../../utils/numberValidation.js'
import { preventCopy } from '../../utils/preventCopy.js'

function getNumberFontSize(digitCount, scale = 1) {
  const minimumFontSize = 1.5 * scale
  const preferredFontSize = (115 / digitCount) * scale
  const maximumFontSize = Math.min(6.6, 50 / digitCount) * scale

  return `clamp(${minimumFontSize}rem, ${preferredFontSize}vw, ${maximumFontSize}rem)`
}

function getFittingNumberFontSize(baseDigitCount, contentDigitCount = baseDigitCount, reservedWidth = 8) {
  const displayFontSize = getNumberFontSize(baseDigitCount)
  const estimatedTextWidth = Math.max(1, contentDigitCount) * 0.68
  const fittingFontSize = `calc((100cqw - ${reservedWidth}px) / ${estimatedTextWidth})`

  return `min(${displayFontSize}, ${fittingFontSize})`
}

function getComparisonFontSize(digitCount) {
  const estimatedTextWidth = Math.max(1, digitCount) * 0.68

  return `min(1.05rem, calc((100cqw - 8px) / ${estimatedTextWidth}))`
}

function ReadyScreen({ onStart }) {
  const [startingDigitChoice, setStartingDigitChoice] = useState('1')
  const [customStartingDigits, setCustomStartingDigits] = useState('')
  const [startingDigitError, setStartingDigitError] = useState('')

  function handleStart(seconds) {
    const startingDigits = startingDigitChoice === 'custom'
      ? Number(customStartingDigits)
      : Number(startingDigitChoice)

    if (!isIntegerInRange(startingDigits, 1, 1000)) {
      setStartingDigitError('Enter a whole number between 1 and 1,000 digits.')
      return
    }

    onStart(seconds, startingDigits)
  }

  return (
    <ReadyScreenLayout
      icon="▶"
      kicker="Memory challenge"
      title="Ready to test your memory?"
      description="Choose where to begin. Every correct guess adds another digit."
    >
      <DurationPicker onStart={handleStart}>
        <div className="duration-field">
          <label htmlFor="starting-digit-choice">How many digits should the first challenge have?</label>
          <select
            id="starting-digit-choice"
            value={startingDigitChoice}
            onChange={(event) => {
              setStartingDigitChoice(event.target.value)
              setStartingDigitError('')
            }}
          >
            <option value="1">1 digit</option>
            <option value="5">5 digits</option>
            <option value="10">10 digits</option>
            <option value="custom">Custom digits</option>
          </select>
        </div>

        {startingDigitChoice === 'custom' && (
          <div className="duration-field">
            <label htmlFor="custom-starting-digits">Custom starting digits</label>
            <input
              id="custom-starting-digits"
              type="number"
              inputMode="numeric"
              min="1"
              max="1000"
              step="1"
              value={customStartingDigits}
              onChange={(event) => {
                setCustomStartingDigits(event.target.value)
                setStartingDigitError('')
              }}
              placeholder="For example, 7"
            />
          </div>
        )}

        {startingDigitError && <p className="form-error">{startingDigitError}</p>}
      </DurationPicker>
    </ReadyScreenLayout>
  )
}

function NumberScreen({
  round,
  secondsLeft,
  memorizeSeconds,
  isCountdownPaused,
  onToggleCountdownPause,
}) {
  const numberFontSize = getFittingNumberFontSize(round.digitCount)

  return (
    <div className="number-state">
      <p className="stage-kicker">Memorize this number</p>
      <div
        className="number-display copy-protected"
        style={{ fontSize: numberFontSize }}
        onCopy={preventCopy}
      >
        {round.number}
      </div>
      <CountdownBar
        secondsLeft={secondsLeft}
        durationSeconds={memorizeSeconds}
        isPaused={isCountdownPaused}
        onTogglePause={onToggleCountdownPause}
      />
    </div>
  )
}

function GuessScreen({ round, guess, error, isSubmitting, onGuessChange, onSubmit }) {
  const answerDigitCount = Math.max(1, round.digitCount, guess.length)
  const answerFontSize = getFittingNumberFontSize(round.digitCount, answerDigitCount, 42)
  const enteredDigitCount = guess.replace(/\D/g, '').length
  const filledDotCount = Math.min(enteredDigitCount, round.digitCount)
  const remainingDotCount = round.digitCount - filledDotCount
  const dotFontSize = `min(2.15rem, ${135 / round.digitCount}cqw)`

  return (
    <GuessForm
      className="number-guess-state"
      title="What was the number?"
      error={error}
      isSubmitting={isSubmitting}
      submitLabel="Check answer"
      onSubmit={onSubmit}
    >
      <div className="hidden-number" aria-hidden="true">
        <div className="digit-dot-row" style={{ fontSize: dotFontSize }}>
          <span className="digit-dots is-filled">{'•'.repeat(filledDotCount)}</span>
          <span className="digit-dots">{'•'.repeat(remainingDotCount)}</span>
        </div>
        {round.digitCount > 12 && (
          <span className="digit-progress-count">
            {Math.min(enteredDigitCount, round.digitCount)} / {round.digitCount} entered
          </span>
        )}
      </div>
      <label htmlFor="guess">Your answer</label>
      <input
        id="guess"
        className="number-guess-input"
        type="text"
        inputMode="numeric"
        autoComplete="off"
        autoFocus
        value={guess}
        style={{ fontSize: answerFontSize }}
        onChange={(event) => onGuessChange(event.target.value.trim())}
        placeholder="Type the number"
        disabled={isSubmitting}
      />
    </GuessForm>
  )
}

function ResultScreen({ result, onNextRound }) {
  const nextLevel = formatQuantity(result.nextDigitCount, 'digit')
  const userAnswerLength = result.userGuess.length
  const correctAnswerLength = result.correctNumber.length
  const hasLongAnswers = Math.max(userAnswerLength, correctAnswerLength) > 20

  return (
    <ResultScreenLayout
      correct={result.correct}
      title={result.correct ? 'That’s correct!' : 'That wasn’t it.'}
      nextLevel={nextLevel}
      onNextRound={() => onNextRound(result.nextDigitCount)}
    >
      {!result.correct && (
        <div
          className={`number-answer-comparison${hasLongAnswers ? ' is-long' : ''}`}
          aria-label="Answer comparison"
        >
          <div className="comparison-value user-answer-value">
            <span>Your answer</span>
            <strong
              className="copy-protected"
              style={{ fontSize: getComparisonFontSize(userAnswerLength) }}
              onCopy={preventCopy}
            >
              {result.userGuess}
            </strong>
          </div>
          <div className="comparison-value correct-answer-value">
            <span>Correct answer</span>
            <strong
              className="copy-protected"
              style={{ fontSize: getComparisonFontSize(correctAnswerLength) }}
              onCopy={preventCopy}
            >
              {result.correctNumber}
            </strong>
          </div>
        </div>
      )}
    </ResultScreenLayout>
  )
}

function StoppedScreen({
  currentDigitCount,
  onRestartCurrentLevel,
  onChangeSettings,
}) {
  const currentLevel = formatQuantity(currentDigitCount, 'digit')

  return (
    <StoppedScreenLayout
      description={`Continue from ${currentLevel}, or choose new digits and memorization time.`}
    >
      <button type="button" onClick={onRestartCurrentLevel}>
        Restart from {currentLevel}
      </button>
      <button
        className="secondary-button"
        type="button"
        onClick={onChangeSettings}
      >
        Change digits &amp; seconds
      </button>
    </StoppedScreenLayout>
  )
}

export default function NumberGameStage({ game }) {
  const {
    phase,
    round,
    memorizeSeconds,
    secondsLeft,
    guess,
    setGuess,
    result,
    error,
    currentDigitCount,
    isCountdownPaused,
    startGame,
    startRound,
    submitGuess,
    toggleCountdownPause,
    restartCurrentLevel,
    returnToSetup,
    retry,
  } = game

  return (
    <div className="game-stage">
      {phase === 'ready' && <ReadyScreen onStart={startGame} />}
      {phase === 'loading' && <LoadingScreen title="Preparing your number" />}
      {phase === 'showing' && round && (
        <NumberScreen
          round={round}
          secondsLeft={secondsLeft}
          memorizeSeconds={memorizeSeconds}
          isCountdownPaused={isCountdownPaused}
          onToggleCountdownPause={toggleCountdownPause}
        />
      )}
      {(phase === 'guessing' || phase === 'submitting') && round && (
        <GuessScreen
          round={round}
          guess={guess}
          error={error}
          isSubmitting={phase === 'submitting'}
          onGuessChange={setGuess}
          onSubmit={submitGuess}
        />
      )}
      {phase === 'result' && result && (
        <ResultScreen result={result} onNextRound={startRound} />
      )}
      {phase === 'stopped' && (
        <StoppedScreen
          currentDigitCount={currentDigitCount}
          onRestartCurrentLevel={restartCurrentLevel}
          onChangeSettings={returnToSetup}
        />
      )}
      {phase === 'error' && <ErrorScreen error={error} onRetry={retry} />}
    </div>
  )
}
