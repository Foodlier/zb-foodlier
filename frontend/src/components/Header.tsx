import { useState } from 'react'
import * as S from '../styles/HeaderPage.styled'
import { palette } from '../constants/Styles'
import logo from '../assets/foodlier_logo.png'
import useIcon from '../hooks/useIcon'
import NotificationItem from './notification/NotificationItem'
import MoSearch from './search/MoSearch'

const Header = () => {
  const { IcSearch, IcBell } = useIcon()

  const [isToggle, setIsToggle] = useState(false)
  const [isMoSearchOpen, setIsMoSearchOpen] = useState(false)

  const toggleMoSearch = () => {
    setIsMoSearchOpen(!isMoSearchOpen)
  }

  const HEADER_MENU_LIST = ['꿀조합', '냉마카세']

  return (
    <S.Container>
      <S.Logo src={logo} alt="로고 이미지" />
      <S.WrapIcon>
        <S.WrapNotification>
          <S.Icon onClick={() => setIsToggle(!isToggle)}>
            <IcBell size={3} color={palette.textPrimary} />
          </S.Icon>
          <S.Notification $isToggle={isToggle}>
            <NotificationItem />
          </S.Notification>
        </S.WrapNotification>

        <S.Icon onClick={toggleMoSearch}>
          <IcSearch size={3} color={palette.textPrimary} />
        </S.Icon>
      </S.WrapIcon>
      <S.WrapMenu>
        {HEADER_MENU_LIST.map(item => (
          <S.Menu key={item}>{item}</S.Menu>
        ))}
        <S.WrapNotification>
          <S.Button onClick={() => setIsToggle(!isToggle)}>
            <IcBell size={3} color={palette.textPrimary} />
          </S.Button>
          <S.Notification $isToggle={isToggle}>
            <NotificationItem />
          </S.Notification>
        </S.WrapNotification>
        <S.LoginButton>LOGIN</S.LoginButton>
      </S.WrapMenu>

      {isMoSearchOpen && <MoSearch setIsMoSearchOpen={setIsMoSearchOpen} />}
    </S.Container>
  )
}

export default Header
