import { postJson } from './apiClient.js'

const NAME_GAME_API_URL = import.meta.env.VITE_NAME_API_URL ?? '/api/name-game'

export async function createNameRound(nameCount, nameMode, signal) {
  return postJson(
    `${NAME_GAME_API_URL}/round`,
    { nameCount, nameMode },
    signal,
    'The game server could not start a name round.',
  )
}

export async function checkNameGuess(roundId, guesses, signal) {
  return postJson(
    `${NAME_GAME_API_URL}/guess`,
    { roundId, guesses },
    signal,
    'Your names could not be checked. Please try again.',
  )
}
