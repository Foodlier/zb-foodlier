/* eslint-disable import/prefer-default-export */
/* eslint-disable import/no-extraneous-dependencies */
import { Cookies } from 'react-cookie'

const cookies = new Cookies()

export const setCookie = (key: string, value: string, option?: any) => {
  cookies.set(key, value, { ...option })
}

export const getCookie = (key: string) => {
  return cookies.get(key)
}

export const removeCookie = (key: string) => {
  cookies.remove(key)
}
