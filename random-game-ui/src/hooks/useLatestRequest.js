import { useCallback, useEffect, useRef } from 'react'

export function useLatestRequest() {
  const activeController = useRef(null)

  const abortRequest = useCallback(() => {
    activeController.current?.abort()
    activeController.current = null
  }, [])

  const runLatestRequest = useCallback(async (request) => {
    abortRequest()

    const controller = new AbortController()
    activeController.current = controller

    try {
      return await request(controller.signal)
    } finally {
      if (activeController.current === controller) {
        activeController.current = null
      }
    }
  }, [abortRequest])

  useEffect(() => abortRequest, [abortRequest])

  return { runLatestRequest, abortRequest }
}
