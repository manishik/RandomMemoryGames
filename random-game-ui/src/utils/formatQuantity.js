export function formatQuantity(count, singularUnit) {
  return `${count} ${count === 1 ? singularUnit : `${singularUnit}s`}`
}
