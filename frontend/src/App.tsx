import { Routes, Route } from 'react-router-dom'

import MainPage from './pages/MainPage'
import Header from './components/Header'
import BottomNavigation from './components/BottomNavigation'
import Login from './pages/Login'
import CookForMe from './pages/CookForMe'

function App() {
  return (
    <>
      <Header />
      <Routes>
        <Route path="/" element={<MainPage />} />
        <Route path="/login" element={<Login />} />
        <Route path="/cookforme" element={<CookForMe />} />
      </Routes>
      <BottomNavigation />
    </>
  )
}

export default App
