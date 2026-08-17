# Memory Games UI

A beginner-friendly React application for practicing memory with two games:

- **Random Number Game** — memorize an increasingly long number.
- **Random Name Game** — memorize first names, last names, or full names.

Players can choose the memorization time, pause and resume the countdown, stop
or restart a game, and use either the light or dark theme. Number text and dot
indicators resize to remain inside the game card, and long incorrect answers
stack vertically so every digit remains visible.

The UI uses React and Vite. It calls the Spring Boot API in
`../random-game-api` to create rounds and check answers.

## Run the application

You need Node.js/npm, Java 21, and Maven.

### 1. Start the API

From `random-game-api`:

```bash
mvn spring-boot:run
```

The API starts at `http://localhost:8080`.

### 2. Start the UI

From `random-game-ui`:

```bash
npm install
npm run dev
```

Open `http://localhost:5173`.

You normally need `npm install` only after cloning the project or changing its
dependencies.

## How the code is organized

The application separates responsibilities into a few simple layers:

```text
Components       -> display the page and report player actions
Game hooks       -> contain number-specific or name-specific rules
Shared hooks     -> manage phases, requests, countdowns, and pause state
API modules      -> send JSON requests to Spring Boot
Styles           -> control layout, colors, and light/dark themes
```

This keeps shared behavior in one place while leaving the two games free to
have different validation and result displays.

## Project structure

```text
src/
├── api/
│   ├── apiClient.js          Shared POST/JSON request helper
│   ├── numberGameApi.js      Number-game endpoints
│   └── nameGameApi.js        Name-game endpoints
├── components/
│   ├── number-game/          Number-game screens and metadata
│   ├── name-game/            Name-game screens and metadata
│   ├── CountdownBar.jsx      Countdown and Pause/Resume button
│   ├── DurationPicker.jsx    Memorization-time form
│   ├── GameHeader.jsx        Game title, back, and stop controls
│   ├── GamePage.jsx          Shared game-page layout
│   ├── GamePicker.jsx        Landing-page game choices
│   ├── GameStageLayouts.jsx  Shared ready and guess layouts
│   ├── GameStatusScreens.jsx Shared loading, result, stop, and error layouts
│   ├── RoundMeta.jsx         Shared round information
│   └── ThemePicker.jsx       Light/Dark selector
├── hooks/
│   ├── useGameSession.js     Shared game state and phase changes
│   ├── useLatestRequest.js   Request cancellation
│   ├── useMemorizeCountdown.js Countdown and pause timing
│   ├── useNumberGame.js      Number-game rules
│   └── useNameGame.js        Name-game rules
├── constants/                Shared fixed labels
├── utils/
│   ├── formatQuantity.js     Singular/plural text formatting
│   ├── numberValidation.js   Whole-number range validation
│   └── preventCopy.js        Shared display copy-prevention handler
├── App.jsx                   Selects the page and owns the theme
├── main.jsx                  Starts React
└── styles.css                Layout and both color themes
```

`constants/` and `utils/` contain definitions used in more than one component.
They prevent the same labels, plural formatting, or validation logic from being
copied into multiple files.

## What happens when the page opens?

```text
index.html
   -> provides the root HTML element
main.jsx
   -> renders App
App.jsx
   -> displays GamePicker
GamePicker
   -> lets the player choose a theme and a game
```

`App` stores the selected game with React state. No router is needed because
the application switches between only three views: the game picker, number
game, and name game.

## Light and dark themes

The initial page contains an **Appearance** selector. Dark is used when the
player has not saved a preference yet.

When the player chooses Light or Dark:

1. `App.jsx` updates the theme state.
2. The theme is applied to the document using `data-theme`.
3. `styles.css` applies the matching colors.
4. The choice is saved in browser `localStorage` for the next visit.

The selected theme remains active when the player opens either game.

## What happens during a game?

Starting a number round follows this path:

```text
Player clicks Start
   -> NumberGameStage calls startGame
   -> useNumberGame asks useGameSession to load a round
   -> numberGameApi calls the Spring Boot API
   -> useGameSession stores the round and starts the showing phase
   -> React displays the number and countdown
```

The name game follows the same shared flow through `useNameGame` and
`nameGameApi`.

`useMemorizeCountdown` owns the timer for both games. Pausing freezes the
remaining time and progress bar; resuming continues from the same point.

## Adaptive answers and progress

The number game keeps long values inside their available space instead of
using horizontal scrolling:

