import { useState, useEffect, useRef, KeyboardEvent } from 'react'
import SockJS from 'sockjs-client'
import StompJs from 'stompjs'
import { useNavigate } from 'react-router-dom'
import useIcon from '../../hooks/useIcon'
import * as S from '../../styles/chat/ChatRoom.styled'
import ProposalModal from './ProposalModal'
import { RoomInfoInterface } from './RoomListItem'
import ModalWithTwoButton from '../ui/ModalWithTwoButton'
import getTime from '../../utils/getTime'
import axiosInstance from '../../utils/FetchCall'
import DealModal from '../point/DealModal'
import RejectModal from '../point/RejectModal'
import { getCookie } from '../../utils/Cookies'

interface DmMessage {
  roomId: number
  dmId: number
  message: string
  writer: string
  messageType: string
  createdAt: string
}

const ChatRoom = ({ roomNum }: { roomNum: number | undefined }) => {
  const { InitialUserImg } = useIcon()
  const [isProposalModalOpen, setIsProposalModalOpen] = useState(false)
  const [isDealModalOpen, setIsDealModalOpen] = useState(false)
  const [isRejectModalOpen, setIsRejectModalOpen] = useState(false)
  const [isExitModalOpen, setIsExitModalOpen] = useState(false)
  const [dmMessageList, setDmMessageList] = useState<DmMessage[]>([])
  const [message, setMessage] = useState('')
  const [messageHasNext, setMessageHasNext] = useState(false)
  const [lastDmNum, setLastDmNum] = useState(0)
  const [stompClientstate, setStompClientstate] = useState<StompJs.Client>()
  const [roomInfo, setRoomInfo] = useState<RoomInfoInterface>()
  const [nowNickname, setNowNickName] = useState('')
  // 옵저버 관찰 대상
  const observerEl = useRef<HTMLDivElement>(null!)
  // 스크롤
  const scrollRef = useRef<HTMLDivElement>(null!)
  // 채팅창
  const inputllRef = useRef<HTMLInputElement>(null!)
  // 거래 금액 저장
  const priceRef = useRef(0)

  // 로그인 구현시 TOKEN 따로 받아와야 함
  const LoginTOKEN = getCookie('refreshToken')
  const socketTOKEN = `Bearer ${LoginTOKEN}`

  // 내 정보 가져오기
  const getMyInfo = async () => {
    try {
      const res = await axiosInstance.get('/api/profile/private')
      if (res.status === 200) {
        setNowNickName(res.data.nickName)
      }
    } catch (error) {
      console.log(error)
    }
  }

  // 방 정보 가져오기
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

  // 방 정보 새로 가져오기(방 클릭시 마다)
  useEffect(() => {
    getRoomInfo()
    getMyInfo()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [roomNum])

  //  DM 리스트 가져오기
  const getDmMessage = async () => {
    if (messageHasNext) {
      try {
        const res = await axiosInstance.get(
          `/api/dm/message?roomId=${roomNum}&dmId=${lastDmNum}`
        )
        if (res.status === 200) {
          setMessageHasNext(res.data.hasNext)
          setDmMessageList(prevMessages =>
            prevMessages.concat(res.data.messageList)
          )
          setLastDmNum(
            res.data.messageList[res.data.messageList.length - 1].dmId
          )
        }
      } catch (error) {
        console.log(error)
      }
    }
  }

  // 최초 DM 리스트 가져오기
  const firstGetDm = async () => {
    try {
      const res = await axiosInstance.get(
        `/api/dm/message?roomId=${roomNum}&dmId=${0}`
      )
      setMessageHasNext(res.data.hasNext)
      if (res.status === 200) {
        setDmMessageList(prevMessages =>
          prevMessages.concat(res.data.messageList)
        )
        setLastDmNum(res.data.messageList[res.data.messageList.length - 1].dmId)
      }
    } catch (error) {
      console.log(error)
    }
  }

  // 채팅 전 나눴던 DM 리스트 맨처음 부분 가져오기 (방 클릭시 마다)
  useEffect(() => {
    firstGetDm()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [roomNum])

  // 방 정보 새로 업데이트 될 때 마다(방 클릭시 마다)
  useEffect(() => {
    // 채팅 환경설정
    const socket = new SockJS(
      'http://ec2-15-165-55-217.ap-northeast-2.compute.amazonaws.com/ws'
    )
    const stompClient = StompJs.over(socket)
    setStompClientstate(stompClient)
    // 메세지 수신
    stompClient?.connect({ Authorization: socketTOKEN }, () => {
      // 채팅방 번호 입력
      // const roomNum = roomInfo?.roomId
      stompClient?.subscribe(`/sub/message/${roomNum}`, comeMessage => {
        // 새로운 메시지 도착 시 호출될 콜백 함수
        const newMessage = JSON.parse(comeMessage.body)
        setDmMessageList(prevMessages => [newMessage, ...prevMessages])
        // 여기에 스크롤바 하단 고정 메소드 추가
      })
    })

    return () => {
      // 소켓 리셋
      socket.close()
      // 디엠방 리셋
      setDmMessageList([])
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [roomNum])

  // 메세지 로딩시 필요
  useEffect(() => {
    // 무한 스크롤 옵저버
    const observer = new IntersectionObserver(
      (entries, observe) => {
        if (entries[0].isIntersecting) {
          if (messageHasNext) {
            getDmMessage()
          }
          observe.unobserve(entries[0].target)
        }
      },
      { threshold: 1 }
    )

    if (dmMessageList.length > 0) {
      // // 관찰 대상 지정
      observer.observe(observerEl.current)
    }

    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [dmMessageList])

  // 메시지 전송
  const sendMessage = (answer: string) => {
    let newMessage = {
      content: '',
      sender: nowNickname,
    }
    if (answer === 'reject') {
      newMessage.content = '해당 제안을 거절하였습니다'
      if (stompClientstate) {
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
    } else if (answer === 'approve') {
      newMessage.content = '해당 제안을 수락하였습니다.'
      if (stompClientstate) {
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
    } else if (answer === 'out') {
      newMessage.content = '상대방이 채팅방을 나가셨습니다.'
      if (stompClientstate) {
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
    } else {
      newMessage = {
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
    }
    // 채팅시 스크롤 맨 밑으로 고정시키기
    scrollRef.current.scrollTop = scrollRef.current.scrollHeight
    setMessage('')
    inputllRef.current.focus()
  }

  // 가격 제안
  const sendSuggestion = (price: number) => {
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
  const navigate = useNavigate()
  const leaveRoom = async () => {
    try {
      sendMessage('out')
      const res = await axiosInstance.put(
        `/api/dm/room/exit/${roomInfo?.roomId}`
      )
      if (res.status === 200) {
        navigate('/')
      }
    } catch (error) {
      console.log(error)
    }
  }

  // Enter로 메세지 보내기
  const handleKeyUp = (e: KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      sendMessage('message')
    }
  }

  // 거래창 모달 띄우기 및 금액 전달
  const deal = (price: string) => {
    priceRef.current = Number(price)
    setIsDealModalOpen(true)
  }

  return (
    <S.Container>
      <S.ChattingHeader>
        <S.FlexAlignCenter>
          {roomInfo?.profileUrl ? (
            <S.ProfileImage src={roomInfo?.profileUrl} $size={4} />
          ) : (
            <InitialUserImg size={4} />
          )}

          <S.Nickname>{roomInfo?.nickname}</S.Nickname>
        </S.FlexAlignCenter>
        <S.FlexAlignCenter>
          {roomInfo?.role === 'requester' && (
            <S.ProposalButton onClick={() => setIsProposalModalOpen(true)}>
              제안하기
            </S.ProposalButton>
          )}
          <S.ExitButton onClick={() => setIsExitModalOpen(true)}>
            채팅방 나가기
          </S.ExitButton>
        </S.FlexAlignCenter>
      </S.ChattingHeader>
      <S.ChattingMessage className="Here" ref={scrollRef}>
        {roomInfo?.exit && (
          <S.ExitMessage>
            상대방이 존재하지 않아 채팅이 불가합니다.
          </S.ExitMessage>
        )}
        {dmMessageList.map(item => (
          <S.WrapMessage key={item.dmId} $isMe={item.writer === nowNickname}>
            {item.messageType === 'CHAT' ? (
              <S.Wrap>
                {!(item.writer === nowNickname) &&
                  (roomInfo?.profileUrl ? (
                    <S.ProfileImage src={roomInfo?.profileUrl} $size={4} />
                  ) : (
                    <InitialUserImg size={4} />
                  ))}
                <S.Message $isMe={item.writer === nowNickname}>
                  {item.message}
                </S.Message>
              </S.Wrap>
            ) : (
              <S.Wrap>
                {!(item.writer === nowNickname) &&
                  (roomInfo?.profileUrl ? (
                    <S.ProfileImage src={roomInfo?.profileUrl} $size={4} />
                  ) : (
                    <InitialUserImg size={4} />
                  ))}
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
                    <S.SuggestionButton
                      type="button"
                      $isAccept={false}
                      onClick={() => setIsRejectModalOpen(true)}
                    >
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
        {dmMessageList.length > 0 && <S.ObserverDiv ref={observerEl} />}
      </S.ChattingMessage>
      <S.WrapInput>
        <S.Input
          value={message}
          onChange={e => setMessage(e.target.value)}
          onKeyUp={e => handleKeyUp(e)}
          disabled={roomInfo?.exit}
          ref={inputllRef}
        />
        <S.Button
          onClick={() => {
            sendMessage('message')
          }}
          disabled={roomInfo?.exit}
        >
          전송
        </S.Button>
      </S.WrapInput>
      {isProposalModalOpen && (
        <ProposalModal
          setIsProposalModalOpen={setIsProposalModalOpen}
          sendSuggestion={sendSuggestion}
          roomId={roomInfo?.roomId}
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
          sendMessage={sendMessage}
          setIsDealModalOpen={setIsDealModalOpen}
        />
      )}
      {isRejectModalOpen && (
        <RejectModal
          roomId={roomInfo?.roomId}
          setIsRejectModalOpen={setIsRejectModalOpen}
          sendMessage={sendMessage}
        />
      )}
    </S.Container>
  )
}

export default ChatRoom
