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
import FindPasswordPage from './pages/auth/FindPasswordPage'
import MyPage from './pages/user/MyPage'
import ProfilePage from './pages/user/ProfilePage'
import ProfileMorePage from './pages/user/ProfileMorePage'
import Point from './pages/point/Point'
import Success from './pages/point/Success'
import KakaoLoginPage from './pages/auth/KakaoRedirectionPage'
import NaverLoginPage from './pages/auth/NaverRedirectionPage'
import MyLogPage from './pages/user/MyLogPage'
import NotFoundPage from './pages/NotFoundPage'
import { getCookie } from './utils/Cookies'

function App() {
  return (
    <div>
      <Routes>
        {getCookie('accessToken') ? (
          <Route path="/" element={<MainPage />} />
        ) : (
          <Route path="/login" element={<LoginPage />} />
        )}
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
          path="/recipe/detail/write-review"
          element={<WriteReviewPage />}
        />
        <Route path="/chat" element={<ChattingPage />} />
        <Route path="/my" element={<MyPage />} />
        <Route path="/profile/:id" element={<ProfilePage />} />
        <Route path="/profile/:id/more" element={<ProfileMorePage />} />
        <Route path="/point" element={<Point />} />
        <Route path="/point/success" element={<Success />} />
        <Route path="/point/fail" element={<Success />} />
        <Route path="/my/:category" element={<MyLogPage />} />
        <Route path="*" element={<NotFoundPage />} />
      </Routes>
    </div>
  )
}

export default App
