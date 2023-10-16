import axios, { AxiosError } from 'axios'

const API_BASE_URL = 'http://localhost:8080'
// 서버에서 받아온 안전한 accountToken 사용
const API_TOKEN = ''
// 로그인 시 저장되는 token 가져와서 사용하는 방식으로 수정 필요
const REFRESH_TOKEN = ''
const axiosInstance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    Authorization: `Bearer ${API_TOKEN}`,
  },
  withCredentials: true,
})

export async function reissueToken() {
  try {
    const response = await axiosInstance.post('/auth/reissue', null, {
      headers: {
        RefreshToken: `Bearer ${REFRESH_TOKEN}`,
      },
    })
    return response.data
  } catch (error) {
    const axiosError = error as AxiosError
    // Handle errors here
    throw axiosError
  }
}

export default axiosInstance
