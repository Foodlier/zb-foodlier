import { useEffect, useRef, useState } from 'react'
import Header from '../../components/Header'
import BottomNavigation from '../../components/BottomNavigation'
import * as S from '../../styles/chat/ChattingPage.styled'
import ChatRoom from '../../components/chat/ChatRoom'
import RoomListItem, {
  RoomInfoInterface,
} from '../../components/chat/RoomListItem'
import axiosInstance from '../../utils/FetchCall'
import DmTest2 from './DmTest2'

const ChattingPage = () => {
  const [currentChat, setCurrentChat] = useState(1)
  const [dmRoomList, setDmRoomList] = useState<RoomInfoInterface[]>([])
  const [isLoading, setIsLoading] = useState(false)
  const firstDmRoomRef = useRef<HTMLButtonElement>(null)

  const getDmRoomList = async () => {
    try {
      const res = await axiosInstance.get('/api/dm/room/0/10')
      if (res.status === 200) {
        console.log(res.data.content)
        setDmRoomList(res.data.content)
      }
    } catch (error) {
      console.log(error)
    }
  }

  useEffect(() => {
    getDmRoomList()
  }, [])

  useEffect(() => {
    if (dmRoomList.length > 0) {
      setIsLoading(true)
    }
    if (dmRoomList.length > 0 && firstDmRoomRef.current) {
      firstDmRoomRef.current.click()
    }
  }, [dmRoomList])

  return (
    <>
      <Header />
      {isLoading && (
        <S.Container>
          {dmRoomList.length > 0 ? (
            <>
              <S.WrapChatList>
                {dmRoomList.map((item, index) => (
                  <S.Wrap key={item.roomId}>
                    <S.ReqireButton>요청서 보기</S.ReqireButton>
                    <S.RoomListButton
                      ref={index === 0 ? firstDmRoomRef : null}
                      onClick={() => setCurrentChat(item.roomId)}
                      $isActive={currentChat === item.roomId}
                    >
                      <RoomListItem roomInfo={item} />
                    </S.RoomListButton>
                  </S.Wrap>
                ))}
              </S.WrapChatList>

              <ChatRoom
                roomNum={
                  dmRoomList.find(item => item.roomId === currentChat)?.roomId
                }
              />
            </>
          ) : (
            <S.NoRoom>현재 생성된 채팅방이 없습니다</S.NoRoom>
          )}
        </S.Container>
      )}

      <DmTest2 />
      <BottomNavigation />
    </>
  )
}

export default ChattingPage
