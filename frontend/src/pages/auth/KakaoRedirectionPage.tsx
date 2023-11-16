/* eslint-disable react-hooks/exhaustive-deps */
import { useEffect } from 'react'

import { useRecoilState } from 'recoil'
import Swal from 'sweetalert2'
import { onAuthLoginSuccess } from '../../services/authServices'
import axiosInstance from '../../utils/FetchCall'
import { myInfoState } from '../../store/recoilState'

const KakaoRedirection = () => {
  const authorizationCode = new URL(
    document.location.toString()
  ).searchParams.get('code')

  const [, setProfile] = useRecoilState(myInfoState)

  // 프로필 조회 API
  const getMyProfile = async () => {
    const response = await axiosInstance.get('/api/profile/private')

    if (response.status === 200) {
      setProfile(response.data)
      localStorage.setItem('PROFILE', JSON.stringify(response.data))
    }
  }

  const loginKaKao = async () => {
    try {
      const { data } = await axiosInstance.post('/api/auth/oauth2/kakao', {
        authorizationCode,
      })
      console.log(authorizationCode)
      Swal.fire({
        icon: 'success',
        title: '로그인 성공',
        text: '메인 페이지로 이동합니다.',
        showConfirmButton: false,
        timer: 1500,
      })
      onAuthLoginSuccess(data)
      getMyProfile()
    } catch (error) {
      console.log(error)
      Swal.fire({
        icon: 'error',
        title: '로그인 실패',
        text: '다시 시도해주세요.',
        showConfirmButton: false,
        timer: 1500,
      })
    }
  }

  useEffect(() => {
    if (authorizationCode) loginKaKao()
  }, [authorizationCode])

  return <div>로그인 중입니다.</div>
}

export default KakaoRedirection
