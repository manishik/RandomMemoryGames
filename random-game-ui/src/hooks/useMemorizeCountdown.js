import { useCallback, useEffect, useRef, useState } from 'react'

export function useMemorizeCountdown({ isActive, durationSeconds, onComplete }) {
  const [secondsLeft, setSecondsLeft] = useState(0)
  const [isPaused, setIsPaused] = useState(false)
  const remainingTimeMs = useRef(null)
  const resetVersion = useRef(0)
  const onCompleteRef = useRef(onComplete)

  useEffect(() => {
    onCompleteRef.current = onComplete
  }, [onComplete])

  useEffect(() => {
    if (!isActive || isPaused) {
      return undefined
    }

    const displayTimeMs = remainingTimeMs.current ?? durationSeconds * 1000
    const startedAt = window.performance.now()
    const currentResetVersion = resetVersion.current

    function updateSecondsLeft() {
      const elapsedTimeMs = window.performance.now() - startedAt
      setSecondsLeft(Math.ceil(Math.max(0, displayTimeMs - elapsedTimeMs) / 1000))
    }

    updateSecondsLeft()

    const countdown = window.setInterval(updateSecondsLeft, 250)
    const finishCountdown = window.setTimeout(() => {
      remainingTimeMs.current = 0
      setSecondsLeft(0)
      onCompleteRef.current()
    }, displayTimeMs)

    return () => {
      window.clearInterval(countdown)
      window.clearTimeout(finishCountdown)

      if (resetVersion.current === currentResetVersion) {
        remainingTimeMs.current = Math.max(
          0,
          displayTimeMs - (window.performance.now() - startedAt),
        )
      }
    }
  }, [isActive, durationSeconds, isPaused])

  const togglePause = useCallback(() => {
    if (isActive) {
      setIsPaused((current) => !current)
    }
  }, [isActive])

  const reset = useCallback(() => {
    resetVersion.current += 1
    remainingTimeMs.current = null
    setSecondsLeft(0)
    setIsPaused(false)
  }, [])

  return {
    secondsLeft,
    isPaused,
    togglePause,
    reset,
  }
}
