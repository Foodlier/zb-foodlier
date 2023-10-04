import './reset.css'
import { Routes, Route } from 'react-router-dom'
import LoginPage from './pages/LoginPage'
import RegisterPage from './pages/RegisterPage'
import MainPage from './pages/MainPage'
import RecipePage from './pages/recipe/RecipePage'
import WriteRecipePage from './pages/recipe/WriteRecipePage'
import RecipeDetailPage from './pages/recipe/RecipeDetailPage'
import ChattingPage from './pages/chat/ChattingPage'
import RefrigeratorPage from './pages/refrigerator/RefrigeratorPage'
import RequestPage from './pages/refrigerator/RequestPage'
import RequestDetailPage from './pages/refrigerator/RequestDetailPage'
import WriteQuotationPage from './pages/refrigerator/WriteQuotationPage'
import QuotationDetail from './pages/refrigerator/QuotationDetailPage'

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
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />
      <Route path="/refrigerator" element={<RefrigeratorPage />} />
      <Route path="/refrigerator/request/write" element={<RequestPage />} />
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
        element={<QuotationDetail />}
      />
      <Route path="/recipe" element={<RecipePage />} />
      <Route path="/recipe/write" element={<WriteRecipePage />} />
      <Route path="/recipe/detail" element={<RecipeDetailPage />} />
      <Route path="/chat" element={<ChattingPage />} />
    </Routes>
  )
}

export default App