- the number shown during memorization shrinks only when it reaches the card edge;
- the typed number follows the same font and shrink-to-fit behavior;
- one progress dot is shown for every expected digit;
- dots fill and clear as digits are typed or deleted;
- long incorrect results put the correct answer above the player's answer and
  shrink each value to its own row.

The name answer field also uses responsive text sizing and wraps long names
inside the field.

Displayed challenges and result answers use the shared `preventCopy.js`
handler and `.copy-protected` style. This prevents normal text selection and
copying for both numbers and names without blocking the answer inputs. It is a
casual-game safeguard, not a security boundary: values delivered to the browser
can still be inspected with browser developer tools.

## Game phases

`useGameSession` stores one `phase`. The stage component uses it to choose the
screen to display.

| Phase | What the player sees |
| --- | --- |
| `ready` | Game settings |
| `loading` | A loading message |
| `showing` | The number or names and countdown |
| `guessing` | The answer form |
| `submitting` | The disabled form while the answer is checked |
| `result` | The correct or incorrect result |
| `stopped` | Restart and settings options |
| `error` | An error message and retry button |

Keeping one phase prevents multiple game screens from appearing together.

## Shared code

The main shared modules are:

| File | Responsibility |
| --- | --- |
| `apiClient.js` | Sends POST requests, serializes JSON, and checks responses |
| `useGameSession.js` | Manages shared state and phase transitions |
| `useLatestRequest.js` | Cancels an older request when a newer one starts |
| `useMemorizeCountdown.js` | Manages countdown, pause, resume, and reset |
| `CountdownBar.jsx` | Displays countdown controls for both games |
| `GameStageLayouts.jsx` | Provides shared ready and answer-form layouts |
| `GameStatusScreens.jsx` | Provides shared loading, result, stop, and error layouts |
| `preventCopy.js` | Prevents normal selection/copying of displayed numbers and names |

Number- and name-specific rules remain in their matching hooks and components.
For example, number guesses require digits, while name guesses allow multiple
lines and ignore case differences.

## React concepts used here

### Components and props

A component is a function that returns JSX. Props pass data and actions from a
parent component to a child.

```jsx
<NumberGameStage game={game} />
```

### State

State stores values that can change, such as the selected game or theme.
Updating state causes React to render the affected components again.

```jsx
const [selectedGame, setSelectedGame] = useState(null)
```

### Effects and refs

The shared hooks use effects to run cleanup when timers, requests, or components
change. Refs remember timer and request details without causing extra renders.

### Controlled inputs

The answer inputs receive their value from React state and update that state in
`onChange`. React is therefore the source of truth for the form.

## UI and API connection

| UI function | Spring Boot endpoint |
| --- | --- |
| `createNumberRound` | `POST /api/number-game/round` |
| `checkNumberGuess` | `POST /api/number-game/guess` |
| `createNameRound` | `POST /api/name-game/round` |
| `checkNameGuess` | `POST /api/name-game/guess` |

During development, Vite forwards `/api/...` requests from port `5173` to the
Spring Boot server on port `8080`. Browser developer tools therefore show the
request URL on port `5173`; the forwarding to port `8080` happens inside the
Vite development server.

For a separately hosted API, set `VITE_NUMBER_API_URL` and `VITE_NAME_API_URL`
when building. Never put passwords or secrets in Vite environment variables,
because browser code can read them.

## Useful commands

| Command | Purpose |
| --- | --- |
| `npm run dev` | Start the development server |
| `npm run build` | Create the optimized `dist/` build |
| `npm run preview` | Preview the production build locally |

## Suggested learning order

1. `src/main.jsx` — where React starts.
2. `src/App.jsx` — page and theme selection.
3. `src/components/GamePicker.jsx` — a small component using props.
4. `src/components/number-game/NumberGameStage.jsx` — phase-based rendering.
5. `src/hooks/useNumberGame.js` — number-specific behavior.
6. `src/hooks/useGameSession.js` — shared game behavior.
7. `src/api/numberGameApi.js` and `src/api/apiClient.js` — server requests.
8. Compare the matching name-game files.
9. Read `styles.css` after the component flow makes sense.

When changing the application:

- visual content belongs in a component;
- game-specific behavior belongs in the matching game hook;
- shared game behavior belongs in a shared hook;
- HTTP endpoints belong in the matching API module;
- reusable request mechanics belong in `apiClient.js`;
- layout and colors belong in `styles.css`.
