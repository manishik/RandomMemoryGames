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
import { NAME_MODE_LABELS } from '../../constants/nameModes.js'
import { formatQuantity } from '../../utils/formatQuantity.js'

function normalizeName(name = '') {
  return name.trim().replaceAll(/\s+/g, ' ').toLowerCase()
}

function ReadyScreen({ onStart }) {
  const [nameMode, setNameMode] = useState('FIRST')

  return (
    <ReadyScreenLayout
      icon="Aa"
      iconClassName="name-ready-icon"
      kicker="Name challenge"
      title="What kind of names will you remember?"
      description="The game starts with one name and adds another after each correct answer."
    >
      <DurationPicker onStart={(seconds) => onStart(seconds, nameMode)}>
        <fieldset className="name-mode-fieldset">
          <legend>Name type</legend>
          <div className="name-mode-options">
            <label>
              <input
                type="radio"
                name="name-mode"
                value="FIRST"
                checked={nameMode === 'FIRST'}
                onChange={(event) => setNameMode(event.target.value)}
              />
              <span>First names</span>
            </label>
            <label>
              <input
                type="radio"
                name="name-mode"
                value="LAST"
                checked={nameMode === 'LAST'}
                onChange={(event) => setNameMode(event.target.value)}
              />
              <span>Last names</span>
            </label>
            <label>
              <input
                type="radio"
                name="name-mode"
                value="FULL"
                checked={nameMode === 'FULL'}
                onChange={(event) => setNameMode(event.target.value)}
              />
              <span>Full names</span>
            </label>
          </div>
        </fieldset>
      </DurationPicker>
    </ReadyScreenLayout>
  )
}

function NamesScreen({
  round,
  secondsLeft,
  memorizeSeconds,
  isCountdownPaused,
  onToggleCountdownPause,
}) {
  return (
    <div className="name-showing-state">
      <p className="stage-kicker">
        Memorize these {NAME_MODE_LABELS[round.nameMode].toLowerCase()}
      </p>
      <ol className="name-display-list">
        {round.names.map((name) => (
          <li key={name}>
            <span className="name-display-text">{name}</span>
          </li>
        ))}
      </ol>
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
  return (
    <GuessForm
      className="name-guess-state"
      title={`Which ${NAME_MODE_LABELS[round.nameMode].toLowerCase()} did you see?`}
      error={error}
      isSubmitting={isSubmitting}
      submitLabel="Check names"
      onSubmit={onSubmit}
    >
      <p className="guess-help">
        Enter {formatQuantity(round.nameCount, 'name')} in the same order, one per line.
      </p>
      <label htmlFor="name-guess">Your answer</label>
      <textarea
        id="name-guess"
        rows={Math.min(8, Math.max(3, round.nameCount))}
        autoComplete="off"
        autoFocus
        value={guess}
        onChange={(event) => onGuessChange(event.target.value)}
        placeholder={round.nameMode === 'FULL' ? 'Ava Patel\nNoah Kim' : 'Ava\nNoah'}
        disabled={isSubmitting}
      />
    </GuessForm>
  )
}

function ResultScreen({ result, onNextRound }) {
  const nextLevel = formatQuantity(result.nextNameCount, 'name')

  return (
    <ResultScreenLayout
      correct={result.correct}
      title={result.correct ? 'Every name is correct!' : 'Those weren’t the names.'}
      nextLevel={nextLevel}
      onNextRound={() => onNextRound(result.nextNameCount)}
    >
      {!result.correct && (
        <div className="name-answer-comparison">
          <p>Your answers compared with the correct names:</p>
          <div className="comparison-table-scroll">
            <table>
              <thead>
                <tr>
                  <th scope="col">#</th>
                  <th scope="col">Your answer</th>
                  <th scope="col">Correct answer</th>
                </tr>
              </thead>
              <tbody>
                {result.correctNames.map((correctName, index) => {
                  const userName = result.userGuesses[index]
                  const isMatch = normalizeName(userName) === normalizeName(correctName)

                  return (
                    <tr key={`${correctName}-${index}`}>
                      <th scope="row">{index + 1}</th>
                      <td className={isMatch ? 'matching-answer' : 'wrong-answer'}>
                        {userName}
                      </td>
                      <td className="correct-answer">{correctName}</td>
                    </tr>
                  )
                })}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </ResultScreenLayout>
  )
}

function StoppedScreen({ onRestartFromBeginning, onChangeSettings }) {
  return (
    <StoppedScreenLayout description="Restart from 1 name, or choose a new name type and memorization time.">
      <button type="button" onClick={onRestartFromBeginning}>
        Restart from 1 name
      </button>
      <button
        className="secondary-button"
        type="button"
        onClick={onChangeSettings}
      >
        Change names &amp; seconds
      </button>
    </StoppedScreenLayout>
  )
}

export default function NameGameStage({ game }) {
  const {
    phase,
    round,
    memorizeSeconds,
    secondsLeft,
    guess,
    setGuess,
    result,
    error,
    isCountdownPaused,
    startGame,
    startRound,
    submitGuess,
    toggleCountdownPause,
    returnToSetup,
    restartFromBeginning,
    retry,
  } = game

  return (
    <div className="game-stage">
      {phase === 'ready' && <ReadyScreen onStart={startGame} />}
      {phase === 'loading' && <LoadingScreen title="Preparing random names" />}
      {phase === 'showing' && round && (
        <NamesScreen
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
          onRestartFromBeginning={restartFromBeginning}
          onChangeSettings={returnToSetup}
        />
      )}
      {phase === 'error' && <ErrorScreen error={error} onRetry={retry} />}
    </div>
  )
}
