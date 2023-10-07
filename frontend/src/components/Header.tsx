import { useEffect, useRef, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import logo from '../../public/images/foodlier_logo.png'
import useIcon from '../hooks/useIcon'
import { palette } from '../constants/Styles'
import NotificationItem from './notification/NotificationItem'
import * as S from '../styles/Header.styled'
import MoSearch from './search/MoSearch'

const Header = () => {
  const navigate = useNavigate()
  const { IcSearch, IcBell } = useIcon()

  const notiRef = useRef<HTMLDivElement | null>(null)
  const [isToggle, setIsToggle] = useState(false)

  useEffect(() => {
    // 토글이 열려있을 경우 화면 클릭 시 토글 닫히게 설정
    const handleOutsideClose = () => {
      if (isToggle) {
        setIsToggle(false)
      }
    }

    document.addEventListener('click', handleOutsideClose, { capture: true })

    return () => document.removeEventListener('click', handleOutsideClose)
  }, [isToggle])

  // 모바일 검색 - 토글
  const [isMoSearchOpen, setIsMoSearchOpen] = useState(false)

  const toggleMoSearch = () => {
    setIsMoSearchOpen(!isMoSearchOpen)
  }

  const HEADER_MENU_LIST = [
    {
      title: '꿀조합',
      navigate: 'recipe',
    },
    { title: '냉마카세', navigate: 'refrigerator' },
    { title: '채팅', navigate: 'chat' },
  ]

  const navigateTo = (pageName: string) => {
    navigate(`/${pageName}`)
  }

  return (
    <S.Container>
      <S.LogoButton onClick={() => navigateTo('')}>
        <S.Logo src={logo} alt="로고 이미지" />
      </S.LogoButton>
      <S.WrapIcon>
        <S.WrapNotification>
          <S.Icon onClick={() => setIsToggle(!isToggle)}>
            <IcBell size={3} color={palette.textPrimary} />
          </S.Icon>
          <S.Notification $isToggle={isToggle}>
            <NotificationItem />
          </S.Notification>
        </S.WrapNotification>
        {/* 모바일 검색 - onClick 추가 */}
        <S.Icon onClick={toggleMoSearch}>
          <IcSearch size={3} color={palette.textPrimary} />
        </S.Icon>
      </S.WrapIcon>
      <S.WrapMenu>
        {HEADER_MENU_LIST.map(item => (
          <S.Menu key={item.title} onClick={() => navigateTo(item.navigate)}>
            {item.title}
          </S.Menu>
        ))}
        <S.WrapNotification ref={notiRef}>
          <S.Button onClick={() => setIsToggle(!isToggle)}>
            <IcBell size={3} color={palette.textPrimary} />
          </S.Button>
          <S.Notification $isToggle={isToggle}>
            <NotificationItem />
          </S.Notification>
        </S.WrapNotification>
        <S.LoginButton onClick={() => navigateTo('login')}>LOGIN</S.LoginButton>
      </S.WrapMenu>
      {/* 모바일 검색 - MoSearch 컴포넌트 추가  */}
      {isMoSearchOpen && <MoSearch setIsMoSearchOpen={setIsMoSearchOpen} />}
    </S.Container>
  )
}

export default Header
