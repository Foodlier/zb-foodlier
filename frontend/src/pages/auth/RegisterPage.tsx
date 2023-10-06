import { ChangeEvent, useState, useRef } from 'react'
import DaumPostcode from 'react-daum-postcode'
import { rest } from 'msw'
import * as S from '../../styles/auth/RegisterPage.styled'
import upload from '../../assets/profile_upload.svg'
import Modal from '../../components/auth/AddressModal'
import { worker } from '../../mocks/browsers'

interface FormData {
  profileImage: string
  nickname: string
  email: string
  password: string
  passwordCheck: string
  address: string
  detailAddress: string
}

interface FormErrors {
  nickname: string
  email: string
  password: string
  passwordCheck: string
  address: string
  detailAddress: string
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
    address: '',
    detailAddress: '',
  })

  /*
    폼 유효성 검사 관련 상태
  */
  const [formErrors, setFormErrors] = useState<FormErrors>({
    nickname: '',
    email: '',
    password: '',
    passwordCheck: '',
    address: '',
    detailAddress: '',
  })

  /*
    폼 유효성 검사 결과에 따른 버튼 활성화 상태
  */
  // const [isFormValid] = useState(false)

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

  const [isModalOpen, setIsModalOpen] = useState(false)
  const [openPostcode, setOpenPostcode] = useState<boolean>(false)
  const [, setAddress] = useState<string>('')

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
      setFormData({ ...formData, address: data.address })
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

  const saveImgFile = () => {
    const file = imgRef.current?.files?.[0]
    if (file) {
      const reader = new FileReader()
      reader.readAsDataURL(file)
      reader.onloadend = () => {
        setImgFile(reader.result as string)
        formData.profileImage = reader.result as string
      }
    }
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
    } else if (name === 'address') {
      if (value === '') {
        setFormErrors({
          ...formErrors,
          [name]: '주소를 검색해주세요.',
        })
      }
    } else if (name === 'detailAddress') {
      if (value === '') {
        setFormErrors({
          ...formErrors,
          [name]: '상세 주소를 입력해주세요.',
        })
      }
    }
  }

  /*
    회원가입 폼 제출 이벤트
  */
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    try {
      // 회원가입 API 엔드포인트
      const endpoint = '/auth/signup'
      // MSW를 사용하여 모의 서버로 요청을 보냅니다.
      worker.use(
        rest.post(endpoint, (_req, res, ctx) => {
          // 원하는 가짜 응답 데이터를 설정할 수 있습니다.
          return res(
            ctx.status(200),
            ctx.json({ message: '회원가입 성공', data: formData })
          )
        })
      )

      // Axios 대신 fetch 또는 다른 HTTP 클라이언트를 사용하여 요청을 보낼 수 있습니다.
      const response = await fetch(endpoint, {
        method: 'POST',
        body: JSON.stringify(formData),
        headers: {
          'Content-Type': 'application/json',
        },
      })

      // HTTP 응답 코드 및 데이터를 확인합니다.
      if (response.status === 200) {
        console.log('회원가입 성공:', await response.json())
        // 여기에서 필요한 리다이렉션 또는 다른 동작을 수행할 수 있습니다.
      } else {
        console.error('회원가입 실패:', await response.json())
      }
    } catch (error) {
      console.error('회원가입 실패:', error)
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
          <S.InputContainer>
            <S.Profile>
              <S.ProfileImage
                src={typeof imgFile === 'string' ? imgFile : upload}
                alt=""
              />
              <S.FileInput
                type="file"
                accept="image/*"
                id="profileImg"
                onChange={saveImgFile}
                ref={imgRef}
              />
            </S.Profile>
          </S.InputContainer>
          <S.ProfileButton type="button">
            <label htmlFor="profileImg">사진 추가</label>
          </S.ProfileButton>
          <S.InputContainer>
            <S.Input2
              type="text"
              name="nickname"
              placeholder="닉네임을 입력해주세요"
              value={formData.nickname}
              onChange={handleChange}
            />
            <S.DetailButton type="button">중복확인</S.DetailButton>
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
            <S.DetailButton type="button">인증요청</S.DetailButton>
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
            <S.Input2
              type="string"
              disabled
              value={formData.address}
              name="address"
              onChange={handleChange}
            />
            <S.DetailButton type="button" onClick={openModal}>
              주소검색
            </S.DetailButton>
            <S.ErrorMessage>{formErrors.address}</S.ErrorMessage>
          </S.InputContainer>
          <S.Input1
            type="string"
            name="detailAddress"
            placeholder="상세 주소"
            value={formData.detailAddress}
            onChange={handleChange}
          />
          <S.ErrorMessage>{formErrors.detailAddress}</S.ErrorMessage>
          <S.ConfirmButton type="submit">가입하기</S.ConfirmButton>
        </S.Form>
      </S.Container>
    </>
  )
}

export default Register
