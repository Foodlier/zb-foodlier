import axios, { AxiosError } from 'axios'

const API_BASE_URL = 'http://localhost:8080'
// 서버에서 받아온 안전한 accountToken 사용
const API_TOKEN =
  // 요리사용
  'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJybGFlaHFAZ21haWwuY29tIiwianRpIjoiMSIsInJvbGVzIjpbIlJPTEVfQ0hFRiJdLCJ0eXBlIjoiQVQiLCJpYXQiOjE3MjkwMzMwMjQsImV4cCI6MTcyOTA3NjIyNH0.H1nMVZGcjlcMwkSmjkSghz3EImVR5quDtwrRhXfSxZmASkDkR4ZHyQn6ZZN5LYWRM4Ent30WSaploceO2mb-Yg'
// 요청계정1
// 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlaHFsczgxOEBuYXZlci5jb20iLCJqdGkiOiIyIiwicm9sZXMiOltdLCJ0eXBlIjoiQVQiLCJpYXQiOjE3MjkwMzMwMjQsImV4cCI6MTcyOTA3NjIyNH0.O2zNkiBwdqX_iYZsQE6O4wkaEzidyqhIR297njtcVgyKBz2i6NXeEPBkA05zI1ahaJHUgrm-kIuhN80wEvnsYQ'
// 요청계정2
// 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXh0Y29va2VyMTAxNEBnbWFpbC5jb20iLCJqdGkiOiIzIiwicm9sZXMiOltdLCJ0eXBlIjoiQVQiLCJpYXQiOjE3MjkwMzMwMjQsImV4cCI6MTcyOTA3NjIyNH0.Hv5VzvxJN9UWnj4NQ9Ssil8kHr3BeeJHgkfDpncRRrRAq-M9dHXe_uId4d-AEjgXpyATdAenVmMHNDJG-zXTQQ'

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
