/* eslint-disable import/prefer-default-export */
import { rest } from 'msw'

export const handlers = [
  rest.post('/auth/signup', (_req, res, ctx) => {
    // 가짜 응답 데이터 생성 (원하는대로 수정 가능)
    return res(
      ctx.status(200),
      ctx.json({ message: '회원가입 성공', data: null })
    )
  }),
]
