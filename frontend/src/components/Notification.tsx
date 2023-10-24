/* eslint-disable react/prop-types */
import styled from 'styled-components'
import { palette, zindex } from '../constants/Styles'

// 스타일드 컴포넌트를 사용하여 알림 스타일을 정의
const NotificationWrapper = styled.div<{ $show: boolean }>`
  width: 30rem;
  position: fixed;
  top: 10rem;
  right: 2rem;
  background-color: ${palette.white};
  color: ${palette.textPrimary};
  border: 1px solid ${palette.divider};
  border-radius: 0.5rem;
  padding: 2rem;
  z-index: ${zindex.modal}; /* 알림이 화면 위에 나타나도록 함 */
  transition: all 0.3s ease-in-out;
  margin-bottom: 1rem;
  box-shadow: 1px 1px 4px ${palette.shadow};

  /* 알림이 나타날 때의 애니메이션 */
  opacity: ${props => (props.$show ? 1 : 0)};
`

interface NotificationProps {
  text: string
  show: boolean
  setShow: (value: boolean) => void
}

const Notification: React.FC<NotificationProps> = ({ text, show }) => {
  return <NotificationWrapper $show={show}>{text}</NotificationWrapper>
}

export default Notification
