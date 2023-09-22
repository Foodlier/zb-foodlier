import styled from 'styled-components'
import useIcon from '../hooks/useIcon'
import { breakpoints, palette } from '../constants/Styles'

const Container = styled.div`
  position: fixed;
  display: flex;
  bottom: 0;
  width: 100vw;
  padding: 2rem 0;
  border-radius: 2rem 2rem 0 0;
  box-shadow: 0px 4px 15px ${palette.shadow};

  ${breakpoints.large} {
    display: none;
  }
`

const WrapIcon = styled.button`
  display: flex;
  flex: 1;
  flex-direction: column;
  align-items: center;
  color: ${palette.textPrimary};
`

const Text = styled.span`
  font-size: 1.3rem;
  margin-top: 0.5rem;
`

const BottomNavigation = () => {
  const { IcHomeLight, IcFavoriteLight, IcPinLight, IcChatLight, IcUserLight } =
    useIcon()
  const ICON_SIZE = 3

  const BOTTOM_NAVIGATION_MENU_LIST = [
    {
      title: '홈',
      icon: <IcHomeLight size={ICON_SIZE} color={palette.textPrimary} />,
    },
    {
      title: '꿀조합',
      icon: <IcFavoriteLight size={ICON_SIZE} color={palette.textPrimary} />,
    },
    {
      title: '냉마카세',
      icon: <IcPinLight size={ICON_SIZE} color={palette.textPrimary} />,
    },
    {
      title: '채팅',
      icon: <IcChatLight size={ICON_SIZE} color={palette.textPrimary} />,
    },
    {
      title: '내 정보',
      icon: <IcUserLight size={ICON_SIZE} color={palette.textPrimary} />,
    },
  ]

  return (
    <Container>
      {BOTTOM_NAVIGATION_MENU_LIST.map(item => (
        <WrapIcon key={item.title}>
          {item.icon}
          <Text>{item.title}</Text>
        </WrapIcon>
      ))}
    </Container>
  )
}

export default BottomNavigation
