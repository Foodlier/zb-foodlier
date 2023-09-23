// import React from 'react'
import { Routes, Route } from 'react-router-dom'
import MainPage from './pages/MainPage'
import Header from './components/Header'
import BottomNavigation from './components/BottomNavigation'

function App() {
  return (
    <>
      <Header />
      <Routes>
        <Route path="/" element={<MainPage />} />
      </Routes>
      <BottomNavigation />
    </>
  )
}

export default App
