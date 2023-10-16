import { useEffect, useState } from 'react'
import styled from 'styled-components'
import Header from '../../components/Header'
import BottomNavigation from '../../components/BottomNavigation'
import DetailMainItem from '../../components/recipe/detail/DetailMain'
import DetailIngredients from '../../components/recipe/detail/DetailIngredients'
import DetailProcedure from '../../components/recipe/detail/DetailProcedure'
import RecipeComment from '../../components/recipe/detail/comment/RecipeComment'
import RecipeReviewList from '../../components/recipe/detail/review/RecipeReviewList'
import axiosInstance from '../../utils/FetchCall'
import { Recipe } from '../../constants/Interfacs'

export const DetailContainer = styled.div`
  width: 100%;
  padding: 0 2%;
`

const RecipeDetailPage = () => {
  const [isLoadng, setIsLoading] = useState(true)
  const [recipeData, setRecipeData] = useState<Recipe | undefined>()

  const getRecipe = async () => {
    const recipeId = 1
    try {
      // 현재 List 조회 API X -> 추후 id 받아오는 형식으로 수정 필요
      const res = await axiosInstance.get(`/recipe/${recipeId}`)

      if (res.status === 200) {
        setRecipeData(res.data)
        console.log('ss', recipeData)
        setIsLoading(false)
      }
    } catch (error) {
      // 추후 Error code를 통한 에러 처리 구현 예정
      console.log(error)
    }
  }

  useEffect(() => {
    getRecipe()
  }, [])

  // 로딩 component 구현 필요
  if (isLoadng) return null

  return (
    <>
      <Header />

      <DetailContainer>
        <DetailMainItem recipe={recipeData} />
        <DetailIngredients ingredients={recipeData?.recipeIngredientDtoList} />
        <DetailProcedure detail={recipeData?.recipeDetailDtoList} />
        <RecipeComment />
        <RecipeReviewList />
      </DetailContainer>

      <BottomNavigation />
    </>
  )
}

export default RecipeDetailPage
