import logo from '../assets/foodlier_logo.png'
import naver from '../assets/btn_naver.png'
import kakao from '../assets/btn_kakao.png'
import * as S from '../styles/LoginPage.styled'

const Login = () => {
  return (
    <S.Container>
      <S.Title>
        <img src={logo} alt="Foodlier Logo" />
      </S.Title>
      <S.Form>
        <S.Input type="email" placeholder="Email" />
        <S.Input type="password" placeholder="Password" />
        <S.Button type="submit">로그인</S.Button>
        <S.RegisterButton>회원가입</S.RegisterButton>
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
