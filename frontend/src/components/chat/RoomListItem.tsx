import * as S from '../../styles/chat/RoomListItem.styled'
import returnPriceWithComma from '../../utils/returnPriceWithComma'
import useIcon from '../../hooks/useIcon'

export interface RoomInfoInterface {
  roomId: number
  nickname: string
  profileUrl: string
  requestId: number
  expectedPrice: string
  exit: boolean
  role: string
  suggested: boolean
}

const RoomListItem = ({ roomInfo }: { roomInfo: RoomInfoInterface }) => {
  const { InitialUserImg } = useIcon()

  return (
    <S.WrapChatItem>
      <S.Wrap>
        {roomInfo.profileUrl ? (
          <S.ProfileImage src={roomInfo.profileUrl} />
        ) : (
          <InitialUserImg size={4.5} />
        )}

        <S.WrapProfileText>
          <S.Nickname>{roomInfo.nickname}</S.Nickname>
          <S.Price>{returnPriceWithComma(roomInfo.expectedPrice)}</S.Price>
        </S.WrapProfileText>
      </S.Wrap>
    </S.WrapChatItem>
  )
}

export default RoomListItem
