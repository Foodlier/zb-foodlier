export interface Recipe {
  content: string
  difficulty: string
  expectedTime: number
  heartCount: number
  mainImageUrl: string
  memberId: number
  nickname: string
  profileUrl: string
  recipeDetailDtoList: RecipeDetailDtoList[]
  recipeId: number
  recipeIngredientDtoList: RecipeIngredientDtoList[]
  title: string
}

export interface RecipeDetailDtoList {
  cookingOrderImageUrl: string
  cookingOrder: string
}

export interface RecipeIngredientDtoList {
  name: string
  count: number
  unit: string
  [key: string]: string | number
}

export interface CommentItem {
  // message: string
  // createdAt: string
  // isDeleted: boolean
  // nickname: string
  // profileUrl: string
  commentId: number
  profileUrl: string
  createdAt: string
  message: string
  nickname: string
}
