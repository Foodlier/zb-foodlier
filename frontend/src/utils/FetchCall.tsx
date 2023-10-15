import axios from 'axios'

const API_BASE_URL = 'http://localhost:8080'
// 서버에서 받아온 안전한 accountToken 사용
const API_TOKEN =
  // 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlaHFsczgxOEBuYXZlci5jb20iLCJqdGkiOiIyIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTcyODg1NTUzMiwiZXhwIjoxNzI4ODk4NzMyfQ.fB8-6erXP1zQ7yq8TFaTJ97BFaGKhh8XQ3tzU61YhSQQmSFf7kKgHn9__ggUg3UmCfBj0_JWnbJazV0r7wCYcg'
  // 아래거가 요리사용(김도빈테스트)
  'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJybGFlaHFAZ21haWwuY29tIiwianRpIjoiMSIsInJvbGVzIjpbIlJPTEVfVVNFUiIsIlJPTEVfQ0hFRiJdLCJpYXQiOjE3Mjg4NTkwMTUsImV4cCI6MTcyODkwMjIxNX0.tPx-ZNgpczdQQuxHiT7t691IeGGAQSCgPMggSSRorFyHvzMu-yIboEAbKGm9YfBK7fHMyv3TD-dMCbnkMqmO5g'

// 닉네임 - 요리사 계정
// 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjb29rZXJ0ZXN0MTAxNUBnbWFpbC5jb20iLCJqdGkiOiIzIiwicm9sZXMiOlsiUk9MRV9VU0VSIiwiUk9MRV9DSEVGIl0sImlhdCI6MTcyODg5MTY5OCwiZXhwIjoxNzI4OTM0ODk4fQ.RZBm2ItpwKduAgbtEd_NgHe_QQtVCr11zBinuvqAV1APBJJTyDlgJFEflahmGSY7RX3KAfo835vxKZpjHd86Yw'

const axiosInstance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    Authorization: `Bearer ${API_TOKEN}`,
  },
  withCredentials: true,
})

export default axiosInstance
