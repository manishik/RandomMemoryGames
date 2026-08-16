import RoundMeta from '../RoundMeta.jsx'
import { NAME_MODE_LABELS } from '../../constants/nameModes.js'

export default function NameRoundMeta({ round, memorizeSeconds, nameMode }) {
  const nameCount = round?.nameCount ?? 1

  return (
    <RoundMeta
      className="name-round-meta"
      levelCount={nameCount}
      levelUnit="name"
      memorizeSeconds={memorizeSeconds}
    >
      <div>
        <span className="meta-label">Name type</span>
        <strong>{NAME_MODE_LABELS[nameMode]}</strong>
      </div>
    </RoundMeta>
  )
}
