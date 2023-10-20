const min = 2
const max = 8

export function validateNickname(nickname: string) {
  const nicknameRegex = new RegExp(`^.{${min},${max}}$`)
  return nicknameRegex.test(nickname)
}

export function validateEmail(email: string) {
  const emailRegex = /^[a-zA-Z0-9]+@[a-zA-Z0-9]+\.[a-zA-Z0-9]+$/
  return emailRegex.test(email)
}

export function validatePassword(password: string) {
  const passwordRegex = /^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\W)(?=\S+$).{8,16}$/
  return passwordRegex.test(password)
}

export function validatePasswordConfirm(
  password: string,
  passwordConfirm: string
) {
  return password === passwordConfirm
}

export function validatePhoneNumber(phoneNumber: string) {
  const phoneNumberRegex = /^010[0-9]{8}$/
  return phoneNumberRegex.test(phoneNumber)
}
