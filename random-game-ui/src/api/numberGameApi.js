import { postJson } from './apiClient.js'

const NUMBER_GAME_API_URL = import.meta.env.VITE_NUMBER_API_URL ?? '/api/number-game'

export async function createNumberRound(digitCount, signal) {
  return postJson(
    `${NUMBER_GAME_API_URL}/round`,
    { digitCount },
    signal,
    'The game server could not start a round.',
  )
}

export async function checkNumberGuess(roundId, guess, signal) {
  return postJson(
    `${NUMBER_GAME_API_URL}/guess`,
    { roundId, guess },
    signal,
    'Your answer could not be checked. Please try again.',
  )
}
