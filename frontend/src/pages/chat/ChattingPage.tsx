import React from 'react'
import Header from '../../components/Header'
import BottomNavigation from '../../components/BottomNavigation'
import * as S from '../../styles/chat/ChattingPage.styled'
import useIcon from '../../hooks/useIcon'
import { palette } from '../../constants/Styles'
import ChatListItem from '../../components/chat/ChatListItem'

const ChattingPage = () => {
  const { IcImgBoxLight } = useIcon()

  const dummy = [
    {
      id: 0,
      isMe: false,
      message:
        '긴 텍스트 입력 예시긴 텍스트 입력 예시긴 텍스트 입력 예시긴 텍스트 입력 예시긴 텍스트 입력 예시긴 텍스트 입력 예시긴 텍스트 입력 예시긴 텍스트 입력 예시긴 텍스트 입력 예시긴 텍스트 입력 예시긴 텍스트 입력 예시긴 텍스트 입력 예시',
      imageUrl: '',
      createdAt: '2023-09-21T09:00:00',
    },

    {
      id: 1,
      isMe: true,
      message: '메세지2',
      imageUrl: '',
      createdAt: '2023-09-21T09:00:00',
    },
    {
      id: 2,
      isMe: true,
      message: '메세지3',
      imageUrl: '',
      createdAt: '2023-09-21T09:00:00',
    },
  ]

  const dateFormat = (date: string) => {
    return date.substring(11, 16)
  }

  return (
    <>
      <Header />
      <S.Container>
        <S.WrapChatList>
          <ChatListItem />
        </S.WrapChatList>
        <S.ChattingRoom>
          <S.ChattingHeader>
            <S.ProfileImage $size={4} />
            <S.Nickname>닉네임입니다.</S.Nickname>
          </S.ChattingHeader>
          <S.ChattingMessage>
            {dummy.map(item => (
              <S.WrapMessage key={item.id} $isMe={item.isMe}>
                <S.FlexAlignCenter>
                  {!item.isMe && <S.ProfileImage $size={3} />}
                  <S.Message $isMe={item.isMe}>{item.message}</S.Message>
                </S.FlexAlignCenter>
                <S.MessageTime>{dateFormat(item.createdAt)}</S.MessageTime>
              </S.WrapMessage>
            ))}
            <S.WrapDate>2023.09.02</S.WrapDate>
          </S.ChattingMessage>
          <S.WrapInput>
            <IcImgBoxLight size={3.5} color={palette.textPrimary} />
            <S.Input />
            <S.Button>전송</S.Button>
          </S.WrapInput>
        </S.ChattingRoom>
      </S.Container>
      <BottomNavigation />
    </>
  )
}

export default ChattingPage
