import { useState, useEffect } from 'react'
import SockJS from 'sockjs-client'
import StompJs from 'stompjs'
import axiosInstance from '../../utils/FetchCall'

interface DmMessage {
  roomId: number
  dmId: number
  message: string
  writer: string
  messageType: string
  createdAt: string
}

const DmTest2 = () => {
  const [messages, setMessages] = useState<DmMessage[]>([])
  const [inputState, setInputState] = useState('')
  const [stompClientstate, setStompClientstate] = useState<StompJs.Client>()

  const TOKEN =
    'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlaHFsczgxOEBuYXZlci5jb20iLCJqdGkiOiIyIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTcyODg1NTUzMiwiZXhwIjoxNzI4ODk4NzMyfQ.fB8-6erXP1zQ7yq8TFaTJ97BFaGKhh8XQ3tzU61YhSQQmSFf7kKgHn9__ggUg3UmCfBj0_JWnbJazV0r7wCYcg'

  useEffect(() => {
    // SockJS 및 STOMP 클라이언트 연결 설정
    const socket = new SockJS('http://localhost:8080/ws')
    const stompClient = StompJs.over(socket)
    setStompClientstate(stompClient)
    stompClient.connect({ Authorization: TOKEN }, () => {
      // 채팅방 번호 설정
      const roomNum = 2
      stompClient.subscribe(`/sub/message/${roomNum}`, message => {
        // 새로운 메시지 도착 시 호출될 콜백 함수
        const newMessage = JSON.parse(message.body)
        setMessages(prevMessages => [...prevMessages, newMessage])
      })
    })
    return () => {
      socket.close()
    }
  }, [])

  const sendMessage = (input: string | number) => {
    // 메시지 전송 로직
    // 예: 채팅방 번호 1번에 메시지 보내기
    const roomNum = 2
    const newMessage = {
      content: input,
      sender: '테스트김도빈',
    }
    if (stompClientstate) {
      stompClientstate.send(
        `/pub/message/${roomNum}`,
        {},
        JSON.stringify({
          roomId: roomNum,
          message: newMessage.content,
          writer: newMessage.sender,
          messageType: 'CHAT',
        })
      )
    }
    setInputState('')
  }

  // 채팅방 나가기
  const leaveRoom = async () => {
    try {
      sendMessage('상대방이 채팅방을 퇴장하셨습니다.')
      const res = await axiosInstance.put(`/dm/room/exit/4`)
      if (res.status === 200) {
        console.log(res)
      }
    } catch (error) {
      console.log(error)
    }
  }

  return (
    <div>
      <ul>
        {messages.map(message => (
          <li key={message.dmId}>
            <strong>{message.writer}: </strong>
            {message.message}
          </li>
        ))}
      </ul>

      <input
        type="text"
        value={inputState}
        onChange={e => setInputState(e.target.value)}
      />
      <button type="button" onClick={() => sendMessage(inputState)}>
        Sendㅡ
      </button>
      <button type="button" onClick={leaveRoom}>
        leave
      </button>
    </div>
  )
}

export default DmTest2
