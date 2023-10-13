import axios from 'axios'

const API_BASE_URL = 'http://localhost:8080'
// 서버에서 받아온 안전한 accountToken 사용
const API_TOKEN =
  'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzZW9uLm8ubWluQGdtYWlsLmNvbSIsImp0aSI6IjEiLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwidHlwZSI6IkFUIiwiaWF0IjoxNzI4NzA3ODcwLCJleHAiOjE3Mjg3MTE0NzB9.6jeCxnIabhl4Xc1Kw90AOIBglIMkp2Ksi3niX_Vqb78cVdMi4JW9SgtaKaK0wJkjS6wRzI0-9PbQQR88BG7e7A'
const axiosInstance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    Authorization: `Bearer ${API_TOKEN}`,
  },
  withCredentials: true,
})

export default axiosInstance
