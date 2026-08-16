import { formatQuantity } from '../utils/formatQuantity.js'

export default function RoundMeta({
  levelCount,
  levelUnit,
  memorizeSeconds,
  className = '',
  children,
}) {
  const classes = ['round-meta', className].filter(Boolean).join(' ')

  return (
    <div className={classes}>
      <div>
        <span className="meta-label">Current level</span>
        <strong>{formatQuantity(levelCount, levelUnit)}</strong>
      </div>
      {children}
      <div>
        <span className="meta-label">Memorize time</span>
        <strong>{formatQuantity(memorizeSeconds, 'second')}</strong>
      </div>
    </div>
  )
}
