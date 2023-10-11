import React, { useState, useEffect } from 'react'
import SockJS from 'sockjs-client'
import StompJs from 'stompjs'
import { stompClient } from '../../utils/stompClinet'

const DmTest1 = () => {
  const [messages, setMessages] = useState<string[]>([])
  const [input, setInput] = useState('')

  const TOKEN =
    'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJybGFlaHFAZ21haWwuY29tIiwianRpIjoiMSIsInJvbGVzIjpbIlJPTEVfVVNFUiIsIlJPTEVfQ0hFRiJdLCJpYXQiOjE3Mjg1NDU4MDAsImV4cCI6MTcyODU4OTAwMH0.5g-pdCpmfNhuj3eY8I6eV1UJbuNlXm__X_vh9zrAUaAzwiicQP_8E-bpwnLJLL15AKS729AP5X6OrwRDqkwkyQ'

  useEffect(() => {
    // SockJS 및 STOMP 클라이언트 연결 설정
    stompClient.connect({ Authorization: TOKEN }, () => {
      // 채팅방 번호 설정 (1번 채팅방)
      const roomNum = 1
      stompClient.subscribe(`/sub/message/${roomNum}`, message => {
        // 새로운 메시지 도착 시 호출될 콜백 함수
        const newMessage = JSON.parse(message.body)
        setMessages(prevMessages => [...prevMessages, newMessage])
      })
    })
  }, [])

  const sendMessage = () => {
    // 메시지 전송 로직
    // 예: 채팅방 번호 1번에 메시지 보내기
    const roomNum = 1
    const newMessage = {
      content: input,
      sender: '요리사',
    }
    stompClient.send(
      `/pub/message/${roomNum}`,
      {},
      JSON.stringify({
        roomId: roomNum,
        message: newMessage.content,
        writer: newMessage.sender,
        messageType: 'CHAT',
      })
    )
    setInput('')
  }

  return (
    <div>
      <ul>
        {messages.map((message, index) => (
          <li key={index}>
            <strong>{message.writer}: </strong>
            {message.message}
          </li>
        ))}
      </ul>

      <input
        type="text"
        value={input}
        onChange={e => setInput(e.target.value)}
      />
      <button type="button" onClick={sendMessage}>
        Send
      </button>
    </div>
  )
}

export default DmTest1
