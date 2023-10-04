/* eslint-disable import/no-extraneous-dependencies */
/* eslint-disable import/prefer-default-export */
// msw.js 또는 msw.ts 파일에서 핸들러를 추가합니다.
import { rest } from 'msw'

// MSW 핸들러 등록
const handlers = [
  // GET /api/recipe/:id 요청에 대한 핸들러
  rest.get('/api/recipe/:id', (req, res, ctx) => {
    // 요청에서 ":id" 부분을 가져옵니다.
    const { id } = req.params

    // 가짜 응답 데이터를 정의합니다.
    return res(
      ctx.status(200), // 상태 코드 200 (OK)
      ctx.json({ message: `Mocked response for recipe with id: ${id}` }) // 가짜 응답 데이터
    )
  }),
]

export { handlers }
