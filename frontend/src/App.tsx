import './reset.css'
import { useEffect, useState } from 'react'
import { Routes, Route } from 'react-router-dom'
import { EventSourcePolyfill } from 'event-source-polyfill'
import styled from 'styled-components'
import RefrigeratorPage from './pages/refrigerator/RefrigeratorPage'
import WriteRequestPage from './pages/refrigerator/WriteRequestPage'
import RequestDetailPage from './pages/refrigerator/RequestDetailPage'
import WriteQuotationPage from './pages/refrigerator/WriteQuotationPage'
import QuotationDetailPage from './pages/refrigerator/QuotationDetailPage'
import LoginPage from './pages/auth/LoginPage'
import RegisterPage from './pages/auth/RegisterPage'
import MainPage from './pages/MainPage'
import RecipePage from './pages/recipe/RecipePage'
import WriteRecipePage from './pages/recipe/WriteRecipePage'
import RecipeDetailPage from './pages/recipe/RecipeDetailPage'
import ChattingPage from './pages/chat/ChattingPage'
import Notification from './components/Notification'
import MyProfilePage from './pages/user/MyProfilePage'
import StarRating from './components/StarRating'

const Flex = styled.div`
  position: fixed;
  top: 10rem;
  left: 20px;
  display: flex;
  flex-direction: column;
`

function App() {
  const [message, setMessage] = useState<string[]>([])

  useEffect(() => {
    const URL = 'http://localhost:3000/sse'
    const TOKEN = ''
    const eventSource = new EventSourcePolyfill(URL, {
      headers: {
        token: TOKEN,
      },
    })
    eventSource.onmessage = e => {
      console.log(e)
      setMessage(prevMessages => [...prevMessages, JSON.stringify(e.data)])
    }
    eventSource.onerror = error => {
      // 에러 핸들링
      console.error('SSE 연결 중 오류 발생:', error)
      eventSource.close() // 오류 발생 시 SSE 연결을 종료합니다.
    }

    return () => {
      // 컴포넌트 언마운트 시 SSE 연결 해제
      eventSource.close()
    }
  }, [])

  return (
    <div>
      <Routes>
        <Route path="/" element={<MainPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/refrigerator" element={<RefrigeratorPage />} />
        <Route
          path="/refrigerator/request/write"
          element={<WriteRequestPage />}
        />
        <Route
          path="/refrigerator/request/detail"
          element={<RequestDetailPage />}
        />
        <Route
          path="/refrigerator/quotation/write"
          element={<WriteQuotationPage />}
        />
        <Route
          path="/refrigerator/quotation/detail"
          element={<QuotationDetailPage />}
        />
        <Route path="/refrigerator/" element={<RefrigeratorPage />} />
        <Route path="/recipe" element={<RecipePage />} />
        <Route path="/recipe/write" element={<WriteRecipePage />} />
        <Route path="/recipe/detail" element={<RecipeDetailPage />} />
        <Route path="/chat" element={<ChattingPage />} />
        <Route path="/myprofile" element={<MyProfilePage />} />
        <Route path="/test" element={<StarRating />} />
      </Routes>
      <Flex>
        {message.map(item => (
          <Notification key={item} message={item} />
        ))}
      </Flex>
    </div>
  )
}

export default App
