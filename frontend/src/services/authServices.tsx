/* eslint-disable @typescript-eslint/no-use-before-define */
import axios from 'axios'
import { setCookie, removeCookie } from '../utils/Cookies'

const onSilentRefresh = () => {
  axios
    .post('/auth/reissue')
    .then(onLoginSuccess)
    .catch(err => {
      console.log(err)
    })
}

const onLoginSuccess = (res: any) => {
  const JWT_EXPIRY_TIME = 24 * 3600 * 1000 // 24 hours
  const { accessToken, refreshToken } = res.data

  axios.defaults.headers.common.Authorization = `Bearer ${accessToken}`
  axios.defaults.headers.common.Authorization = `Bearer ${refreshToken}`

  // 로그인 성공시 쿠키에 accessToken 저장
  setCookie('accessToken', accessToken, { path: '/' })
  setCookie('refreshToken', refreshToken, { path: '/' })

  setTimeout(onSilentRefresh, JWT_EXPIRY_TIME - 60000)
}

const LogoutSuccess = () => {
  delete axios.defaults.headers.common.Authorization
  removeCookie('accessToken')
  removeCookie('refreshToken')
}

export { onLoginSuccess, onSilentRefresh, LogoutSuccess }
