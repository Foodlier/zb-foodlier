import React from 'react'
import * as S from '../../styles/chat/ChatListItem.styled'
import useIcon from '../../hooks/useIcon'
import { palette } from '../../constants/Styles'

const ChatListItem = () => {
  const { IcDeskAltLight } = useIcon()
  return (
    <S.WrapChatItem>
      <S.Wrap>
        <S.ProfileImage />
        <S.WrapProfileText>
          <S.Nickname>닉네임</S.Nickname>
          <S.Price>10,000원</S.Price>
        </S.WrapProfileText>
      </S.Wrap>
      <IcDeskAltLight size={3} color={palette.textPrimary} />
    </S.WrapChatItem>
  )
}

export default ChatListItem
