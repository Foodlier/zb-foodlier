export interface Recipe {
  id: string | number
  title: string
  nickname: string
  introduce: string
  imagePath: string
  likeCount: number
  isLike: boolean
}

const RecipeData: Recipe[] = [
  {
    id: 1,
    title: '마크정식',
    nickname: '닉네임1',
    introduce: '요리 소개 1',
    imagePath: '/src/assets/contents/food_img_01.jpg',
    likeCount: 150,
    isLike: true,
  },
  {
    id: 2,
    title: '요리 제목 2',
    nickname: '닉네임2',
    introduce: '요리 소개 2',
    imagePath: '/src/assets/contents/food_img_02.jpg',
    likeCount: 200,
    isLike: false,
  },
  {
    id: 3,
    title: '요리 제목 3',
    nickname: '닉네임3',
    introduce: '요리 소개 3',
    imagePath: '/src/assets/contents/food_img_03.jpg',
    likeCount: 200,
    isLike: false,
  },
]

export default RecipeData
