import axios, { AxiosError } from 'axios'

const API_BASE_URL = 'http://localhost:8080'
const API_TOKEN =
  'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzZW9uLm8ubWluQGdtYWlsLmNvbSIsImp0aSI6IjEiLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaWF0IjoxNzI4NTA3MDc4LCJleHAiOjE3Mjg1MTA2Nzh9.pysxYwI8nkeo4rViwiSdItUYNXTI68hhzxGa3uZd5CS1f1ALWCYQwQTS0b082HlNElZBeNyfSEjB-9YfDJqvMQ'

const axiosInstance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
    Authorization: `Bearer ${API_TOKEN}`,
  },
  withCredentials: true,
})

export default axiosInstance
