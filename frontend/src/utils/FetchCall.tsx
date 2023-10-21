import axios, { AxiosError } from 'axios'

const API_BASE_URL =
  'http://ec2-15-165-55-217.ap-northeast-2.compute.amazonaws.com/'
// 서버에서 받아온 안전한 accountToken 사용
const API_TOKEN =
  'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzZW9uLm8ubWluQGdtYWlsLmNvbSIsImp0aSI6IjEiLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwidHlwZSI6IkFUIiwiaWF0IjoxNjk3ODY4OTE3LCJleHAiOjE2OTc4NzI1MTd9.Z58KVIvLgWtzpe_MN4C7BRrOVPygAPrkTFeVIQTXZrOPPATVws1-EkS91j7HkaqKd-YML6-1ssm3C3aE8a9rOQ' // 로그인 시 저장되는 token 가져와서 사용하는 방식으로 수정 필요
const REFRESH_TOKEN = ''
const axiosInstance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    Authorization: `Bearer ${API_TOKEN}`,
  },
  withCredentials: true,
})

export async function postFormData(url: string, data: any) {
  try {
    const response = await axiosInstance.post(url, data, {
      headers: {
        'Content-Type': 'multipart/form-data',
        accept: 'application/json;charset=UTF-8',
      },
    })
    return response
  } catch (error) {
    const axiosError = error as AxiosError
    // Handle errors here
    throw axiosError
  }
}

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
