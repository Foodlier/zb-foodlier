/* eslint-disable @typescript-eslint/no-use-before-define */
import axios from 'axios'
import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import kakao from '../../../public/images/auths/btn_kakao.png'
import logo from '../../../public/images/foodlier_logo.png'
import naver from '../../../public/images/auths/btn_naver.png'
import * as S from '../../styles/auth/LoginPage.styled'
import { onLoginSuccess } from '../../services/authServices'
// import AuthPageModal from '../../components/auth/AuthPageModal'

const Login = () => {
  const navigate = useNavigate()

  const [userInputs, setUserInputs] = useState({
    currentDate: '2023-11-16T12:05:39.912Z',
    email: 'bos3321@gmail.com',
    password: 'bJ1Eb9QW4jDZerYkjrqD2NQZOvPHNc',
  })

  const [isModalOpen, setIsModalOpen] = useState(false)

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target
    setUserInputs({
      ...userInputs,
      [name]: value,
    })
  }

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault()
    axios
      .post('/api/auth/signin', userInputs)
      .then(res => {
        onLoginSuccess(res)
        setIsModalOpen(true)
        navigate('/')
      })
      .catch(err => {
        if (err.response) {
          if (err.response.status === 400) {
            alert('아이디 또는 비밀번호가 일치하지 않습니다.')
          } else if (err.response.status === 401) {
            alert('이메일 인증을 완료해주세요.')
          } else {
            console.error(err) // 다른 오류 상황에 대한 에러 메시지 출력
          }
        } else {
          console.error(err) // 네트워크 오류 등에 대한 에러 메시지 출력
        }
      })
  }

  return (
    <S.Container>
      {/* {isModalOpen && (
        <AuthPageModal closeModal={() => setIsModalOpen(false)} />
      )} */}
      {/* <AuthPageModal closeModal={() => setIsModalOpen(false)} /> */}
      <S.Title>
        <S.Logo src={logo} alt="Foodlier Logo" />
      </S.Title>
      <S.Form onSubmit={handleSubmit}>
        <S.Input
          type="email"
          placeholder="Email"
          value={userInputs.email}
          onChange={handleChange}
        />
        <S.Input
          type="password"
          placeholder="Password"
          value={userInputs.password}
          onChange={handleChange}
        />
        <S.FindPassword>
          <button
            type="button"
            onClick={() => {
              navigate('/find-password')
            }}
          >
            비밀번호 찾기
          </button>
        </S.FindPassword>
        <S.Button type="submit">로그인</S.Button>
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
