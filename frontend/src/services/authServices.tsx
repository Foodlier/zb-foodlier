/* eslint-disable @typescript-eslint/no-use-before-define */
import axios from 'axios'
import { setCookie } from '../utils/Cookies'

const JWT_EXPIRY_TIME = 24 * 3600 * 1000 // 24 hours

const onSilentRefresh = () => {
  axios
    .post('/api/auth/reissue')
    .then(onLoginSuccess)
    .catch(err => {
      console.log(err)
    })
}

const onLoginSuccess = (res: any) => {
  const { accessToken, refreshToken } = res.data

  axios.defaults.headers.common.Authorization = `Bearer ${accessToken}`
  axios.defaults.headers.common.Authorization = `Bearer ${refreshToken}`

  // 로그인 성공시 쿠키에 accessToken 저장
  setCookie('accessToken', accessToken, { path: '/' })
  setCookie('refreshToken', refreshToken, { path: '/' })

  setTimeout(onSilentRefresh, JWT_EXPIRY_TIME - 60000)
}

export { onLoginSuccess, onSilentRefresh }
