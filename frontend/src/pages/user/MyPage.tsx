/* eslint-disable react-hooks/exhaustive-deps */
import { useRecoilState } from 'recoil'
import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import * as S from '../../styles/user/MyPage.styled'
import BottomNavigation from '../../components/BottomNavigation'
import Header from '../../components/Header'
import useIcon from '../../hooks/useIcon'
import { palette } from '../../constants/Styles'
import ModalWithTwoButton from '../../components/ui/ModalWithTwoButton'
import axiosInstance, { reissueToken } from '../../utils/FetchCall'
import { myInfoState } from '../../store/recoilState'
import ModalWithoutButton from '../../components/ui/ModalWithoutButton'
import defaultProfile from '../../../public/images/default_profile.png'
import { removeCookie, setCookie } from '../../utils/Cookies'
import ChargeModal from '../../components/point/ChargeModal'

interface MenuListInterface {
  title: string
  navigate?: string
  isMargin?: boolean
  onClick?: () => void
}

const MyPage = () => {
  const navigate = useNavigate()
  const { IcExpandRight } = useIcon()

  const [profile, setProfile] = useRecoilState(myInfoState)
  const [isChefModal, setIsChefModal] = useState(false)
  const [isChefCompleteModal, setIsChefCompleteModal] = useState(false)
  const [chefCompleteContent, setChefCompleteContent] = useState('')
  const [isLogoutModal, setIsLogoutModal] = useState(false)
  const [isWithdrawModal, setIsWithdrawModal] = useState(false)
  const [isChargeModal, setIsChargeModal] = useState(false)

  const MENU_LIST = [
    {
      title: '내가 작성한 글',
      navigate: '/my/recipe',
    },
    {
      title: '내가 작성한 댓글',
      navigate: '/my/comment',
    },
    {
      title: '내가 좋아요 한 글',
      navigate: '/my/like',
      isMargin: true,
    },
    {
      title: '내 거래 내역',
      navigate: '/my/trade-history',
    },
    {
      title: '내 충전 내역',
      navigate: '/my/charge-history',
    },
    {
      title: '내 정보 수정',
      navigate: '/my/edit',
    },
    {
      title: '비밀번호 변경',
      navigate: '/my/edit/password',
    },
    {
      title: '로그아웃',
      onClick: () => {
        setIsLogoutModal(true)
      },
    },
    {
      title: '탈퇴하기',
      onClick: () => {
        // 비밀번호 변경 모달을 열거나 다른 동작을 수행
        setIsWithdrawModal(true)
      },
    },
  ]

  // 마이페이지 메뉴 클릭 시 호출
  // navigate or event
  const handleMenuItemClick = (menuItem: MenuListInterface) => {
    if (menuItem.navigate) {
      navigate(menuItem.navigate)
    } else if (menuItem.onClick) {
      menuItem.onClick()
    }
  }

  // 프로필 조회 API
  const getMyProfile = async () => {
    const response = await axiosInstance.get('/api/profile/private')
    console.log(response)
    if (response.status === 200) {
      setProfile(response.data)
      localStorage.setItem('PROFILE', JSON.stringify(response.data))
    }
  }

  // 요리사 등록 API
  const postChef = async () => {
    try {
      const body = {
        introduce: '잘 부탁드립니다.',
      }
      const response = await axiosInstance.post(
        '/api/profile/private/register-chef',
        body
      )
      console.log(response)
      setChefCompleteContent('요리사 등록이 완료되었습니다.')
      // 요리사 등록 시 refresh token으로 토큰 재발급 후 새로운 토큰으로 저장해야 한다.
      const NEW_TOKEN = reissueToken()
      removeCookie('accessToken')
      setCookie('accessToken', JSON.stringify(NEW_TOKEN), { path: '/' })
    } catch (error) {
      console.log(error)
      setChefCompleteContent('작성한 게시물이 3개 이하입니다.')
    }
    setIsChefModal(false)
    setIsChefCompleteModal(true)
    setTimeout(() => {
      setIsChefCompleteModal(false)
    }, 1500)
  }

  console.log(profile)

  useEffect(() => {
    getMyProfile()
  }, [])

  return (
    <>
      <Header />
      <S.Container>
        <S.ProfileButton
          onClick={() => navigate(`/profile/${profile?.myMemberId}`)}
        >
          <S.ProfileImage src={profile?.profileUrl || defaultProfile} />
          <S.PrifileInfo>
            {profile?.isChef && <S.ChefBadge>요리사</S.ChefBadge>}
            <S.Nickname>{profile?.nickName}</S.Nickname>
            <S.Email>{profile?.email}</S.Email>
          </S.PrifileInfo>
          <IcExpandRight size={3.5} color={palette.textPrimary} />
        </S.ProfileButton>
        <S.WrapMyPoint>
          <S.PointDetailButton>
            <S.PointIcon>P</S.PointIcon>내 포인트
            {/* <IcArrowDropRight size={2.5} color={palette.textPrimary} /> */}
          </S.PointDetailButton>
          <S.PointChargeButton>
            <S.ChargeBadge onClick={() => setIsChargeModal(true)}>
              충전
            </S.ChargeBadge>
            {profile?.point} P
          </S.PointChargeButton>
        </S.WrapMyPoint>
        <S.NavigateButtonList>
          {!profile?.isChef && (
            <S.NavigateButton onClick={() => setIsChefModal(true)}>
              🧑🏻‍🍳 요리사 신청하기
            </S.NavigateButton>
          )}
          {MENU_LIST.map(item => (
            <S.NavigateButton
              key={item.title}
              onClick={() => handleMenuItemClick(item)}
              $isMargin={item.isMargin || false}
            >
              {item.title}
            </S.NavigateButton>
          ))}
        </S.NavigateButtonList>
      </S.Container>

      {/* 요리사 등록 Modal */}
      {isChefModal && (
        <ModalWithTwoButton
          content="요리사 등록을 하시겠습니까?"
          subContent="요리사 등록을 위해서는 게시물 3개 이상 등록이 필요합니다."
          setIsModalFalse={() => setIsChefModal(false)}
          modalEvent={postChef}
        />
      )}

      {/* 요리사 등록 후 노출 Modal */}
      {isChefCompleteModal && (
        <ModalWithoutButton
          content={chefCompleteContent}
          setIsModalFalse={() => setIsChefModal(false)}
        />
      )}

      {/* 로그아웃 Modal */}
      {isLogoutModal && (
        <ModalWithTwoButton
          content="정말 로그아웃하시겠어요?"
          setIsModalFalse={() => setIsLogoutModal(false)}
          modalEvent={() => {
            localStorage.removeItem('accessToken')
            removeCookie('accessToken')
            removeCookie('refreshToken')
            navigate('/login')
          }}
        />
      )}

      {/* 탈퇴 Modal */}
      {isWithdrawModal && (
        <ModalWithTwoButton
          content="정말 탈퇴하시겠어요?"
          subContent="탈퇴 버튼 선택 시, 계정은
      삭제되며 복구되지 않습니다."
          setIsModalFalse={() => setIsWithdrawModal(false)}
          modalEvent={() => console.log('탈퇴하기 API 연동')}
        />
      )}

      {/* 충전 Modal */}
      {isChargeModal && <ChargeModal setIsChargeModal={setIsChargeModal} />}
      <BottomNavigation />
    </>
  )
}

export default MyPage
