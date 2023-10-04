import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import * as S from '../../styles/user/MyPage.styled'
import BottomNavigation from '../../components/BottomNavigation'
import Header from '../../components/Header'
import useIcon from '../../hooks/useIcon'
import { palette } from '../../constants/Styles'
import ModalWithTwoButton from '../../components/ui/ModalWithTwoButton'

interface MenuListInterface {
  title: string
  navigate?: string
  isMargin?: boolean
  onClick?: () => void
}

const MyPage = () => {
  const navigate = useNavigate()
  const { IcExpandRight, IcArrowDropRight } = useIcon()

  const [isLogoutModal, setIsLogoutModal] = useState(false)
  const [isWithdrawModal, setIsWithdrawModal] = useState(false)

  const MENU_LIST = [
    {
      title: '내가 작성한 글',
      navigate: '',
    },
    {
      title: '내가 작성한 댓글',
      navigate: '',
    },
    {
      title: '내가 좋아요 한 글',
      navigate: '',
      isMargin: true,
    },
    {
      title: '내 정보 수정',
      navigate: '',
    },
    {
      title: '비밀번호 변경',
      navigate: '',
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

  const handleMenuItemClick = (menuItem: MenuListInterface) => {
    if (menuItem.navigate) {
      navigate(menuItem.navigate)
    } else if (menuItem.onClick) {
      menuItem.onClick()
    }
  }

  return (
    <>
      <Header />
      <S.Container>
        <S.ProfileButton>
          <S.ProfileImage />
          <S.PrifileInfo>
            <S.Nickname>닉네임입니다.</S.Nickname>
            <S.Email>test@test.com</S.Email>
          </S.PrifileInfo>
          <IcExpandRight size={3.5} color={palette.textPrimary} />
        </S.ProfileButton>
        <S.WrapMyPoint>
          <S.PointDetailButton>
            <S.PointIcon>P</S.PointIcon>내 포인트
            <IcArrowDropRight size={2.5} color={palette.textPrimary} />
          </S.PointDetailButton>
          <S.PointChargeButton>
            <S.ChargeBadge>충전</S.ChargeBadge>
            10,000 P
          </S.PointChargeButton>
        </S.WrapMyPoint>
        <S.NavigateButtonList>
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

      {/* 로그아웃 Modal */}

      {isLogoutModal && (
        <ModalWithTwoButton
          content="정말 로그아웃하시겠어요?"
          setIsModalFalse={() => setIsLogoutModal(false)}
          modalEvent={() => console.log('로그아웃 API 연동')}
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

      <BottomNavigation />
    </>
  )
}

export default MyPage
