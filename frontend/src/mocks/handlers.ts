/* eslint-disable import/no-extraneous-dependencies */
/* eslint-disable import/prefer-default-export */
import { rest } from 'msw'
import users from './dummy.json'

// Path: frontend/src/mocks/handlers.ts
const handlers = [
  rest.get('/', (req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.delay(1000),
      ctx.json({
        message: 'Hello World!',
      })
    )
  }),

  rest.post('/auth/signin', (req, res, ctx) => {
    const { email, password } = req.body as { email: string; password: string }

    const finded = users.find(user => user.email === email)

    if (!finded) {
      return res(ctx.status(401))
    }

    return res(
      ctx.status(200),
      ctx.delay(1000),
      ctx.json({
        email,
        password,
      })
    )
  }),
]

export default handlers
