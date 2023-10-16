import axios from 'axios'

const API_BASE_URL = 'http://localhost:8080'
// 서버에서 받아온 안전한 accountToken 사용
const API_TOKEN =
  'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzZW9uLm8ubWluQGdtYWlsLmNvbSIsImp0aSI6IjEiLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwidHlwZSI6IkFUIiwiaWF0IjoxNzI5MDA1ODcxLCJleHAiOjE3MjkwMDk0NzF9._LMQ12QWbWAZOap1gjIo-_56KPOy5yE_3jYkWKvdGu_6vnBb7QNgP5mNLx1upcnpaZxBOfXk0P_gXkLmrhTuJQ'
const axiosInstance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    Authorization: `Bearer ${API_TOKEN}`,
  },
  withCredentials: true,
})
export default axiosInstance
