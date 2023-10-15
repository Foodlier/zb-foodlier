/* ---------------------레시피------------------------- */

// 레시피 작성 - 난이도 list
export const DIFFICULTY_LIST = [
  {
    content: '어려움',
    value: 'HARD',
  },
  {
    content: '보통',
    value: 'MEDIUM',
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
  name: '',
  count: 0,
  unit: '',
}

// 레시피 조리 순서 객체
export const EMPTY_ORDER = {
  cookingOrderImageUrl: '',
  cookingOrder: '',
}

// 냉장고를 부탁해 요리사 조회 시 option list
export const OPTION_MENU_LIST = [
  { value: 'DISTANCE', text: '거리 순' },
  { value: 'RECIPE', text: '레시피 많은 순' },
  { value: 'REVIEW', text: '리뷰 많은 순' },
  { value: 'STAR', text: '별점 높은 순' },
]

// 냉장고를 부탁해 요청 조회 시 option list
export const REQUEST_OPTION_MENU_LIST = [
  { value: 'DISTANCE', text: '거리 순' },
  { value: 'PRICE', text: '가격 순' },
]
