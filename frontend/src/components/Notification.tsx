import { useState, useEffect } from 'react'
import styled from 'styled-components'

// 스타일드 컴포넌트를 사용하여 알림 스타일을 정의
const NotificationWrapper = styled.div<{ show: boolean }>`
  background-color: rgba(0, 0, 0, 0.8);
  color: white;
  padding: 10px;
  border-radius: 5px;
  z-index: 9999; /* 알림이 화면 위에 나타나도록 함 */
  transition: opacity 0.3s ease-in-out;

  /* 알림이 나타날 때의 애니메이션 */
  opacity: ${({ show }) => (show ? '1' : '0')};
`

interface NotificationProps {
  message: string
}

const Notification: React.FC<NotificationProps> = ({ message }) => {
  const [show, setShow] = useState(true)

  useEffect(() => {
    // 5초 후에 알림을 숨김
    const timer = setTimeout(() => {
      setShow(false)
    }, 5000)

    return () => clearTimeout(timer)
  }, [])

  return <NotificationWrapper show={show}>{message}</NotificationWrapper>
}

export default Notification
