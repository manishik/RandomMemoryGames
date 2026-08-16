import { useState } from 'react'
import { checkNumberGuess, createNumberRound } from '../api/numberGameApi.js'
import { useGameSession } from './useGameSession.js'

export function useNumberGame() {
  const [currentDigitCount, setCurrentDigitCount] = useState(1)
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

  async function startRound(digitCount) {
    setCurrentDigitCount(digitCount)
    await loadRound((signal) => createNumberRound(digitCount, signal))
  }

  async function submitGuess() {
    setError('')

    if (!/^\d+$/.test(guess)) {
      setError('Enter a whole number before checking your answer.')
      return
    }

    await submitRoundGuess(
      (signal) => checkNumberGuess(round.roundId, guess, signal),
      (response) => ({ ...response, userGuess: guess }),
    )
  }

  function startGame(seconds, selectedStartingDigitCount) {
    setMemorizeSeconds(seconds)
    setRound(null)
    startRound(selectedStartingDigitCount)
  }

  function restartCurrentLevel() {
    if (currentDigitCount) {
      setRound(null)
      startRound(currentDigitCount)
    }
  }

  function returnToSetup() {
    resetSession()
    setCurrentDigitCount(1)
  }

  function retry() {
    startRound(currentDigitCount)
  }

  return {
    ...game,
    currentDigitCount,
    startGame,
    startRound,
    submitGuess,
    restartCurrentLevel,
    returnToSetup,
    retry,
  }
}
