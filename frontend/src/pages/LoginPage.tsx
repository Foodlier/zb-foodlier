import logo from '../assets/logo.svg'
import google from '../assets/google.svg'
import kakao from '../assets/kakao.svg'
import * as S from '../styles/Login.styled'

export default function Login() {
  return (
    <S.Container>
      <S.Title>
        <img src={logo} alt="React Logo" />
      </S.Title>
      <S.Form>
        <S.Input type="email" placeholder="Email" />
        <S.Input type="password" placeholder="Password" />
        <S.Button type="submit">로그인</S.Button>
        <S.RegisterButton>회원가입</S.RegisterButton>
      </S.Form>
      <S.Divider />
      <S.SocialLoginButton>
        <img src={google} alt="" />
        <span>Sign in With Google</span>
      </S.SocialLoginButton>
      <S.SocialLoginButton>
        <img src={kakao} alt="" />
        <span>Sign in With Kakao</span>
      </S.SocialLoginButton>
    </S.Container>
  )
}
