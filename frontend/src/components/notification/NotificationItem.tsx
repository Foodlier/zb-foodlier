import { useNavigate } from 'react-router-dom'
import styled from 'styled-components'
import { breakpoints, palette, typography } from '../../constants/Styles'
import { NotiItem } from '../../constants/Interfaces'
import axiosInstance from '../../utils/FetchCall'
import formatDateTime from '../../utils/returnDate'

const Container = styled.button<{ $isRead: boolean }>`
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: flex-start;
  background-color: ${props => (props.$isRead ? palette.white : '#FFF4F2')};
  padding: 1rem;
  border-radius: inherit;
  border-bottom: 1px solid ${palette.divider};
`

const Title = styled.span`
  text-align: left;
  font-size: ${typography.mobile.desc};
  color: ${palette.textPrimary};
  margin-top: 1rem;

  ${breakpoints.large} {
    font-size: ${typography.web.desc};
  }
`

const Date = styled.span`
  font-size: ${typography.mobile.desc};
  color: ${palette.textSecondary};
  ${breakpoints.large} {
    font-size: ${typography.web.desc};
  }
`

const NotificationItem = ({ item }: { item: NotiItem }) => {
  const navigate = useNavigate()

  const readNoti = async () => {
    const res = await axiosInstance.patch(`/api/notification/read/${item.id}`)
    console.log(res)
  }

  // 알림 클릭 시 해당 페이지로 이동
  const onClickNoti = () => {
    readNoti()
    if (item.notificationType === 'REQUEST') {
      navigate('/refrigerator')
    } else {
      navigate(`/recipe/detail/${item.targetId}`)
    }
  }

  if (!item) return null

  return (
    <Container onClick={onClickNoti} $isRead={item.read}>
      <Date>{formatDateTime(item.sentAt)}</Date>

      <Title>{item.content}</Title>
    </Container>
  )
}

export default NotificationItem
