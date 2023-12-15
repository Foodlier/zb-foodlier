import './reset.css'
import { useEffect, useState } from 'react'
import { EventSourcePolyfill, NativeEventSource } from 'event-source-polyfill'
import { Routes, Route } from 'react-router-dom'
import { getCookie } from './utils/Cookies'
import RefrigeratorPage from './pages/refrigerator/RefrigeratorPage'
import WriteRequestPage from './pages/refrigerator/WriteRequestPage'
import RequestDetailPage from './pages/refrigerator/RequestDetailPage'
import WriteQuotationPage from './pages/refrigerator/WriteQuotationPage'
import QuotationDetailPage from './pages/refrigerator/QuotationDetailPage'
import RequestReviewPage from './pages/refrigerator/RequestReviewPage'
import LoginPage from './pages/auth/LoginPage'
import RegisterPage from './pages/auth/RegisterPage'
import MainPage from './pages/MainPage'
import RecipePage from './pages/recipe/RecipePage'
import WriteRecipePage from './pages/recipe/WriteRecipePage'
import RecipeDetailPage from './pages/recipe/RecipeDetailPage'
import WriteReviewPage from './pages/recipe/WriteReviewPage'
import ChattingPage from './pages/chat/ChattingPage'
import FindPasswordPage from './pages/auth/FindPasswordPage'
import MyPage from './pages/user/MyPage'
import ProfilePage from './pages/user/ProfilePage'
import ProfileMorePage from './pages/user/ProfileMorePage'
import PointPage from './pages/point/PointPage'
import SuccessPage from './pages/point/SuccessPage'
import KakaoLoginPage from './pages/auth/KakaoRedirectionPage'
import NaverLoginPage from './pages/auth/NaverRedirectionPage'
import MyLogPage from './pages/user/MyLogPage'
import NotFoundPage from './pages/NotFoundPage'
import TradeHistoryPage from './pages/point/TradeHistoryPage'
import ChargeHistoryPage from './pages/point/ChargeHistoryPage'
import Notification from './components/Notification'
import ProfileEditPage from './pages/user/ProfileEditPage'
import EditPasswordPage from './pages/user/EditPasswordPage'

function App() {
  const [isNoti, setIsNoti] = useState(false)
  const [show, setShow] = useState(false)
  const [message, setMessage] = useState('')

  useEffect(() => {
    const connect = () => {
      if (getCookie('accessToken')) {
        const EventSource = EventSourcePolyfill || NativeEventSource

        const eventSource = new EventSource(
          'https://foodlier.store/notification/subscribe',
          {
            headers: {
              Authorization: `Bearer ${getCookie('accessToken')}`,
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
          const noti = JSON.parse(e.data)

          if (noti.id > 0) {
            setIsNoti(true)
            setShow(true)
            setMessage(noti.content)
            setTimeout(() => {
              setShow(false)
            }, 5000)
            setTimeout(() => {
              setIsNoti(false)
            }, 6000)
          }
        })

        return () => {
          console.log('연결이 끊어졌습니다.')
          eventSource.close()
        }
      }
    }
    connect()
  }, [])
  return (
    <div>
      <Routes>
        <Route path="/" element={<MainPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/find-password" element={<FindPasswordPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/kakao/callback" element={<KakaoLoginPage />} />
        <Route path="/naver/callback" element={<NaverLoginPage />} />
        <Route path="/refrigerator" element={<RefrigeratorPage />} />
        <Route
          path="/refrigerator/request/write"
          element={<WriteRequestPage />}
        />
        <Route
          path="/refrigerator/request/detail/:id"
          element={<RequestDetailPage />}
        />
        <Route
          path="/refrigerator/request/review"
          element={<RequestReviewPage />}
        />
        <Route
          path="/refrigerator/quotation/write"
          element={<WriteQuotationPage />}
        />
        <Route
          path="/refrigerator/quotation/detail/:id"
          element={<QuotationDetailPage />}
        />
        <Route path="/refrigerator/" element={<RefrigeratorPage />} />
        <Route path="/recipe" element={<RecipePage />} />
        <Route path="/recipe/write" element={<WriteRecipePage />} />
        <Route path="/recipe/detail/:id" element={<RecipeDetailPage />} />
        <Route
          path="/recipe/detail/write-review/:id"
          element={<WriteReviewPage />}
        />
        <Route path="/chat" element={<ChattingPage />} />
        <Route path="/my" element={<MyPage />} />
        <Route path="/profile/:id" element={<ProfilePage />} />
        <Route path="/profile/:id/more" element={<ProfileMorePage />} />
        <Route path="/point" element={<PointPage />} />
        <Route path="/point/success" element={<SuccessPage />} />
        <Route path="/point/fail" element={<SuccessPage />} />
        <Route path="/my/trade-history" element={<TradeHistoryPage />} />
        <Route path="/my/charge-history" element={<ChargeHistoryPage />} />
        <Route path="/my/:category" element={<MyLogPage />} />
        <Route path="/my/edit" element={<ProfileEditPage />} />
        <Route path="/my/edit/password" element={<EditPasswordPage />} />
        <Route path="*" element={<NotFoundPage />} />
      </Routes>
      {isNoti && <Notification text={message} show={show} setShow={setShow} />}
    </div>
  )
}

export default App
