import './reset.css'
import { Routes, Route } from 'react-router-dom'
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
import WriteReviewPage from './pages/recipe/WriteReviewPage'
import ChattingPage from './pages/chat/ChattingPage'
import MyProfilePage from './pages/user/MyProfilePage'
import MyPage from './pages/user/MyPage'
import DmTest1 from './pages/chat/DmTest1'
import DmTest2 from './pages/chat/DmTest2'

function App() {
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
        <Route
          path="/recipe/detail/write-review"
          element={<WriteReviewPage />}
        />
        <Route path="/chat" element={<ChattingPage />} />
        <Route path="/my" element={<MyPage />} />
        <Route path="/myprofilepage" element={<MyProfilePage />} />
        <Route path="/dmtest1" element={<DmTest1 />} />
        <Route path="/dmtest2" element={<DmTest2 />} />
      </Routes>
    </div>
  )
}

export default App
