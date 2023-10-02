import React from 'react'
import * as S from '../../styles/chat/RoomListItem.styled'
import returnPriceWithComma from '../../utils/returnPriceWithComma'

export interface RoomInfoInterface {
  roomId: number
  targetNickName: string
  targetProfileImageUrl: string
  requestId: number
  expectedPrice: string
}

const RoomListItem = ({ roomInfo }: { roomInfo: RoomInfoInterface }) => {
  return (
    <S.WrapChatItem>
      <S.Wrap>
        <S.ProfileImage src={roomInfo.targetProfileImageUrl} />
        <S.WrapProfileText>
          <S.Nickname>{roomInfo.targetNickName}</S.Nickname>
          <S.Price>{returnPriceWithComma(roomInfo.expectedPrice)}</S.Price>
        </S.WrapProfileText>
      </S.Wrap>
    </S.WrapChatItem>
  )
}

export default RoomListItem
