/* eslint-disable import/no-extraneous-dependencies */
import { ChangeEvent, useState, useRef, useEffect } from 'react'
import DaumPostcode from 'react-daum-postcode'
import axios from 'axios'
import { useNavigate } from 'react-router-dom'
import Swal from 'sweetalert2'
import * as S from '../../styles/auth/RegisterPage.styled'
import upload from '../../../public/images/profile_upload.svg'
import Modal from '../../components/auth/AddressModal'
import {
  validateEmail,
  validateNickname,
  validatePassword,
  validatePasswordConfirm,
  validatePhoneNumber,
} from '../../utils/FormValidation'

interface FormData {
  profileImage: File | null
  nickname: string
  email: string
  verificationCode: string
  phoneNumber: string
  password: string
  passwordCheck: string
  roadAddress: string
  addressDetail: string
}

interface FormErrors {
  nickname: string
  email: string
  verificationCode: string
  phoneNumber: string
  password: string
  passwordCheck: string
  roadAddress: string
  addressDetail: string
}

const Register = () => {
  const navigate = useNavigate()
  // 폼 데이터 관련 상태
  const [formData, setFormData] = useState<FormData>({
    profileImage: null,
    nickname: '',
    email: '',
    verificationCode: '',
    phoneNumber: '',
    password: '',
    passwordCheck: '',
    roadAddress: '',
    addressDetail: '',
  })

  // 폼 유효성 검사 관련 상태
  const [formErrors, setFormErrors] = useState<FormErrors>({
    nickname: '',
    email: '',
    verificationCode: '',
    phoneNumber: '',
    password: '',
    passwordCheck: '',
    roadAddress: '',
    addressDetail: '',
  })

  const [isCodeInput, setIsCodeInput] = useState<boolean>(false)
  // const [verificationCode, setVerificationCode] = useState<string>('')

  // TODO : 인증번호 전송 API 호출 후 로딩바 또는 로딩 텍스트 표시
  const sendVerificationEmail = async () => {
    setIsCodeInput(true)
    // API : 인증번호 전송 요청
    axios
      .post(`/api/auth/verification/send/${formData.email}`)
      .then(function () {
        Swal.fire({
          icon: 'success',
          title: '인증번호가 전송되었습니다.',
          text: '이메일을 확인해주세요.',
        })
      })
      .catch(err => {
        const { errorCode } = err.response.data
        if (errorCode === 'EMAIL_IS_ALREADY_EXIST') {
          Swal.fire({
            icon: 'error',
            title: '이미 사용 중인 이메일 입니다.',
            text: '다른 이메일을 입력해주세요.',
          })
        } else if (errorCode === 'NICKNAME_IS_ALREADY_EXIST') {
          Swal.fire({
            icon: 'error',
            title: '이미 사용 중인 닉네임 입니다.',
            text: '다른 닉네임을 입력해주세요.',
          })
        } else if (errorCode === 'PHONE_NUMBER_IS_ALREADY_EXIST') {
          Swal.fire({
            icon: 'error',
            title: '이미 사용 중인 휴대폰 번호 입니다.',
            text: '다른 휴대폰 번호를 입력해주세요.',
          })
        }
      })
  }

  const [isFormValid, setIsFormValid] = useState(false)

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
      setFormData({ ...formData, roadAddress: data.address })
    },
  }
  const openModal = () => {
    setIsModalOpen(true)
    handleOpenPostcode.clickButton()
  }
  const closeModal = () => {
    setIsModalOpen(false)
  }

  // 이미지 업로드 관련 상태, 이벤트
  const imgRef = useRef<HTMLInputElement | null>(null)

  const handleImgFile = (e: ChangeEvent<HTMLInputElement>) => {
    e.preventDefault()
    const imgFormData = new FormData()
    if (e.target.files) {
      imgFormData.append('profileImage', e.target.files[0])
      setFormData({ ...formData, profileImage: e.target.files[0] })
    }
  }

  // 폼 입력값 변경 이벤트 및 유효성 검사
  // TODO : React 18 -> useDeferredValue 등 사용하여 성능 최적화
  const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target
    setFormData({ ...formData, [name]: value })
    setFormErrors({ ...formErrors, [name]: '' })

    // 입력값 유효성 검사
    switch (name) {
      case 'nickname':
        if (!validateNickname(value)) {
          setFormErrors({
            ...formErrors,
            [name]: '2~8자로 입력해주세요',
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
        if (value === '') {
          setFormErrors({
            ...formErrors,
            [name]: '인증번호를 입력해주세요.',
          })
        } else {
          setFormErrors({
            ...formErrors,
            [name]: '',
          })
        }
        break
      case 'phoneNumber':
        if (value === '') {
          setFormErrors({
            ...formErrors,
            [name]: '휴대폰 번호를 입력해주세요.',
          })
        } else if (value.includes('-')) {
          setFormErrors({
            ...formErrors,
            [name]: '하이픈(-)을 제외하고 입력해주세요.',
          })
        } else if (!validatePhoneNumber(value)) {
          setFormErrors({
            ...formErrors,
            [name]: '휴대폰 번호 형식이 올바르지 않습니다.',
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
            [name]: '8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.',
          })
        } else if (formData.passwordCheck !== '' && value === '') {
          formData.passwordCheck = ''
          setFormErrors({
            ...formErrors,
            [name]: '비밀번호를 입력해주세요.',
          })
        } else {
          setFormErrors({
            ...formErrors,
            [name]: '',
          })
        }
        break
      case 'passwordCheck':
        if (!validatePasswordConfirm(formData.password, value)) {
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
      case 'roadAddress':
        if (value === '') {
          setFormErrors({
            ...formErrors,
            [name]: '주소를 검색해주세요.',
          })
        }
        break
      case 'addressDetail':
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

  const handleVerificationCode = () => {
    // API : 확인 버튼 누르면 인증번호 일치 여부 확인
    axios
      .post(
        `/api/auth/verify`,
        {},
        {
          params: {
            email: formData.email,
            verificationCode: formData.verificationCode,
          },
        }
      )
      .then(function () {
        Swal.fire({
          icon: 'success',
          title: '인증되었습니다.',
          text: '이메일 인증이 완료되었습니다.',
        })
      })
      .catch(function () {
        Swal.fire({
          icon: 'error',
          title: '인증번호가 일치하지 않습니다.',
          text: '인증번호를 다시 확인해주세요.',
        })
      })
  }

  // 입력값 여부에 따라 버튼 활성화 상태 변경
  useEffect(() => {
    const {
      nickname,
      email,
      verificationCode,
      password,
      passwordCheck,
      phoneNumber,
      roadAddress,
      addressDetail,
    } = formData

    if (
      nickname &&
      email &&
      verificationCode &&
      password &&
      passwordCheck &&
      phoneNumber &&
      roadAddress &&
      addressDetail
    ) {
      setIsFormValid(true)
    } else {
      setIsFormValid(false)
    }
  }, [formData])

  //  회원가입 폼 제출 이벤트
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()

    if (formData.password !== formData.passwordCheck) {
      Swal.fire({
        icon: 'error',
        title: '비밀번호가 일치하지 않습니다.',
        text: '비밀번호를 다시 확인해주세요.',
      })
      return
    }

    const endpoint = '/api/auth/signup'

    const formDataToSend = new FormData()
    formDataToSend.append('addressDetail', formData.addressDetail)
    formDataToSend.append('email', formData.email)
    formDataToSend.append('nickname', formData.nickname)
    formDataToSend.append('password', formData.password)
    formDataToSend.append('phoneNumber', formData.phoneNumber)
    // 이미지 파일이 없을 경우 FormData에서 삭제 후 백엔드에 null 타입으로 전송
    if (formData.profileImage === null) {
      formDataToSend.append('profileImage', '')
      formDataToSend.delete('profileImage')
    } else {
      formDataToSend.append('profileImage', formData.profileImage as Blob)
    }
    formDataToSend.append('roadAddress', formData.roadAddress)

    // API : 회원가입 요청
    axios
      .post(endpoint, formDataToSend, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      })
      .then(function () {
        Swal.fire({
          icon: 'success',
          title: '회원가입이 완료되었습니다.',
          text: '로그인 페이지로 이동합니다.',
          timer: 1500,
        })
        navigate('/login')
      })
      .catch(err => {
        const { errorCode } = err.response.data
        if (errorCode === 'EMAIL_IS_ALREADY_EXIST') {
          Swal.fire({
            icon: 'error',
            title: '이미 사용 중인 이메일 입니다.',
            text: '다른 이메일을 입력해주세요.',
          })
        } else if (errorCode === 'NICKNAME_IS_ALREADY_EXIST') {
          Swal.fire({
            icon: 'error',
            title: '이미 사용 중인 닉네임 입니다.',
            text: '다른 닉네임을 입력해주세요.',
          })
        } else if (errorCode === 'PHONE_NUMBER_IS_ALREADY_EXIST') {
          Swal.fire({
            icon: 'error',
            title: '이미 사용 중인 휴대폰 번호 입니다.',
            text: '다른 휴대폰 번호를 입력해주세요.',
          })
        }
      })
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
              <>
                <S.Input2
                  type="text"
                  name="verificationCode"
                  placeholder="인증번호를 입력해주세요"
                  value={formData.verificationCode}
                  onChange={handleChange}
                />
                <S.DetailButton type="button" onClick={handleVerificationCode}>
                  확인
                </S.DetailButton>
              </>
            ) : (
              <div />
            )}
            <S.ErrorMessage>{formErrors.verificationCode}</S.ErrorMessage>
            <S.InputContainer>
              <S.Input1
                type="string"
                name="phoneNumber"
                placeholder="휴대폰 번호"
                value={formData.phoneNumber}
                onChange={handleChange}
              />
              <S.ErrorMessage>{formErrors.phoneNumber}</S.ErrorMessage>
            </S.InputContainer>
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
              value={formData.roadAddress}
              name="address"
              onChange={handleChange}
            />
            <S.DetailButton type="button" onClick={openModal}>
              주소검색
            </S.DetailButton>
            <S.ErrorMessage>{formErrors.roadAddress}</S.ErrorMessage>
          </S.InputContainer>
          <S.Input1
            type="string"
            name="addressDetail"
            placeholder="상세 주소"
            value={formData.addressDetail}
            onChange={handleChange}
          />
          <S.ErrorMessage>{formErrors.addressDetail}</S.ErrorMessage>
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
