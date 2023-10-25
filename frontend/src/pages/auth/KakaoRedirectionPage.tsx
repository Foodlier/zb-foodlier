/* eslint-disable react-hooks/exhaustive-deps */
import { useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import Swal from 'sweetalert2'
import { onAuthLoginSuccess } from '../../services/authServices'
import axiosInstance from '../../utils/FetchCall'

const KakaoRedirection = () => {
  const authorizationCode = new URL(
    document.location.toString()
  ).searchParams.get('code')
  const navigate = useNavigate()

  useEffect(() => {
    axiosInstance
      .post('/api/auth/oauth2/kakao', {
        authorizationCode,
      })
      .then(res => {
        Swal.fire({
          icon: 'success',
          title: '로그인 성공',
          text: '메인 페이지로 이동합니다.',
          showConfirmButton: false,
          timer: 1500,
        })
        onAuthLoginSuccess(res)
        navigate('/')
      })
      .catch(() => {
        Swal.fire({
          icon: 'error',
          title: '로그인 실패',
          text: '다시 시도해주세요.',
          showConfirmButton: false,
          timer: 1500,
        })
      })
  }, [authorizationCode])

  return <div>로그인 중입니다.</div>
}

export default KakaoRedirection
