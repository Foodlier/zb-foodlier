/* eslint-disable @typescript-eslint/no-use-before-define */
import axios from 'axios'
import { setCookie, removeCookie } from '../utils/Cookies'
import { setInterceptor } from '../utils/FetchCall'

const onSilentRefresh = () => {
  axios
    .post('/api/auth/reissue')
    .then(onLoginSuccess)
    .catch(err => {
      console.log(err)
    })
}

const onLoginSuccess = (res: any) => {
  const JWT_EXPIRY_TIME = 3600000 // 1시간
  const { accessToken, refreshToken } = res.data
  setInterceptor(accessToken)

  // 로그인 성공시 쿠키에 accessToken 저장
  localStorage.setItem('accessToken', JSON.stringify(accessToken))
  setCookie('accessToken', accessToken, { path: '/' })
  setCookie('refreshToken', refreshToken, { path: '/' })

  axios.defaults.headers.common.Authorization = `Bearer ${accessToken}`

  setTimeout(onSilentRefresh, JWT_EXPIRY_TIME - 60000)
}

const onAuthLoginSuccess = (res: any) => {
  const JWT_EXPIRY_TIME = 3600000 // 1시간
  const { accessToken, refreshToken } = res.data.tokenDto

  // 로그인 성공시 쿠키에 accessToken 저장
  setCookie('accessToken', accessToken, { path: '/' })
  setCookie('refreshToken', refreshToken, { path: '/' })

  axios.defaults.headers.common.Authorization = `Bearer ${accessToken}`

  setTimeout(onSilentRefresh, JWT_EXPIRY_TIME - 60000)
}

const LogoutSuccess = () => {
  delete axios.defaults.headers.common.Authorization
  removeCookie('accessToken')
  removeCookie('refreshToken')
}

export { onLoginSuccess, onSilentRefresh, onAuthLoginSuccess, LogoutSuccess }
