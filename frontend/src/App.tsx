import './reset.css'
import { Routes, Route } from 'react-router-dom'
import LoginPage from './pages/LoginPage'
import RegisterPage from './pages/RegisterPage'
import CookForMe from './pages/refrigerator/CookForMe'
import MainPage from './pages/MainPage'
import RequestDetail from './pages/refrigerator/RequestDetail'
import Request from './pages/refrigerator/Request'
import CookForYou from './pages/refrigerator/CookForYou'
import WriteRecipePage from './pages/recipe/WriteRecipePage'
import WriteEstimatePage from './pages/refrigerator/WriteEstimatePage'
import EstimateDetail from './pages/refrigerator/EstimateDetail'

function App() {
  // const [count, setCount] = useState(0)
  // useEffect(() => {
  //   fetch('/api/test')
  //     .then(response => response.json())
  //     .then(json => setMessage(json.SUCCESS_TEXT))
  // }, [])

  return (
    <Routes>
      <Route path="/" element={<LoginPage />} />
      <Route path="/main" element={<MainPage />} />
      <Route path="/register" element={<RegisterPage />} />
      <Route path="/cook-for-me" element={<CookForMe />} />
      <Route path="/cook-for-you" element={<CookForYou />} />
      <Route path="/request" element={<Request />} />
      <Route path="/request-detail" element={<RequestDetail />} />
      <Route path="/a" element={<WriteRecipePage />} />
      <Route path="/b" element={<WriteEstimatePage />} />
      <Route path="/c" element={<EstimateDetail />} />
    </Routes>
  )
}

export default App
