import { useNavigate } from 'react-router-dom'
import * as R from '../styles/BottomNavigation.styled'
import useIcon from '../hooks/useIcon'
import { palette } from '../constants/Styles'

const BottomNavigation = () => {
  const navigate = useNavigate()
  const { IcHomeLight, IcFavoriteLight, IcPinLight, IcChatLight, IcUserLight } =
    useIcon()
  const ICON_SIZE = 3

  const BOTTOM_NAVIGATION_MENU_LIST = [
    {
      title: '홈',
      icon: <IcHomeLight size={ICON_SIZE} color={palette.textPrimary} />,
      navigate: '',
    },
    {
      title: '꿀조합',
      icon: <IcFavoriteLight size={ICON_SIZE} color={palette.textPrimary} />,
      navigate: 'recipe',
    },
    {
      title: '냉마카세',
      icon: <IcPinLight size={ICON_SIZE} color={palette.textPrimary} />,
      navigate: '',
    },
    {
      title: '채팅',
      icon: <IcChatLight size={ICON_SIZE} color={palette.textPrimary} />,
      navigate: '',
    },
    {
      title: '내 정보',
      icon: <IcUserLight size={ICON_SIZE} color={palette.textPrimary} />,
      navigate: '',
    },
  ]

  const navigateTo = (pageName: string) => {
    navigate(`/${pageName}`)
  }

  return (
    <R.Container>
      {BOTTOM_NAVIGATION_MENU_LIST.map(item => (
        <R.WrapIcon key={item.title} onClick={() => navigateTo(item.navigate)}>
          {item.icon}
          <R.Text>{item.title}</R.Text>
        </R.WrapIcon>
      ))}
    </R.Container>
  )
}

export default BottomNavigation