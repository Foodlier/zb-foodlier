/* eslint-disable import/no-extraneous-dependencies */
/* eslint-disable import/prefer-default-export */
import { rest } from 'msw'
import people from './dummy.json'

async function sleep(timeout: number) {
  return new Promise(resolve => {
    setTimeout(resolve, timeout)
  })
}

const handlers = [
  rest.get('/', async (req, res, ctx) => {
    await sleep(200)

    return res(ctx.status(200), ctx.json(people))
  }),
  rest.post('/people', async (req, res, ctx) => {
    await sleep(200)
    people.push({
      id: '345',
      name: 'son',
      country: 'asia',
      lang: 'php',
    })

    return res(ctx.status(201), ctx.json(people))
  }),
]

export default handlers
