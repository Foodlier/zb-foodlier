import React, { useState, useEffect } from 'react'
import SockJS from 'sockjs-client'
import StompJs from 'stompjs'

const DmTest2 = () => {
  const [messages, setMessages] = useState<string[]>([])
  const [input, setInput] = useState('')
  const [stompClientstate, setStompClientstate] = useState<StompJs.Client>()
  const TOKEN =
    'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlaHFsczgxOEBuYXZlci5jb20iLCJqdGkiOiIyIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTcyODUzNzMyOCwiZXhwIjoxNzI4NTgwNTI4fQ.8raKozKynLVI4AmAc5KGzXvl6iiQHaCn-u-NXmkwdTS7GvL-KRtLq7ueepFD6bKW-lv4yENGtgMu1sgJf-6txg'

  useEffect(() => {
    // SockJS 및 STOMP 클라이언트 연결 설정
    const socket = new SockJS('http://localhost:8080/ws')
    const stompClient = StompJs.over(socket)
    setStompClientstate(stompClient)
    stompClient.connect({ Authorization: TOKEN }, () => {
      // 채팅방 번호 설정
      const roomNum = 1
      stompClient.subscribe(`/sub/message/${roomNum}`, message => {
        // 새로운 메시지 도착 시 호출될 콜백 함수
        const newMessage = JSON.parse(message.body)
        setMessages(prevMessages => [...prevMessages, newMessage])
      })
    })
    // return () => {
    //   stompClient.disconnect(() => {
    //     console.log('연결 끊김')
    //   })
    // }
  }, [])

  const sendMessage = () => {
    // 메시지 전송 로직
    // 예: 채팅방 번호 1번에 메시지 보내기
    const roomNum = 1
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
