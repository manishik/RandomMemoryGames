import { useState } from 'react'
import { checkNameGuess, createNameRound } from '../api/nameGameApi.js'
import { useGameSession } from './useGameSession.js'

export function useNameGame() {
  const [nameMode, setNameMode] = useState('FIRST')
  const session = useGameSession()
  const {
    game,
    setRound,
    setError,
    setMemorizeSeconds,
    loadRound,
    submitRoundGuess,
    resetSession,
  } = session
  const { round, guess } = game

  async function startRound(nameCount, selectedNameMode = nameMode) {
    await loadRound((signal) => createNameRound(nameCount, selectedNameMode, signal))
  }

  async function submitGuess() {
    setError('')

    const guesses = guess
      .split(/\n|,/)
      .map((name) => name.trim())
      .filter(Boolean)

    if (guesses.length !== round.nameCount) {
      setError(`Enter ${round.nameCount} ${round.nameCount === 1 ? 'name' : 'names'}, one per line.`)
      return
    }

    await submitRoundGuess(
      (signal) => checkNameGuess(round.roundId, guesses, signal),
      (response) => ({ ...response, userGuesses: guesses }),
    )
  }

  function startGame(seconds, selectedNameMode) {
    setMemorizeSeconds(seconds)
    setNameMode(selectedNameMode)
    setRound(null)
    startRound(1, selectedNameMode)
  }

  function returnToSetup() {
    resetSession()
    setNameMode('FIRST')
  }

  function restartFromBeginning() {
    setRound(null)
    startRound(1)
  }

  function retry() {
    startRound(round?.nameCount ?? 1)
  }

  return {
    ...game,
    nameMode,
    startGame,
    startRound,
    submitGuess,
    returnToSetup,
    restartFromBeginning,
    retry,
  }
}
