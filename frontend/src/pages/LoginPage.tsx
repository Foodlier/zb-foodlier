import logo from '../assets/foodlier_logo.png'
import naver from '../assets/btn_naver.png'
import kakao from '../assets/btn_kakao.png'
import * as L from '../styles/LoginPage.styled'

const Login = () => {
  return (
    <L.Container>
      <L.Title>
        <img src={logo} alt="Foodlier Logo" />
      </L.Title>
      <L.Form>
        <L.Input type="email" placeholder="Email" />
        <L.Input type="password" placeholder="Password" />
        <L.Button type="submit">로그인</L.Button>
        <L.RegisterButton>회원가입</L.RegisterButton>
      </L.Form>
      <L.Divider>
        <L.Text>OR</L.Text>
      </L.Divider>
      <L.Form>
        <L.SocialLoginButton>
          <L.SocialLogo src={naver} alt="" />
          <span>Sign in With Naver</span>
        </L.SocialLoginButton>
        <L.SocialLoginButton>
          <L.SocialLogo src={kakao} alt="" />
          <span>Sign in With Kakao</span>
        </L.SocialLoginButton>
      </L.Form>
    </L.Container>
  )
}

export default Login
