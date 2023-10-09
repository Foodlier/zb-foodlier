import styled from 'styled-components'
import Header from '../../components/Header'
import BottomNavigation from '../../components/BottomNavigation'
import DetailMainItem from '../../components/recipe/detail/DetailMain'
import DetailIngredients from '../../components/recipe/detail/DetailIngredients'
import DetailProcedure from '../../components/recipe/detail/DetailProcedure'
import RecipeCommentList from '../../components/recipe/detail/comment/RecipeComment'
import RecipeReviewList from '../../components/recipe/detail/review/RecipeReviewList'

const comments = [
  {
    message: '댓글 내용 1',
    createdAt: '2023-10-09',
    isDeleted: false,
    nickname: '유저1',
    profileUrl: 'https://source.unsplash.com/random/50x50/?person',
  },
  {
    message: '댓글 내용 2',
    createdAt: '2023-10-10',
    isDeleted: false,
    nickname: '유저2',
    profileUrl: 'https://source.unsplash.com/random/50x50/?person',
  },
  // 댓글 목록 계속 추가
]

export const DetailContainer = styled.div`
  width: 100%;
  padding: 0 2%;
`

const RecipeDetailPage = () => {
  const recipe = {
    recipeId: '',
    memberId: '',
    nickname: '닉네임',
    profileUrl: '/images/chef.svg',
    mainImageUrl: '/images/contents/food_img_05.jpg',
    title: '레시피 제목',
    content:
      'Lorem Ipsum is simply dummy text of the printing  and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the  1500s, when an unknown printer took a galley',
    difficulty: '어려움',
    expectedTime: '30분',
  }
  const ingredients = [
    {
      name: '재료1',
      count: 2,
      unit: '개',
    },
    {
      name: '재료2',
      count: 200,
      unit: 'g',
    },
    {
      name: '재료3',
      count: 1,
      unit: '팩',
    },
  ]

  const detail = [
    {
      cookingOrderImageUrl: '/images/contents/food_img_03.jpg',
      cookingOrder:
        'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galley',
    },
    {
      cookingOrderImageUrl: '/images/contents/food_img_04.jpg',
      cookingOrder:
        'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has',
    },
    {
      cookingOrderImageUrl: '/images/contents/food_img_05.jpg',
      cookingOrder: 'Lorem Ipsum is simply dummy',
    },
  ]

  return (
    <>
      <Header />

      <DetailContainer>
        <DetailMainItem recipe={recipe} />
        <DetailIngredients ingredients={ingredients} />
        <DetailProcedure detail={detail} />
        <RecipeCommentList comments={comments} />
        <RecipeReviewList />
      </DetailContainer>

      <BottomNavigation />
    </>
  )
}

export default RecipeDetailPage
