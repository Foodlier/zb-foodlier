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
import DealModal from '../point/DealModal'

interface DmMessage {
  roomId: number
  dmId: number
  message: string
  writer: string
  messageType: string
  createdAt: string
}

const ChatRoom = ({ roomNum }: { roomNum: number | undefined }) => {
  const { IcImgBoxLight } = useIcon()
  const [isProposalModalOpen, setIsProposalModalOpen] = useState(false)
  const [isDealModalOpen, setIsDealModalOpen] = useState(false)
  const [isExitModalOpen, setIsExitModalOpen] = useState(false)
  const [dmMessageList, setDmMessageList] = useState<DmMessage[]>([])
  const [message, setMessage] = useState('')
  const [stompClientstate, setStompClientstate] = useState<StompJs.Client>()
  const [roomInfo, setRoomInfo] = useState<RoomInfoInterface>()
  const [isSuggested, setIsSuggested] = useState(roomInfo?.suggested)
  // 옵저버 관찰 대상
  const observerEl = useRef<HTMLDivElement>(null!)
  // 가장 마지막 채팅 Ref
  const lastDmRef = useRef<HTMLInputElement>(null!)
  // 거래 금액 저장
  const priceRef = useRef(0)

  // 로그인 구현시 TOKEN 따로 받아와야 함
  const nowNickname = '김도빈테스트'
  const TOKEN =
    'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlaHFsczgxOEBuYXZlci5jb20iLCJqdGkiOiI4Iiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sInR5cGUiOiJBVCIsImlhdCI6MTY5Nzg3ODY5MSwiZXhwIjoxNjk3ODgyMjkxfQ.sYYjrm-8-vzI1kS1MI3BIXxD8t3p48rHrNpkq6qaWVOMJ5BjmvBADh7SYJXDjKKh8EAkRg-H5iNvZcmbCDxfqQ'

  useEffect(() => {
    // 방 정보 새로 가져오기
    const getRoomInfo = async () => {
      try {
        const res = await axiosInstance.get('/api/dm/room/0/10')
        if (res.status === 200) {
          const newRoomInfo = res.data.content.find(
            (item: RoomInfoInterface) => item.roomId === roomNum
          )
          setRoomInfo(newRoomInfo)
        }
      } catch (error) {
        console.log(error)
      }
    }
    getRoomInfo()
  }, [roomNum, isSuggested])

  useEffect(() => {
    // 채팅 환경설정
    const socket = new SockJS(
      'http://ec2-15-165-55-217.ap-northeast-2.compute.amazonaws.com/ws'
    )
    const stompClient = StompJs.over(socket)
    setStompClientstate(stompClient)
    stompClient.connect({ Authorization: TOKEN }, () => {
      // 채팅방 번호 설정
      const roomNum = roomInfo?.roomId
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
          `/api/dm/message?roomId=${roomInfo?.roomId}&dmId=${lastDmNum}`
        )
        console.log(res)

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

  // 채팅방 입장시 첫 번째 채팅방 선택되어 있게끔 만들기
  useEffect(() => {
    if (dmMessageList.length > 0 && lastDmRef.current) {
      lastDmRef.current.focus()
    }
  }, [dmMessageList])

  // 메시지 전송
  const sendMessage = () => {
    const roomNum = roomInfo?.roomId
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
    const roomNum = roomInfo?.roomId
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
      const res = await axiosInstance.put(
        `/api/dm/room/exit/${roomInfo?.roomId}`
      )
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

  // 거래창 모달 띄우기 및 금액 전달
  const deal = (price: string) => {
    priceRef.current = Number(price)
    setIsDealModalOpen(true)
  }

  // 거래 제안 취소하기
  const dealCancel = async () => {
    try {
      const res = await axiosInstance.post(
        `/api/point/suggest/cancel/${roomInfo?.roomId}`
      )
      if (res.status === 200) {
        setIsSuggested(false)
        console.log('거래 취소에 대한 응답 : ', res)
      }
    } catch (error) {
      console.log('거래 취소에 대한 에러 : ', error)
    }
  }

  console.log('룸 정보 : ', roomInfo)
  console.log('룸 suggest 정보 : ', isSuggested)

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
                    <button type="button" onClick={dealCancel}>
                      취소하기
                    </button>
                  </S.Suggestion>
                )}
                {item.writer !== nowNickname && (
                  <S.Suggestion $isMe={item.writer !== nowNickname}>
                    <S.SuggestionTitle>
                      {item.message}원을 제안하셨습니다.
                    </S.SuggestionTitle>
                    <S.SuggestionButton type="button" $isAccept={false}>
                      거절
                    </S.SuggestionButton>
                    <S.SuggestionButton
                      type="button"
                      $isAccept
                      onClick={() => deal(item.message)}
                    >
                      수락
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
          roomId={roomInfo?.roomId}
          setIsSuggested={setIsSuggested}
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
      {isDealModalOpen && (
        <DealModal
          price={priceRef.current}
          roomId={roomInfo?.roomId}
          setIsDealModalOpen={setIsDealModalOpen}
        />
      )}
    </S.Container>
  )
}

export default ChatRoom
