import { useEffect, useRef, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import logo from '../assets/foodlier_logo.png'
import useIcon from '../hooks/useIcon'
import { palette } from '../constants/Styles'
import NotificationItem from './notification/NotificationItem'
import * as R from '../styles/Header.styled'

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
        <R.WrapNotification ref={notiRef}>
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
