/* eslint-disable import/prefer-default-export */
import { rest } from 'msw'

const mockData = ['test1', 'test2', 'test3']
const tempEmail = 'bos3321@gmail.com'

const emailAPI = {
  sendVerificationEmail: `/auth/verification/send/${tempEmail}`,
}

export const handlers = [
  rest.get('/test', (_req, res, ctx) => {
    // 가짜 응답 데이터 생성 (원하는대로 수정 가능)
    return res(ctx.status(200), ctx.json(mockData))
  }),

  rest.post('/auth/signup', (_req, res, ctx) => {
    // 가짜 응답 데이터 생성 (원하는대로 수정 가능)
    return res(ctx.status(201))
  }),

  rest.post(emailAPI.sendVerificationEmail, (_req, res, ctx) => {
    const randomCode = Math.floor(Math.random() * 1000000) + 100000
    randomCode.toString()
    return res(
      ctx.status(200),
      ctx.json({
        message: 'Verification email sent successfully',
        email: tempEmail,
        code: randomCode,
      })
    )
  }),
]
