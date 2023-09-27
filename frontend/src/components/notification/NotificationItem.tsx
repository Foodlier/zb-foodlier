import React from 'react'
import { useNavigate } from 'react-router-dom'
import styled from 'styled-components'
import { palette } from '../../constants/Styles'

// “content”, “sendTime”, “id”, “seen”

// interface NotificationItemProps {
//   id: number | string
//   content: string
//   sendTime: string
//   seen: boolean
// }

const Container = styled.button`
  display: flex;
  flex-direction: column;
  background-color: white;
  padding: 1rem;
  border-radius: inherit;
`

const Title = styled.span`
  font-size: 1.4rem;
  color: ${palette.textPrimary};
`

const Date = styled.span`
  font-size: 1.2rem;
  color: ${palette.textSecondary};
`

const NotificationItem = () => {
  const navigate = useNavigate()

  const dummyData = {
    id: 1,
    content: '테스트입니다',
    sendTime: '1990-01-01',
    seen: false,
  }

  // 알림 클릭 시 해당 페이지로 이동 (임시 : 현재 메인으로)
  const goToRecipe = () => {
    navigate('/')
  }

  return (
    <Container onClick={goToRecipe}>
      <Title>{dummyData.content}</Title>
      <Date>{dummyData.sendTime}</Date>
    </Container>
  )
}

export default NotificationItem