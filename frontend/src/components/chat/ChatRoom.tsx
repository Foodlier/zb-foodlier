import { useState, useEffect, useRef, KeyboardEvent } from 'react'
import SockJS from 'sockjs-client'
import StompJs from 'stompjs'
import { palette } from '../../constants/Styles'
import useIcon from '../../hooks/useIcon'
import * as S from '../../styles/chat/ChatRoom.styled'
import ProposalModal from './ProposalModal'
import { RoomInfoInterface } from './RoomListItem'
import ModalWithTwoButton from '../ui/ModalWithTwoButton'
import getTime from '../../utils/getTime'
import axiosInstance from '../../utils/FetchCall'

interface DmMessage {
  roomId: number
  dmId: number
  message: string
  writer: string
  messageType: string
  createdAt: string
}

const ChatRoom = ({
  roomInfo,
}: {
  roomInfo: RoomInfoInterface | undefined
}) => {
  const { IcImgBoxLight } = useIcon()
  const [isProposalModalOpen, setIsProposalModalOpen] = useState(false)
  const [isExitModalOpen, setIsExitModalOpen] = useState(false)
  const [dmMessageList, setDmMessageList] = useState<DmMessage[]>([])
  const [message, setMessage] = useState('')
  const [stompClientstate, setStompClientstate] = useState<StompJs.Client>()
  // 옵저버 관찰 대상
  const observerEl = useRef<HTMLDivElement>(null!)

  // 가장 마지막 채팅 Ref
  const lastDmRef = useRef<HTMLInputElement>(null!)

  const nowNickname = '김도빈테스트'

  // 로그인 구현시 TOKEN 따로 받아와야 함
  const TOKEN =
    'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJybGFlaHFAZ21haWwuY29tIiwianRpIjoiMSIsInJvbGVzIjpbIlJPTEVfVVNFUiIsIlJPTEVfQ0hFRiJdLCJpYXQiOjE3Mjg4NTkwMTUsImV4cCI6MTcyODkwMjIxNX0.tPx-ZNgpczdQQuxHiT7t691IeGGAQSCgPMggSSRorFyHvzMu-yIboEAbKGm9YfBK7fHMyv3TD-dMCbnkMqmO5g'

  console.log(roomInfo)
  console.log(dmMessageList)

  useEffect(() => {
    // 채팅 환경설정
    const socket = new SockJS('http://localhost:8080/ws')
    const stompClient = StompJs.over(socket)
    setStompClientstate(stompClient)
    stompClient.connect({ Authorization: TOKEN }, () => {
      // 채팅방 번호 설정
      const roomNum = roomInfo?.id
      stompClient.subscribe(`/sub/message/${roomNum}`, comeMessage => {
        // 새로운 메시지 도착 시 호출될 콜백 함수
        const newMessage = JSON.parse(comeMessage.body)
        setDmMessageList(prevMessages => [newMessage, ...prevMessages])
      })
    })

    let lastDmNum: number = 0

    // 채팅 전 나눴던 DM 리스트 가져오기
    const getDmMessage = async () => {
      try {
        const res = await axiosInstance.get(
          `/dm/message?roomId=${roomInfo?.id}&dmId=${lastDmNum}`
        )
        if (res.status === 200) {
          setDmMessageList(prevMessages =>
            prevMessages.concat(res.data.messageList)
          )
          lastDmNum = res.data.messageList[res.data.messageList.length - 1].dmId
          console.log(lastDmNum)
        }
      } catch (error) {
        console.log(error)
      }
    }

    getDmMessage()

    // 무한 스크롤 옵저버
    const observer = new IntersectionObserver(
      entries => {
        if (entries[0].isIntersecting) {
          setTimeout(() => {
            getDmMessage()
          }, 500)
        }
      },
      { threshold: 1 }
    )

    // // 관찰 대상 지정
    observer.observe(observerEl.current)

    const obeserveElCopy = observerEl.current

    return () => {
      // 소켓 리셋
      socket.close()
      // 옵저버 리셋
      observer.unobserve(obeserveElCopy)
      // 디엠방 리셋
      setDmMessageList([])
    }
  }, [roomInfo])

  useEffect(() => {
    if (dmMessageList.length > 0 && lastDmRef.current) {
      lastDmRef.current.focus()
    }
  }, [dmMessageList])

  // 메시지 전송
  const sendMessage = () => {
    const roomNum = roomInfo?.id
    const newMessage = {
      content: message,
      sender: nowNickname,
    }
    if (stompClientstate && message !== '') {
      stompClientstate.send(
        `/pub/message/${roomNum}`,
        {},
        JSON.stringify({
          roomId: roomNum,
          message: newMessage.content,
          writer: newMessage.sender,
          // 정형화
          messageType: 'CHAT',
        })
      )
    }
    setMessage('')
  }

  // 가격 제안
  const sendSuggestion = (price: number) => {
    const roomNum = roomInfo?.id
    const newMessage = {
      content: price,
      sender: nowNickname,
    }
    if (stompClientstate) {
      stompClientstate.send(
        `/pub/message/${roomNum}`,
        {},
        JSON.stringify({
          roomId: roomNum,
          message: newMessage.content,
          writer: newMessage.sender,
          messageType: 'SUGGESTION',
        })
      )
    }
  }

  // 채팅방 나가기
  const leaveRoom = async () => {
    try {
      const res = await axiosInstance.put(`/dm/room/exit/${roomInfo?.id}`)
      if (res.status === 200) {
        console.log(res)
      }
    } catch (error) {
      console.log(error)
    }
  }

  // 엔터로 메세지 보내기
  const handleKeyUp = (e: KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      sendMessage()
    }
  }

  return (
    <S.Container>
      <S.ChattingHeader>
        <S.FlexAlignCenter>
          <S.ProfileImage src={roomInfo?.profileUrl} $size={4} />
          <S.Nickname>{roomInfo?.nickname}</S.Nickname>
        </S.FlexAlignCenter>
        <S.FlexAlignCenter>
          <S.ProposalButton onClick={() => setIsProposalModalOpen(true)}>
            제안하기
          </S.ProposalButton>
          <S.ExitButton onClick={() => setIsExitModalOpen(true)}>
            채팅방 나가기
          </S.ExitButton>
        </S.FlexAlignCenter>
      </S.ChattingHeader>
      <S.ChattingMessage>
        {roomInfo?.exit && (
          <S.ExitMessage>
            상대방이 존재하지 않아 채팅이 불가합니다.
          </S.ExitMessage>
        )}
        {dmMessageList.map((item, index) => (
          <S.WrapMessage key={item.dmId} $isMe={item.writer === nowNickname}>
            {item.messageType === 'CHAT' ? (
              <S.Wrap>
                {!(item.writer === nowNickname) && (
                  <S.ProfileImage src={roomInfo?.profileUrl} $size={3} />
                )}
                <S.Message $isMe={item.writer === nowNickname}>
                  {item.message}
                  <S.FocusInput
                    ref={index === 0 ? lastDmRef : null}
                    type="text"
                  />
                </S.Message>
              </S.Wrap>
            ) : (
              <S.Wrap>
                {!(item.writer === nowNickname) && (
                  <S.ProfileImage src={roomInfo?.profileUrl} $size={3} />
                )}
                {item.writer === nowNickname && (
                  <S.Suggestion $isMe={item.writer === nowNickname}>
                    <S.SuggestionTitle>
                      {item.message}원을 제안하셨습니다.
                    </S.SuggestionTitle>
                    <p>상대방이 수락 혹은 거절할 수 있어요!</p>
                  </S.Suggestion>
                )}
                {item.writer !== nowNickname && (
                  <S.Suggestion $isMe={item.writer !== nowNickname}>
                    <S.SuggestionTitle>
                      {item.message}원을 제안하셨습니다.
                    </S.SuggestionTitle>
                    <S.SuggestionButton type="button" $isAccept>
                      수락
                    </S.SuggestionButton>
                    <S.SuggestionButton type="button" $isAccept={false}>
                      거절
                    </S.SuggestionButton>
                  </S.Suggestion>
                )}
              </S.Wrap>
            )}
            <S.MessageTime>{getTime(item.createdAt)}</S.MessageTime>
          </S.WrapMessage>
        ))}
        {/* <S.WrapDate>2023.09.02</S.WrapDate> */}
        <S.ObserverDiv ref={observerEl} />
      </S.ChattingMessage>
      <S.WrapInput>
        <IcImgBoxLight size={3.5} color={palette.textPrimary} />
        <S.Input
          value={message}
          onChange={e => setMessage(e.target.value)}
          onKeyUp={e => handleKeyUp(e)}
          disabled={roomInfo?.exit}
        />
        <S.Button onClick={sendMessage} disabled={roomInfo?.exit}>
          전송
        </S.Button>
      </S.WrapInput>
      {isProposalModalOpen && (
        <ProposalModal
          setIsProposalModalOpen={setIsProposalModalOpen}
          sendSuggestion={sendSuggestion}
        />
      )}
      {isExitModalOpen && (
        <ModalWithTwoButton
          content="채팅방을 나가시겠습니까?"
          setIsModalFalse={() => setIsExitModalOpen(false)}
          modalEvent={() => {
            leaveRoom()
            setIsExitModalOpen(false)
          }}
        />
      )}
    </S.Container>
  )
}

export default ChatRoom
