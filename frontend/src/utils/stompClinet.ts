import SockJS from 'sockjs-client'
import StompJs from 'stompjs'

const socket = new SockJS('http://localhost:8080/ws')
const stompClient = StompJs.over(socket)

// eslint-disable-next-line import/prefer-default-export
export { stompClient }
