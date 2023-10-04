import { useState } from 'react'
import Header from '../../components/Header'
import BottomNavigation from '../../components/BottomNavigation'
import * as S from '../../styles/chat/ChattingPage.styled'
import ChatRoom from '../../components/chat/ChatRoom'
import RoomListItem from '../../components/chat/RoomListItem'

const ChattingPage = () => {
  const [currentChat, setCurrentChat] = useState(1)

  const dummy = [
    {
      roomId: 1,
      targetNickName: '상대방 닉네임1',
      targetProfileImageUrl: 'https://via.placeholder.com/500',
      requestId: 1,
      expectedPrice: '10000',
    },
    {
      roomId: 2,
      targetNickName: '상대방 닉네임2',
      targetProfileImageUrl: 'https://via.placeholder.com/500',
      requestId: 2,
      expectedPrice: '20000',
    },
    {
      roomId: 3,
      targetNickName: '상대방 닉네임3',
      targetProfileImageUrl: 'https://via.placeholder.com/500',
      requestId: 3,
      expectedPrice: '30000',
    },
  ]

  return (
    <>
      <Header />
      <S.Container>
        <S.WrapChatList>
          {dummy.map(item => (
            <S.Wrap key={item.roomId}>
              <S.ReqireButton>요청서 보기</S.ReqireButton>
              <S.RoomListButton
                onClick={() => setCurrentChat(item.roomId)}
                $isActive={currentChat === item.roomId}
              >
                <RoomListItem roomInfo={item} />
              </S.RoomListButton>
            </S.Wrap>
          ))}
        </S.WrapChatList>
        <ChatRoom roomInfo={dummy.find(item => item.roomId === currentChat)} />
      </S.Container>
      <BottomNavigation />
    </>
  )
}

export default ChattingPage
