import axios from 'axios'

const API_BASE_URL = 'http://localhost:8080/api'
// 서버에서 받아온 안전한 accountToken 사용
const API_TOKEN = ''

const axiosInstance = axios.create({
  //   baseURL: API_BASE_URL,
  headers: {
    Authorization: `Bearer ${API_TOKEN}`,
  },
  withCredentials: true,
})

export default axiosInstance
