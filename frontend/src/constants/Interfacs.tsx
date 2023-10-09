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
}
