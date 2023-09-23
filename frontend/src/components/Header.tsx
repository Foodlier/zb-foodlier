import { useState } from 'react'
import styled from 'styled-components'
import logo from '../assets/foodlier_logo.png'
import useIcon from '../hooks/useIcon'
import { breakpoints, palette } from '../constants/Styles'
import NotificationItem from './notification/NotificationItem'

const Container = styled.header`
  width: 100vw;
  padding: 5%;
  display: flex;
  align-items: center;
  justify-content: space-between;

  ${breakpoints.large} {
    padding: 2%;
  }
`

const Logo = styled.img`
  width: 30%;
  height: auto;

  ${breakpoints.large} {
    width: 15%;
  }
`

const WrapMenu = styled.ul`
  display: none;
  align-items: center;

  ${breakpoints.large} {
    display: flex;
  }
`

const Menu = styled.button`
  padding: 0px 1.8rem;
  font-size: 1.8rem;
  font-weight: 600;
  color: ${palette.textPrimary};
`

const Notification = styled.div<{ $isToggle: boolean }>`
  position: absolute;
  display: ${props => (props.$isToggle ? 'flex' : 'none')};
  flex-direction: column;
  min-width: 25rem;
  top: 5rem;
  padding: 1rem;
  border: 1px solid ${palette.divider};
  border-radius: 1rem;
  font-size: 1.4rem;
`

const WrapIcon = styled.ul`
  display: flex;

  ${breakpoints.large} {
    display: none;
  }
`

const Icon = styled.button`
  margin-left: 1rem;
`

const LoginButton = styled.button`
  padding: 0.8rem 1.2rem;
  background-color: ${palette.main};
  color: white;
  border-radius: 1rem;
  margin-left: 1rem;
  font-size: 1.6rem;
  font-weight: 600;
`

const Button = styled.button``

const WrapNotification = styled.div`
  position: relative;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  padding: 0px 1.8rem;
  font-size: 1.8rem;
  font-weight: 600;
  color: ${palette.textPrimary};
`

const Header = () => {
  const { IcSearch, IcBell } = useIcon()

  const [isToggle, setIsToggle] = useState(false)

  const HEADER_MENU_LIST = ['꿀조합', '냉마카세']

  return (
    <Container>
      <Logo src={logo} alt="로고 이미지" />
      <WrapIcon>
        <WrapNotification>
          <Icon onClick={() => setIsToggle(!isToggle)}>
            <IcBell size={3} color={palette.textPrimary} />
          </Icon>
          <Notification $isToggle={isToggle}>
            <NotificationItem />
          </Notification>
        </WrapNotification>

        <Icon>
          <IcSearch size={3} color={palette.textPrimary} />
        </Icon>
      </WrapIcon>
      <WrapMenu>
        {HEADER_MENU_LIST.map(item => (
          <Menu key={item}>{item}</Menu>
        ))}
        <WrapNotification>
          <Button onClick={() => setIsToggle(!isToggle)}>
            <IcBell size={3} color={palette.textPrimary} />
          </Button>
          <Notification $isToggle={isToggle}>
            <NotificationItem />
          </Notification>
        </WrapNotification>
        <LoginButton>LOGIN</LoginButton>
      </WrapMenu>
    </Container>
  )
}

export default Header
