import { useNavigate } from 'react-router-dom'
import kakao from '../../../public/images/auths/btn_kakao.png'
import logo from '../../../public/images/foodlier_logo.png'
import naver from '../../../public/images/auths/btn_naver.png'
import * as S from '../../styles/auth/LoginPage.styled'

const Login = () => {
  const navigate = useNavigate()

  return (
    <S.Container>
      <S.Title>
        <S.Logo src={logo} alt="Foodlier Logo" />
      </S.Title>
      <S.Form>
        <S.Input type="email" placeholder="Email" />
        <S.Input type="password" placeholder="Password" />
        <S.FindPassword>비밀번호 찾기</S.FindPassword>
        <S.Button
          type="submit"
          onClick={() => {
            navigate('/')
          }}
        >
          로그인
        </S.Button>
        <S.RegisterButton
          onClick={() => {
            navigate('/register')
          }}
        >
          회원가입
        </S.RegisterButton>
      </S.Form>
      <S.Divider>
        <S.Text>OR</S.Text>
      </S.Divider>
      <S.Form>
        <S.SocialLoginButton>
          <S.SocialLogo src={naver} alt="" />
          <span>Sign in With Naver</span>
        </S.SocialLoginButton>
        <S.SocialLoginButton>
          <S.SocialLogo src={kakao} alt="" />
          <span>Sign in With Kakao</span>
        </S.SocialLoginButton>
      </S.Form>
    </S.Container>
  )
}

export default Login
