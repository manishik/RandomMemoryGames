import { useLayoutEffect, useState } from 'react'
import GamePage from './components/GamePage.jsx'
import GamePicker from './components/GamePicker.jsx'
import NameGameStage from './components/name-game/NameGameStage.jsx'
import NameRoundMeta from './components/name-game/NameRoundMeta.jsx'
import NumberGameStage from './components/number-game/NumberGameStage.jsx'
import NumberRoundMeta from './components/number-game/NumberRoundMeta.jsx'
import { useNameGame } from './hooks/useNameGame.js'
import { useNumberGame } from './hooks/useNumberGame.js'

const DEFAULT_THEME = 'dark'

function getInitialTheme() {
  try {
    const savedTheme = window.localStorage.getItem('memory-game-theme')
    return savedTheme === 'light' || savedTheme === 'dark' ? savedTheme : DEFAULT_THEME
  } catch {
    return DEFAULT_THEME
  }
}

function AppShell({ children }) {
  return (
    <main className="app-shell">
      <div className="ambient ambient-one" />
      <div className="ambient ambient-two" />
      {children}
    </main>
  )
}

function NumberGame({ onBack }) {
  const game = useNumberGame()

  return (
    <AppShell>
      <GamePage
        mark="RN"
        title="Random Number Game"
        phase={game.phase}
        onBack={onBack}
        onStop={game.stopGame}
        roundMeta={<NumberRoundMeta round={game.round} memorizeSeconds={game.memorizeSeconds} />}
        tip="Tip: group longer numbers into chunks of two or three digits."
      >
        <NumberGameStage game={game} />
      </GamePage>
    </AppShell>
  )
}

function NameGame({ onBack }) {
  const game = useNameGame()

  return (
    <AppShell>
      <GamePage
        mark="NM"
        title="Random Name Game"
        phase={game.phase}
        onBack={onBack}
        onStop={game.stopGame}
        roundMeta={(
          <NameRoundMeta
            round={game.round}
            nameMode={game.nameMode}
            memorizeSeconds={game.memorizeSeconds}
          />
        )}
        tip="Tip: connect each name to a familiar person or mental image."
      >
        <NameGameStage game={game} />
      </GamePage>
    </AppShell>
  )
}

function App() {
  const [selectedGame, setSelectedGame] = useState(null)
  const [theme, setTheme] = useState(getInitialTheme)

  useLayoutEffect(() => {
    document.documentElement.dataset.theme = theme
    try {
      window.localStorage.setItem('memory-game-theme', theme)
    } catch {
      // The selected theme still applies when storage is unavailable.
    }
  }, [theme])

  if (selectedGame === 'number') {
    return <NumberGame onBack={() => setSelectedGame(null)} />
  }

  if (selectedGame === 'name') {
    return <NameGame onBack={() => setSelectedGame(null)} />
  }

  return (
    <AppShell>
      <GamePicker theme={theme} onThemeChange={setTheme} onSelect={setSelectedGame} />
    </AppShell>
  )
}

export default App
