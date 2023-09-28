/* ---------------------레시피------------------------- */

// 레시피 작성 - 난이도 list
export const DIFFICULTY_LIST = [
  {
    content: '어려움',
    value: 'HARD',
  },
  {
    content: '보통',
    value: 'MIDEUM',
  },
  {
    content: '쉬움',
    value: 'EASY',
  },
]

// 레시피 작성 - 조리 재료 list
export const INGREDIENT_LIST = [
  {
    value: 'name',
    placeholder: '재료명',
    width: 50,
    radius: '0.6rem 0 0 0.6rem',
  },
  {
    value: 'count',
    placeholder: '수량',
    width: 25,
    radius: '0',
  },
  {
    value: 'unit',
    placeholder: '단위',
    width: 25,
    radius: '0 0.6rem 0.6rem 0',
  },
]

// 레시피 재료 객체
export const EMPTY_INGREDIENT = {
  index: 0,
  name: '',
  count: 0,
  unit: '',
}

// 레시피 조리 순서 객체
export const EMPTY_ORDER = {
  index: 0,
  image: '',
  content: '',
}
