/* eslint-disable import/no-extraneous-dependencies */
// 비밀번호 찾기 스타일 컴포넌트
import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import Swal from 'sweetalert2'
import * as S from '../../styles/auth/FindPasswordPage.styled'
import axiosInstance from '../../utils/FetchCall'
import logo from '../../../public/images/foodlier_logo.png'
import { palette } from '../../constants/Styles'

interface UserInputs {
  phoneNumber: string
  email: string
}

const FindPasswordPage = () => {
  const navigate = useNavigate()
  const [userInputs, setUserInputs] = useState<UserInputs>({
    phoneNumber: '',
    email: '',
  })

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target
    setUserInputs({
      ...userInputs,
      [name]: value,
    })
  }

  const handleSubmitFindPassword = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault()
    axiosInstance
      .post('/api/auth/findPassword', userInputs)
      .then(res => {
        Swal.fire({
          icon: 'success',
          title: '이메일로 비밀번호를 전송했습니다.',
          text: '이메일을 확인해주세요.',
          confirmButtonColor: `${palette.main}`,
        })
        console.log(res)
      })
      .catch(err => {
        const { errorCode } = err.response.data
        if (errorCode === 'MEMBER_NOT_FOUND') {
          Swal.fire({
            icon: 'error',
            title: '회원 정보가 없습니다.',
            text: '회원가입을 해주세요.',
            confirmButtonColor: `${palette.main}`,
          })
          return
        }
        console.log(err)
      })
  }

  return (
    <S.Container>
      <S.Logo src={logo} alt="Foodlier Logo" />
      <S.Title>비밀번호 찾기</S.Title>
      <S.Form onSubmit={handleSubmitFindPassword}>
        <S.InputBox>
          <S.Label htmlFor="phoneNumber">핸드폰 번호</S.Label>
          <S.Input
            id="phoneNumber"
            type="phoneNumber"
            name="phoneNumber"
            value={userInputs.phoneNumber}
            onChange={handleChange}
          />
        </S.InputBox>
        <S.InputBox>
          <S.Label htmlFor="email">이메일</S.Label>
          <S.Input
            id="email"
            type="email"
            name="email"
            value={userInputs.email}
            onChange={handleChange}
          />
          <S.FindPasswordButton type="submit">
            비밀번호 찾기
          </S.FindPasswordButton>
          <S.HomeButton
            type="button"
            onClick={() => {
              navigate('/login')
            }}
          >
            홈으로
          </S.HomeButton>
        </S.InputBox>
      </S.Form>
    </S.Container>
  )
}

export default FindPasswordPage
