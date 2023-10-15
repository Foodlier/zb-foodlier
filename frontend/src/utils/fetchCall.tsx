import axios from 'axios'

const API_BASE_URL = 'http://localhost:8080'
// 서버에서 받아온 안전한 accountToken 사용
const API_TOKEN =
  'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzZW9uLm8ubWluQGdtYWlsLmNvbSIsImp0aSI6IjEiLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwidHlwZSI6IkFUIiwiaWF0IjoxNzI4OTI0MDEwLCJleHAiOjE3Mjg5Mjc2MTB9.XIDEqaArm-RzK3-2RsHYXYEGOz7QLCSyncyaFQxbOE5RfC2vQmamsISRVjVVbZCIhnySBACUvIZFYbExazGCQA'
const axiosInstance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    Authorization: `Bearer ${API_TOKEN}`,
  },
  withCredentials: true,
})

export default axiosInstance
