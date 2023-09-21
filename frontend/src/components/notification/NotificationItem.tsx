import React from 'react'
import styled from 'styled-components'
import { palette } from '../../constants/Styles'

// “content”, “sendTime”, “id”, “seen”

// interface NotificationItemProps {
//   id: number | string
//   content: string
//   sendTime: string
//   seen: boolean
// }

const Container = styled.div`
  display: flex;
  flex-direction: column;
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
  const dummyData = {
    id: 1,
    content: '테스트입니다',
    sendTime: '1990-01-01',
    seen: false,
  }

  return (
    <Container>
      <Title>{dummyData.content}</Title>
      <Date>{dummyData.sendTime}</Date>
    </Container>
  )
}

export default NotificationItem
