import { ChangeEvent, FormEvent, useState } from 'react'
import * as S from '../styles/RegisterPage.styled'
import upload from '../assets/profile_upload.svg'

interface FormData {
  nickname: string
  email: string
  password: string
  passwordCheck: string
}

interface FormErrors {
  nickname: string
  email: string
  password: string
  passwordCheck: string
}

const Register = () => {
  const [formData, setFormData] = useState<FormData>({
    nickname: '',
    email: '',
    password: '',
    passwordCheck: '',
  })

  const [formErrors, setFormErrors] = useState<FormErrors>({
    nickname: '',
    email: '',
    password: '',
    passwordCheck: '',
  })

  const [isFormValid, setIsFormValid] = useState(false)

  const validateNickname = (nickname: string) => {
    const nicknameRegex = /^[가-힣a-zA-Z0-9]{4,10}$/
    return nicknameRegex.test(nickname)
  }

  const validateEmail = (email: string) => {
    const emailRegex = /^[a-zA-Z0-9]+@[a-zA-Z0-9]+\.[a-zA-Z0-9]+$/
    return emailRegex.test(email)
  }

  const validatePassword = (password: string) => {
    const passwordRegex = /^[A-Za-z0-9!@#$%^&*()_+{}[\]:;<>,.?~\\-]{8,16}$/
    return passwordRegex.test(password)
  }

  const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target
    setFormData({ ...formData, [name]: value })
    setFormErrors({ ...formErrors, [name]: '' })

    if (name === 'nickname') {
      if (!validateNickname(value)) {
        setFormErrors({
          ...formErrors,
          [name]: '4~10자의 한글, 영문, 숫자만 사용 가능합니다.',
        })
      }
    } else if (name === 'email') {
      if (!validateEmail(value)) {
        setFormErrors({
          ...formErrors,
          [name]: '이메일 형식이 올바르지 않습니다.',
        })
      }
    } else if (name === 'password') {
      if (!validatePassword(value)) {
        setFormErrors({
          ...formErrors,
          [name]: '8~16자의 영문, 숫자, 특수문자를 사용하세요.',
        })
      }
    }
  }

  const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault()
    // 여기에서 데이터 전송 또는 처리를 수행할 수 있습니다.
  }

  return (
    <S.Container>
      <S.Title>회원가입</S.Title>
      <S.Profile>
        <S.ProfileImage src={upload} alt="" />
        <p>사진 업로드</p>
      </S.Profile>
      <S.Form onSubmit={handleSubmit}>
        <S.InputContainer>
          <S.Input2
            type="text"
            name="nickname"
            placeholder="닉네임을 입력해주세요"
            value={formData.nickname}
            onChange={handleChange}
          />
          <S.DetailButton>중복확인</S.DetailButton>
          <S.ErrorMessage>{formErrors.nickname}</S.ErrorMessage>
        </S.InputContainer>
        <S.InputContainer>
          <S.Input2
            type="email"
            name="email"
            placeholder="이메일을 입력해주세요"
            value={formData.email}
            onChange={handleChange}
          />
          <S.DetailButton>인증요청</S.DetailButton>
          <S.ErrorMessage>{formErrors.email}</S.ErrorMessage>
        </S.InputContainer>
        <S.InputContainer>
          <S.Input1
            type="password"
            name="password"
            placeholder="비밀번호"
            value={formData.password}
            onChange={handleChange}
          />
          <S.ErrorMessage>{formErrors.password}</S.ErrorMessage>
        </S.InputContainer>
        <S.InputContainer>
          <S.Input1
            type="password"
            name="passwordCheck"
            placeholder="비밀번호 재입력"
            value={formData.passwordCheck}
            onChange={handleChange}
          />
          <S.ErrorMessage>{formErrors.passwordCheck}</S.ErrorMessage>
        </S.InputContainer>
        <S.InputContainer>
          <S.Input2 type="email" disabled />
          <S.DetailButton>주소검색</S.DetailButton>
        </S.InputContainer>
        <S.Input1 type="email" placeholder="상세 주소" />
        <S.ConfirmButton type="submit" disabled={!isFormValid}>
          가입하기
        </S.ConfirmButton>
      </S.Form>
    </S.Container>
  )
}

export default Register
