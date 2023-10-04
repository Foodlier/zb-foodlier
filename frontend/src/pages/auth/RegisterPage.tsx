import { ChangeEvent, FormEvent, useState, useRef } from 'react'
import DaumPostcode from 'react-daum-postcode'
import * as S from '../../styles/auth/RegisterPage.styled'
import upload from '../../assets/profile_upload.svg'
import Modal from '../../components/auth/AddressModal'

interface FormData {
  profileImage: string
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
  /*
    폼 데이터 관련 상태
  */
  const [formData, setFormData] = useState<FormData>({
    profileImage: '',
    nickname: '',
    email: '',
    password: '',
    passwordCheck: '',
  })

  /*
    폼 유효성 검사 관련 상태
  */
  const [formErrors, setFormErrors] = useState<FormErrors>({
    nickname: '',
    email: '',
    password: '',
    passwordCheck: '',
  })

  /*
    폼 유효성 검사 결과에 따른 버튼 활성화 상태
  */
  const [isFormValid] = useState(false)

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

  /*
    폼 입력값 변경 이벤트 및 유효성 검사
   */
  const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target
    setFormData({ ...formData, [name]: value })
    setFormErrors({ ...formErrors, [name]: '' })
    console.log(formData)

    /*
      입력값 유효성 검사
    */
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
    } else if (name === 'passwordCheck') {
      if (formData.password !== value) {
        setFormErrors({
          ...formErrors,
          [name]: '비밀번호가 일치하지 않습니다.',
        })
      }
    }
  }

  const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault()
  }

  const [isModalOpen, setIsModalOpen] = useState(false)
  const [openPostcode, setOpenPostcode] = useState<boolean>(false)
  const [address, setAddress] = useState<string>('')

  /*
    주소검색 모달창 관련 상태, 이벤트
  */
  const handleOpenPostcode = {
    clickButton: () => {
      setOpenPostcode(current => !current)
    },
    selectAddress: (data: any) => {
      setAddress(data.address)
      setOpenPostcode(false)
      setIsModalOpen(false)
    },
  }
  const openModal = () => {
    setIsModalOpen(true)
    handleOpenPostcode.clickButton()
  }
  const closeModal = () => {
    setIsModalOpen(false)
  }

  /*
   이미지 업로드 관련 상태, 이벤트
  */

  const [imgFile, setImgFile] = useState<string | ArrayBuffer | null>(null)
  const imgRef = useRef<HTMLInputElement | null>(null)

  // 이미지 업로드 input의 onChange
  const saveImgFile = () => {
    const file = imgRef.current?.files?.[0]
    if (file) {
      const reader = new FileReader()
      reader.readAsDataURL(file)
      reader.onloadend = () => {
        setImgFile(reader.result as string)
      }
    }
  }

  return (
    <>
      <Modal
        isOpen={isModalOpen}
        onClose={closeModal}
        title="주소 검색"
        content={
          openPostcode && (
            <DaumPostcode
              onComplete={handleOpenPostcode.selectAddress} // 값을 선택할 경우 실행되는 이벤트
              autoClose={false} // 값을 선택할 경우 사용되는 DOM을 제거하여 자동 닫힘 설정
              defaultQuery="광교마을로 156" // 팝업을 열때 기본적으로 입력되는 검색어
            />
          )
        }
      />

      <S.Container>
        <S.Title>회원가입</S.Title>
        <S.Form onSubmit={handleSubmit}>
          <S.Profile>
            <S.ProfileImage
              src={typeof imgFile === 'string' ? imgFile : upload}
              alt=""
            />
            <label htmlFor="profileImg">사진 추가</label>
            <S.FileInput
              type="file"
              accept="image/*"
              id="profileImg"
              onChange={saveImgFile}
              ref={imgRef}
            />
          </S.Profile>
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
            <S.Input2 type="string" disabled value={address} />
            <S.DetailButton onClick={openModal}>주소검색</S.DetailButton>
          </S.InputContainer>
          <S.Input1 type="email" placeholder="상세 주소" />
          <S.ConfirmButton type="submit" disabled={!isFormValid}>
            가입하기
          </S.ConfirmButton>
        </S.Form>
      </S.Container>
    </>
  )
}

export default Register
