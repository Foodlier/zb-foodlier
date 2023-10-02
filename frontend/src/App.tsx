import './reset.css'
import { Routes, Route } from 'react-router-dom'
import LoginPage from './pages/LoginPage'
import RegisterPage from './pages/RegisterPage'
import CookForMe from './pages/CookForMe'
import MainPage from './pages/MainPage'
import RecipePage from './pages/recipe/RecipePage'
import WriteRecipePage from './pages/recipe/WriteRecipePage'
import RecipeDetailPage from './pages/recipe/RecipeDetailPage'

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
      <Route path="/recipe" element={<RecipePage />} />
      <Route path="/recipe/write" element={<WriteRecipePage />} />
      <Route path="/recipe/detail" element={<RecipeDetailPage />} />
      <Route path="/register" element={<RegisterPage />} />
      <Route path="/cook-for-me" element={<CookForMe />} />
    </Routes>
  )
}

export default App
