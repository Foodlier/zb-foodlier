export interface RecipeListItem {
  recipeId: number
  title: string
  mainImageUrl: string
  content: string
  heartCount: number
  isHeart: boolean
}

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

// 내 정보 - 주소
export interface Address {
  addressDetail: string
  lat: number
  lnt: number
  roadAddress: string
}

// 내 정보
export interface Profile {
  myMemberId: number
  address: Address
  email: string
  nickName: string
  phoneNumber: string
  profileUrl: string
  isChef: boolean
  point: number
}

// 요리사 정보
export interface Chef {
  chefId: number
  distance: number
  introduce: string
  lat: number
  lnt: number
  nickname: string
  profileUrl: string
  recipeCount: number
  reviewCount: number
  starAvg: number
  isQuotation?: number
  quotationId?: number
  requestId?: number
}

// 요청서
export interface RequestForm {
  content: string
  requestFormId: number
  title: string
}

// 주변 요청 정보
export interface Requester {
  distance: 0
  expectedPrice: 0
  lat: 0
  lnt: 0
  mainImageUrl: string
  memberId: 0
  nickname: string
  profileUrl: string
  requestId: 0
  title: string
}

// 요청서 detail
export interface RequestDetail {
  address: string
  addressDetail: string
  content: string
  expectedAt: string
  expectedPrice: number
  heartCount: number
  ingredientList: string[]
  mainImageUrl: string
  recipeId: number
  recipeContent: string
  recipeTitle: string
  requestId: number
  requesterNickname: string
  title: string
}

// 견적서 목록
export interface Quotation {
  content: string
  difficulty: string
  expectedTime: number
  quotationId: number
  title: string
}

// 견적서 detail
export interface QuotationDetail {
  content: string
  difficulty: string
  expectedTime: number
  quotationId: number
  recipeDetailDtoList: string[]
  recipeIngredientDtoList: RecipeIngredientDtoList[]
  title: string
}

// select box
export interface CustomSelectData {
  value: string
  text: string
}

export interface MarkerItem {
  lat: number
  lnt: number
  nickname: string
  starAvg: number
  reviewCount: number
  introduce: string
  [key: string]: string | number
}

export interface NotiItem {
  content: string
  id: number
  notificationType: string
  read: boolean
  sentAt: string
  targetId: number
}

export interface Comment {
  message: string
  recipeId: number
  createdAt: string
}

// 댓글
export interface CommentItem {
  createdAt: string
  id: number
  memberId: number
  message: string
  modifiedAt: string
  nickname: string
  profileImageUrl: string
}
