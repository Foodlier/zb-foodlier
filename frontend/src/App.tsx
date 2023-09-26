import './reset.css'
import { Routes, Route } from 'react-router-dom'
import LoginPage from './pages/LoginPage'
import MainPage from './pages/MainPage'
import RegisterPage from './pages/RegisterPage'
import CookForMe from './pages/refrigerator/CookForMe'
import RequestDetail from './pages/refrigerator/RequestDetail'

function App() {
  // const [count, setCount] = useState(0)
  // useEffect(() => {
  //   fetch('/api/test')
  //     .then(response => response.json())
  //     .then(json => setMessage(json.SUCCESS_TEXT))
  // }, [])

  return (
    <Routes>
      <Route path="/" element={<MainPage />} />
      <Route path="/" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />
      <Route path="/cook-for-me" element={<CookForMe />} />
      <Route path="/request-detail" element={<RequestDetail />} />
    </Routes>
  )
}

export default App
