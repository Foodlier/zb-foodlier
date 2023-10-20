import axios from 'axios'
import { getCookie } from './Cookies'

// TODO: Localhost 대신 EC2 주소로 변경 시 CORS 에러 발생 이슈 논의 필요
// const API_BASE_URL = 'http://ec2-15-165-55-217.ap-northeast-2.compute.amazonaws.com'
// 서버에서 받아온 안전한 accessToken 사용
const API_ACCESS_TOKEN = getCookie('accessToken')
const API_REFRESH_TOKEN = getCookie('refreshToken')

const axiosInstance = axios.create({
  // baseURL: API_BASE_URL,
  headers: {
    Authorization: `Bearer ${API_ACCESS_TOKEN}`,
    RefreshToken: `Bearer ${API_REFRESH_TOKEN}`,
  },
  withCredentials: true,
})

// export async function reissueToken() {
//   try {
//     const response = await axiosInstance.post('/auth/reissue', null, {
//       headers: {
//         RefreshToken: `Bearer ${REFRESH_TOKEN}`,
//       },
//     })
//     return response.data
//   } catch (error) {
//     const axiosError = error as AxiosError
//     // Handle errors here
//     throw axiosError
//   }
// }

export default axiosInstance
