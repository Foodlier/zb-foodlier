import { ChangeEvent, useState, useRef, useEffect } from 'react'
import DaumPostcode from 'react-daum-postcode'
import { rest } from 'msw'
import axios from 'axios'
import { useNavigate } from 'react-router-dom'
import * as S from '../../styles/auth/RegisterPage.styled'
import upload from '../../../public/images/profile_upload.svg'
import Modal from '../../components/auth/AddressModal'
import { worker } from '../../mocks/browsers'

interface FormData {
  profileImage: File | null
  nickname: string
  email: string
  verificationCode: string
  password: string
  passwordCheck: string
  address: string
  detailAddress: string
}

interface FormErrors {
  nickname: string
  email: string
  verificationCode: string
  password: string
  passwordCheck: string
  address: string
  detailAddress: string
}

const Register = () => {
  const navigate = useNavigate()
  // 폼 데이터 관련 상태
  const [formData, setFormData] = useState<FormData>({
    profileImage: null as File | null,
    nickname: '',
    email: '',
    verificationCode: '',
    password: '',
    passwordCheck: '',
    address: '',
    detailAddress: '',
  })

  // 폼 유효성 검사 관련 상태
  const [formErrors, setFormErrors] = useState<FormErrors>({
    nickname: '',
    email: '',
    verificationCode: '',
    password: '',
    passwordCheck: '',
    address: '',
    detailAddress: '',
  })

  const [isCodeInput, setIsCodeInput] = useState<boolean>(false)
  const [verificationCode, setVerificationCode] = useState<string>('')

  const sendVerificationEmail = async () => {
    const endpoint = `/auth/verification/send/${formData.email}`
    setIsCodeInput(true)

    axios.post(endpoint).then(res => {
      if (res.data) {
        console.log(res.data.code)
        setVerificationCode(res.data.code)
      }
    })
  }

  /*
    폼 유효성 검사 결과에 따른 버튼 활성화 상태
    정규식은 개발 단계이므로 간단하게 작성하였습니다.
  */
  const [isFormValid, setIsFormValid] = useState(false)

  const validateNickname = (nickname: string) => {
    const nicknameRegex = /^[a-zA-Z0-9]{4,}$/
    return nicknameRegex.test(nickname)
  }

  const validateEmail = (email: string) => {
    const emailRegex = /^[a-zA-Z0-9]+@[a-zA-Z0-9]+\.[a-zA-Z0-9]+$/
    return emailRegex.test(email)
  }

  const validatePassword = (password: string) => {
    const passwordRegex = /^[a-zA-Z0-9]{4,}$/
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
  // const [imgFile, setImgFile] = useState<File | null>(null)
  const imgRef = useRef<HTMLInputElement | null>(null)

  const handleImgFile = (e: ChangeEvent<HTMLInputElement>) => {
    e.preventDefault()
    const imgFormData = new FormData()
    if (e.target.files) {
      imgFormData.append('profileImage', e.target.files[0])
      setFormData({ ...formData, profileImage: e.target.files[0] })
    }
  }
  /*
    폼 입력값 변경 이벤트 및 유효성 검사
   */
  const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target
    setFormData({ ...formData, [name]: value })
    // console.log(formData)

    // 입력값 유효성 검사
    switch (name) {
      case 'nickname':
        if (!validateNickname(value)) {
          setFormErrors({
            ...formErrors,
            [name]: '4자리 이상 입력해주세요.',
          })
        }
        break
      case 'email':
        if (!validateEmail(value)) {
          setFormErrors({
            ...formErrors,
            [name]: '이메일 형식이 올바르지 않습니다.',
          })
        }
        break
      case 'verificationCode':
        if (value != verificationCode) {
          setFormErrors({
            ...formErrors,
            [name]: '인증번호가 일치하지 않습니다.',
          })
        } else {
          setFormErrors({
            ...formErrors,
            [name]: '',
          })
        }
        break
      case 'password':
        if (!validatePassword(value)) {
          setFormErrors({
            ...formErrors,
            [name]: '4자리 이상 입력해주세요.',
          })
        } else {
          setFormErrors({
            ...formErrors,
            [name]: '',
          })
        }
        break
      case 'passwordCheck':
        if (value !== formData.password) {
          setFormErrors({
            ...formErrors,
            [name]: '비밀번호가 일치하지 않습니다.',
          })
        } else {
          setFormErrors({
            ...formErrors,
            [name]: '',
          })
        }
        break
      case 'address':
        if (value === '') {
          setFormErrors({
            ...formErrors,
            [name]: '주소를 검색해주세요.',
          })
        }
        break
      case 'detailAddress':
        if (value === '') {
          setFormErrors({
            ...formErrors,
            [name]: '상세 주소를 입력해주세요.',
          })
        }
        break
      default:
        break
    }
  }

  // 입력값 여부에 따라 버튼 활성화 상태 변경
  useEffect(() => {
    const {
      nickname,
      email,
      verificationCode,
      password,
      passwordCheck,
      address,
      detailAddress,
    } = formData

    if (
      nickname &&
      email &&
      verificationCode &&
      password &&
      passwordCheck &&
      address &&
      detailAddress
    ) {
      setIsFormValid(true)
    } else {
      setIsFormValid(false)
    }
  }, [formData])

  //  회원가입 폼 제출 이벤트
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    try {
      const endpoint = '/api/auth/signup'

      const formDataToSend = new FormData()
      formDataToSend.append('profileImage', formData.profileImage as Blob)
      formDataToSend.append('nickname', formData.nickname)
      formDataToSend.append('email', formData.email)
      formDataToSend.append('password', formData.password)
      formDataToSend.append('address', formData.address)
      formDataToSend.append('detailAddress', formData.detailAddress)

      worker.use(
        rest.post(endpoint, (_req, res, ctx) => {
          return res(ctx.status(200), ctx.json({ message: '회원가입 성공' }))
        })
      )

      // eslint-disable-next-line no-restricted-syntax
      // for (const key of formDataToSend.keys()) {
      //   console.log(key)
      // }
      // console.log('====================================')
      // // eslint-disable-next-line no-restricted-syntax
      // for (const value of formDataToSend.values()) {
      //   console.log(value)
      // }

      // axios를 사용하여 POST 요청을 보냅니다.
      // const response = await axios.post(endpoint, formData, {
      //   headers: {
      //     'Content-Type': 'application/json',
      //   },
      // })
      // Axios 대신 fetch 또는 다른 HTTP 클라이언트를 사용하여 요청을 보낼 수 있습니다.
      const response = await fetch(endpoint, {
        method: 'POST',
        body: JSON.stringify(formDataToSend),
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      })

      // HTTP 응답 코드 및 데이터를 확인합니다.
      if (response.status === 200) {
        console.log('회원가입 성공:', await response.json())
        alert('회원가입 성공')
        navigate('/')
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
        <S.Form onSubmit={handleSubmit} encType="multipart/form-data">
          <S.InputContainer>
            <S.Profile>
              <S.ProfileImage
                src={
                  formData.profileImage
                    ? URL.createObjectURL(formData.profileImage)
                    : upload
                }
                alt=""
              />
              <S.FileInput
                type="file"
                accept="image/*"
                id="profileImg"
                onChange={handleImgFile}
                ref={imgRef}
              />
            </S.Profile>
          </S.InputContainer>
          <S.ProfileButton type="button">
            <label htmlFor="profileImg">사진 추가</label>
          </S.ProfileButton>
          <S.InputContainer>
            <S.Input1
              type="text"
              name="nickname"
              placeholder="닉네임을 입력해주세요"
              value={formData.nickname}
              onChange={handleChange}
            />
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
            <S.DetailButton type="button" onClick={sendVerificationEmail}>
              인증요청
            </S.DetailButton>
            <S.ErrorMessage>{formErrors.email}</S.ErrorMessage>
            {isCodeInput ? (
              <S.Input1
                type="text"
                name="verificationCode"
                placeholder="인증번호를 입력해주세요"
                value={formData.verificationCode}
                onChange={handleChange}
              />
            ) : (
              <div />
            )}
            <S.ErrorMessage>{formErrors.verificationCode}</S.ErrorMessage>
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
          {isFormValid ? (
            <S.ConfirmButton type="submit">가입하기</S.ConfirmButton>
          ) : (
            <S.DisabledButton type="button" disabled>
              가입하기
            </S.DisabledButton>
          )}
        </S.Form>
      </S.Container>
    </>
  )
}

export default Register
