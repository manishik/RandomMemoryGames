import { useState } from 'react'
import { useLatestRequest } from './useLatestRequest.js'
import { useMemorizeCountdown } from './useMemorizeCountdown.js'

export function useGameSession() {
  const [round, setRound] = useState(null)
  const [phase, setPhase] = useState('ready')
  const [memorizeSeconds, setMemorizeSeconds] = useState(3)
  const [guess, setGuess] = useState('')
  const [result, setResult] = useState(null)
  const [error, setError] = useState('')
  const { runLatestRequest, abortRequest } = useLatestRequest()
  const {
    secondsLeft,
    isPaused: isCountdownPaused,
    togglePause: toggleCountdownPause,
    reset: resetCountdown,
  } = useMemorizeCountdown({
    isActive: phase === 'showing' && round !== null,
    durationSeconds: memorizeSeconds,
    onComplete: () => setPhase('guessing'),
  })

  async function loadRound(request) {
    setPhase('loading')
    setError('')
    setGuess('')
    setResult(null)
    resetCountdown()

    try {
      const nextRound = await runLatestRequest(request)
      setRound(nextRound)
      setPhase('showing')
      return nextRound
    } catch (requestError) {
      if (requestError.name !== 'AbortError') {
        setError(requestError.message)
        setPhase('error')
      }
      return null
    }
  }

  async function submitRoundGuess(request, mapResult) {
    setError('')
    setPhase('submitting')

    try {
      const response = await runLatestRequest(request)
      setResult(mapResult(response))
      setPhase('result')
      return response
    } catch (requestError) {
      if (requestError.name !== 'AbortError') {
        setError(requestError.message)
        setPhase('guessing')
      }
      return null
    }
  }

  function stopSession() {
    abortRequest()
    setPhase('stopped')
    resetCountdown()
    setGuess('')
    setResult(null)
    setError('')
  }

  function resetSession() {
    abortRequest()
    setMemorizeSeconds(3)
    setRound(null)
    setGuess('')
    setResult(null)
    setError('')
    resetCountdown()
    setPhase('ready')
  }

  const game = {
    round,
    phase,
    memorizeSeconds,
    secondsLeft,
    guess,
    setGuess,
    result,
    error,
    isCountdownPaused,
    toggleCountdownPause,
    stopGame: stopSession,
  }

  return {
    game,
    setRound,
    setMemorizeSeconds,
    setError,
    loadRound,
    submitRoundGuess,
    resetSession,
  }
}
