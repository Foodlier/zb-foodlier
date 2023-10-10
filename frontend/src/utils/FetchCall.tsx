import axios from 'axios'

const API_BASE_URL = 'http://localhost:8080'
// 서버에서 받아온 안전한 accountToken 사용
const API_TOKEN =
  'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJybGFlaHFAZ21haWwuY29tIiwianRpIjoiMSIsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJpYXQiOjE3Mjg1MTU1NDgsImV4cCI6MTcyODUxOTE0OH0.1lqykwd47xQcufLPmDTnGZKBddyOn6ukpwdK2rIm0BZO9c9iCG36sIl-FEakNvnPcntCfuNgpHELjtOe2rTcpA'

const axiosInstance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    Authorization: `Bearer ${API_TOKEN}`,
  },
  withCredentials: true,
})

export default axiosInstance
