import { useNavigate } from 'react-router-dom'
import axios from 'axios'
import { useState, useEffect } from 'react'
import logo from '../../assets/foodlier_logo.png'
import naver from '../../assets/btn_naver.png'
import kakao from '../../assets/btn_kakao.png'
import * as S from '../../styles/auth/LoginPage.styled'

interface ResType {
  id: string
  name: string
  country: string
  language: string
}

const Login = () => {
  const [peopleData, setPeopleData] = useState<ResType[]>([])
  const navigate = useNavigate()

  useEffect(() => {
    const fetchData = async () => {
      try {
        const res = await axios({
          method: 'get',
          url: '/',
        })
        console.log(res.data)
        if (res.status === 200) {
          setPeopleData(res.data)
        }
      } catch (error) {
        console.log(error)
      }
    }
    fetchData()
  }, [])

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
