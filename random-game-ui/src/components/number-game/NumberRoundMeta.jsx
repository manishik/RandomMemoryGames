import RoundMeta from '../RoundMeta.jsx'

export default function NumberRoundMeta({ round, memorizeSeconds }) {
  const digitCount = round?.digitCount ?? 1

  return (
    <RoundMeta
      levelCount={digitCount}
      levelUnit="digit"
      memorizeSeconds={memorizeSeconds}
    />
  )
}
