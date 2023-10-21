/* eslint-disable @typescript-eslint/no-use-before-define */
import { EventSourcePolyfill, NativeEventSource } from 'event-source-polyfill'
import axios from 'axios'
import { setCookie, removeCookie } from '../utils/Cookies'

const onSilentRefresh = () => {
  axios
    .post('/api/auth/reissue')
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
  subscribeNoti(accessToken)
}

const subscribeNoti = (accessToken: string) => {
  const EventSource = EventSourcePolyfill || NativeEventSource

  const eventSource = new EventSource(
    'http://localhost:8080/notification/subscribe',
    {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
      heartbeatTimeout: 86400000,
      // withCredentials: true,
    }
  )

  // connection되면
  eventSource.addEventListener('open', function (e) {
    console.log('event open - connect')
    console.log(e)
    // Connection was opened.
  })

  // error 나면
  eventSource.addEventListener('error', function (e) {
    console.log('event error')
    console.log(e)
  })

  eventSource.addEventListener('sse', (e: any) => {
    console.log(e)
    console.log('json:', JSON.parse(e.data))
  })
}

const LogoutSuccess = () => {
  delete axios.defaults.headers.common.Authorization
  removeCookie('accessToken')
  removeCookie('refreshToken')
}

export { onLoginSuccess, onSilentRefresh, LogoutSuccess }
