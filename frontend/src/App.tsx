import './reset.css'
import { useEffect, useState } from 'react'
import { Routes, Route } from 'react-router-dom'
import styled from 'styled-components'
import LoginPage from './pages/LoginPage'
import RegisterPage from './pages/RegisterPage'
import CookForMe from './pages/CookForMe'
import MainPage from './pages/MainPage'
import RecipePage from './pages/recipe/RecipePage'
import WriteRecipePage from './pages/recipe/WriteRecipePage'
import RecipeDetailPage from './pages/recipe/RecipeDetailPage'
import ChattingPage from './pages/chat/ChattingPage'
import Notification from './components/Notification'

const Flex = styled.div`
  position: fixed;
  top: 20px;
  left: 20px;
  display: flex;
  flex-direction: column;
`

function App() {
  const [message, setMessage] = useState('')

  useEffect(() => {
    const eventSource = new EventSource('http://localhost:8080/sse')
    eventSource.onmessage = e => {
      setMessage(JSON.parse(e.data))
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
        <Route path="/cook-for-me" element={<CookForMe />} />
        <Route path="/recipe" element={<RecipePage />} />
        <Route path="/recipe/write" element={<WriteRecipePage />} />
        <Route path="/recipe/detail" element={<RecipeDetailPage />} />
        <Route path="/chat" element={<ChattingPage />} />
      </Routes>
      <Flex>
        <Notification message={message} />
      </Flex>
    </div>
  )
}

export default App
