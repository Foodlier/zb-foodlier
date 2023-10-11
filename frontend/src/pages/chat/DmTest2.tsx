import React, { useState, useEffect } from 'react'
import SockJS from 'sockjs-client'
import StompJs from 'stompjs'
import { stompClient } from '../../utils/stompClinet'

const DmTest2 = () => {
  const [messages, setMessages] = useState([])
  const [input, setInput] = useState('')

  const TOKEN =
    'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlaHFsczgxOEBuYXZlci5jb20iLCJqdGkiOiIyIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTcyODUzNzMyOCwiZXhwIjoxNzI4NTgwNTI4fQ.8raKozKynLVI4AmAc5KGzXvl6iiQHaCn-u-NXmkwdTS7GvL-KRtLq7ueepFD6bKW-lv4yENGtgMu1sgJf-6txg'

  useEffect(() => {
    // SockJS 및 STOMP 클라이언트 연결 설정
    stompClient.connect({ Authorization: TOKEN }, () => {
      // 채팅방 번호 설정
      const roomNum = 1
      stompClient.subscribe(`/sub/message/${roomNum}`, message => {
        // 새로운 메시지 도착 시 호출될 콜백 함수
        console.log('야!')
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
      sender: '요청자',
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

export default DmTest2
