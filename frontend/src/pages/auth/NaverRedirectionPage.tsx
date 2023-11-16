/* eslint-disable react/jsx-no-useless-fragment */
/* eslint-disable react-hooks/exhaustive-deps */
import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { onAuthLoginSuccess } from '../../services/authServices'
import axiosInstance from '../../utils/FetchCall'
import ModalWithoutButton from '../../components/ui/ModalWithoutButton'

const NaverRedirection = () => {
  const authorizationCode = new URL(
    document.location.toString()
  ).searchParams.get('code')
  const state = 'false'
  const navigate = useNavigate()

  const [modalContent, setModalContent] = useState('')
  const [isModal, setIsModal] = useState(false)

  const switchModal = () => {
    setIsModal(true)
    setTimeout(() => {
      setIsModal(false)
      navigate(-1)
    }, 1500)
  }

  const loginNaver = async () => {
    try {
      const { data } = await axiosInstance.post('/api/auth/oauth2/naver', {
        authorizationCode,
        state,
      })

      // 일반 계정으로 가입 된 계정
      if (data.registrationType === 'DOMAIN') {
        switchModal()
        setModalContent('일반 계정으로 가입 된 계정입니다.')
      }
      // 카카오 계정으로 가입 된 계정
      else if (data.registrationType === 'KAKAO') {
        switchModal()
        setModalContent('카카오 계정으로 가입 된 계정입니다.')
      }
      // 네이버 계정으로 정상 로그인
      else {
        onAuthLoginSuccess(data)
        navigate('/')
      }
    } catch (error) {
      console.log(error)
      switchModal()
      setModalContent('로그인에 실패하였습니다.')
    }
  }

  useEffect(() => {
    loginNaver()
  }, [authorizationCode])

  return (
    <>
      {isModal && (
        <ModalWithoutButton
          content={modalContent}
          setIsModalFalse={() => setIsModal(false)}
        />
      )}
    </>
  )
}

export default NaverRedirection
