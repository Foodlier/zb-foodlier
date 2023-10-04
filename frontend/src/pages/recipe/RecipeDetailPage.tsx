import styled from 'styled-components'
import Header from '../../components/Header'
import BottomNavigation from '../../components/BottomNavigation'
import DetailMainItem from '../../components/recipe/detail/DetailMain'
import DetailIngredients from '../../components/recipe/detail/DetailIngredients'
import DetailProcedure from '../../components/recipe/detail/DetailProcedure'

export const DetailContainer = styled.div`
  width: 100%;
  padding: 0 2%;
`

const RecipeDetailPage = () => {
  const recipe = {
    recipeId: '',
    memberId: '',
    nickname: '닉네임',
    profileUrl: '/src/assets/chef.svg',
    mainImageUrl: '/src/assets/contents/food_img_05.jpg',
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
      cookingOrderImageUrl: '/src/assets/contents/food_img_03.jpg',
      cookingOrder:
        'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galley',
    },
    {
      cookingOrderImageUrl: '/src/assets/contents/food_img_04.jpg',
      cookingOrder:
        'Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has',
    },
    {
      cookingOrderImageUrl: '/src/assets/contents/food_img_05.jpg',
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
      </DetailContainer>

      <BottomNavigation />
    </>
  )
}

export default RecipeDetailPage
