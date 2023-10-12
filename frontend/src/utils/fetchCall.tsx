import axios from 'axios'

const API_BASE_URL = 'http://localhost:8080'
// 서버에서 받아온 안전한 accountToken 사용
const API_TOKEN =
  'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzZW9uLm8ubWluQGdtYWlsLmNvbSIsImp0aSI6IjEiLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNzI4NjEzMjkyLCJleHAiOjE3Mjg2MTY4OTJ9.IyQ36Fl801_YE9r2FkhFzifBTRNCBK_3oryrcrYc9NHmlnaalTo0rkHO95raGCqdBY1ctyVYamwf_kwtXOmPVg'
const axiosInstance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    Authorization: `Bearer ${API_TOKEN}`,
  },
  withCredentials: true,
})

export default axiosInstance
