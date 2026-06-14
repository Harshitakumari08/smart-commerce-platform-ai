export function isEmail(value: string) {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value);
}

export function isStrongPassword(value: string) {
  return /^(?=.*[A-Z])(?=.*\d).{8,}$/.test(value);
}

export function isPhone(value: string) {
  return /^\+?[0-9]{7,15}$/.test(value);
}
