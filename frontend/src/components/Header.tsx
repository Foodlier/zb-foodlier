import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import logo from '../assets/foodlier_logo.png'
import useIcon from '../hooks/useIcon'
import { palette } from '../constants/Styles'
import NotificationItem from './notification/NotificationItem'
import * as R from '../styles/Header.styled'

const Header = () => {
  const navigate = useNavigate()

  const { IcSearch, IcBell } = useIcon()

  const [isToggle, setIsToggle] = useState(false)

  const HEADER_MENU_LIST = [
    {
      title: '꿀조합',
      navigate: 'recipe',
    },
    { title: '냉마카세', navigate: '' },
  ]

  const navigateTo = (pageName: string) => {
    navigate(`/${pageName}`)
  }

  return (
    <R.Container>
      <R.Logo src={logo} alt="로고 이미지" />
      <R.WrapIcon>
        <R.WrapNotification>
          <R.Icon onClick={() => setIsToggle(!isToggle)}>
            <IcBell size={3} color={palette.textPrimary} />
          </R.Icon>
          <R.Notification $isToggle={isToggle}>
            <NotificationItem />
          </R.Notification>
        </R.WrapNotification>

        <R.Icon>
          <IcSearch size={3} color={palette.textPrimary} />
        </R.Icon>
      </R.WrapIcon>
      <R.WrapMenu>
        {HEADER_MENU_LIST.map(item => (
          <R.Menu key={item.title} onClick={() => navigateTo(item.navigate)}>
            {item.title}
          </R.Menu>
        ))}
        <R.WrapNotification>
          <R.Button onClick={() => setIsToggle(!isToggle)}>
            <IcBell size={3} color={palette.textPrimary} />
          </R.Button>
          <R.Notification $isToggle={isToggle}>
            <NotificationItem />
          </R.Notification>
        </R.WrapNotification>
        <R.LoginButton>LOGIN</R.LoginButton>
      </R.WrapMenu>
    </R.Container>
  )
}

export default Header
