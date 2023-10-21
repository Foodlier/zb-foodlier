/* eslint-disable react/prop-types */
import { useState, useEffect } from 'react'
import styled from 'styled-components'
import { palette, zindex } from '../constants/Styles'

// 스타일드 컴포넌트를 사용하여 알림 스타일을 정의
const NotificationWrapper = styled.div<{ $show: boolean; $index: number }>`
  position: fixed;
  top: ${props => props.$index * 10}rem;
  right: 2rem;
  background-color: ${palette.white};
  color: ${palette.textPrimary};
  border: 1px solid ${palette.divider};
  border-radius: 1rem;
  padding: 2rem;
  z-index: ${zindex.modal}; /* 알림이 화면 위에 나타나도록 함 */
  transition: all 0.3s ease-in-out;
  margin-bottom: 1rem;

  /* 알림이 나타날 때의 애니메이션 */
  display: ${props => (props.$show ? 'block' : 'none')};
`

interface NotificationProps {
  text: string
  index: number
}

const Notification: React.FC<NotificationProps> = ({ text, index }) => {
  const [show, setShow] = useState(true)

  useEffect(() => {
    // 5초 후에 알림을 숨김
    const timer = setTimeout(() => {
      setShow(false)
    }, 5000)

    return () => clearTimeout(timer)
  }, [])

  return (
    <NotificationWrapper $show={show} $index={index}>
      {text}
    </NotificationWrapper>
  )
}

export default Notification
