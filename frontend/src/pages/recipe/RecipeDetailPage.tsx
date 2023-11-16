/* eslint-disable react-hooks/exhaustive-deps */
import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import styled from 'styled-components'
import Header from '../../components/Header'
import BottomNavigation from '../../components/BottomNavigation'
import DetailMainItem from '../../components/recipe/detail/DetailMain'
import DetailIngredients from '../../components/recipe/detail/DetailIngredients'
import DetailProcedure from '../../components/recipe/detail/DetailProcedure'
import RecipeReviewList from '../../components/recipe/detail/review/RecipeReviewList'
import axiosInstance from '../../utils/FetchCall'
import { Recipe } from '../../constants/Interfaces'
import DetailEditDelete from '../../components/recipe/detail/DetailEditDelete'
import RecipeComment from '../../components/recipe/detail/comment/RecipeComment'
import ModalWithTwoButton from '../../components/ui/ModalWithTwoButton'

export const DetailContainer = styled.div`
  width: 100%;
  padding: 0 2%;
`

const RecipeDetailPage = () => {
  const { id } = useParams()
  const navigate = useNavigate()
  const localProfile = localStorage.getItem('PROFILE')
  const profile = localProfile ? JSON.parse(localProfile) : {}

  const [isModal, setIsModal] = useState(false)
  const [isLoadng, setIsLoading] = useState(true)
  const [recipeData, setRecipeData] = useState<Recipe | undefined>()

  const getRecipe = async () => {
    try {
      const res = await axiosInstance.get(`/api/recipe/detail/${id}`)

      if (res.status === 200) {
        setRecipeData(res.data)
        console.log(res.data)
        setIsLoading(false)
      }
    } catch (error) {
      // 추후 Error code를 통한 에러 처리 구현 예정
      console.log(error)
    }
  }

  const activeModal = () => {
    setIsModal(true)
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
        {recipeData?.memberId === profile.myMemberId && (
          <DetailEditDelete recipeId={recipeData?.recipeId || 0} />
        )}
        <DetailMainItem recipe={recipeData} activeModal={activeModal} />
        <DetailIngredients ingredients={recipeData?.recipeIngredientDtoList} />
        <DetailProcedure detail={recipeData?.recipeDetailDtoList} />
        <RecipeComment
          recipeId={recipeData?.recipeId || 0}
          activeModal={activeModal}
        />
        <RecipeReviewList
          recipeId={recipeData?.recipeId || 0}
          activeModal={activeModal}
        />
      </DetailContainer>
      {isModal && (
        <ModalWithTwoButton
          content="로그인이 필요한 기능입니다."
          subContent="로그인하러 가시겠습니까?"
          setIsModalFalse={() => setIsModal(false)}
          modalEvent={() => {
            setIsModal(false)
            navigate('/login')
          }}
        />
      )}
      <BottomNavigation />
    </>
  )
}

export default RecipeDetailPage
