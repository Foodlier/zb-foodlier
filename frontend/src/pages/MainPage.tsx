import { useRecoilState } from 'recoil'
import { useNavigate } from 'react-router-dom'
import { useEffect, useState } from 'react'
import * as S from '../styles/MainPage.styled'
import SlickSlider from '../components/slider/Slider'
import { palette } from '../constants/Styles'
import useIcon from '../hooks/useIcon'
import Header from '../components/Header'
import BottomNavigation from '../components/BottomNavigation'
import RecipeItem from '../components/recipe/RecipeItem'
import ChefItem, { MainChef } from '../components/chef/ChefItem'
import axiosInstance from '../utils/FetchCall'
import { RecipeListItem } from '../constants/Interfaces'
import { myInfoState } from '../store/recoilState'

const MainPage = () => {
  const { IcAddLight } = useIcon()
  const navigate = useNavigate()

  const [, setProfile] = useRecoilState(myInfoState)
  const [recipeList, setRecipeList] = useState<RecipeListItem[]>([])
  const [recommendList, setRecommendList] = useState<RecipeListItem[]>([])
  const [topChefList, setTopChefList] = useState<MainChef[]>([])

  const navigateToRecipeDetail = (id: number) => {
    navigate(`/recipe/detail/${id}`)
  }

  const getRecipe = async () => {
    try {
      const mainRecipe = await axiosInstance.get('/api/recipe/main')
      const recommendRecipe = await axiosInstance.get('/api/recipe/recommended')
      const chefList = await axiosInstance.get('/api/profile/public/top-chef')

      setRecipeList(mainRecipe.data)
      setRecommendList(recommendRecipe.data)
      setTopChefList(chefList.data)
    } catch (error) {
      console.log(error)
    }
  }

  // 프로필 조회 API
  const getMyProfile = async () => {
    const response = await axiosInstance.get('/api/profile/private')
    if (response.status === 200) {
      setProfile(response.data)
      localStorage.setItem('PROFILE', JSON.stringify(response.data))
    }
  }

  useEffect(() => {
    getRecipe()
    getMyProfile()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  return (
    <>
      {/* Header */}
      <Header />

      {/* 검색 */}
      <S.WebSearchWrapper>
        <S.WebSearchContainer>
          {/* <WebSearch onSearch={handleSearch} /> */}
        </S.WebSearchContainer>
      </S.WebSearchWrapper>

      {/* 배너 */}
      <S.SlickContainer>
        <S.Tit>오늘 이 요리 어때요?</S.Tit>
        <SlickSlider slides={recommendList} />
      </S.SlickContainer>

      {/* 레시피 */}
      <S.RecipeContainer>
        <S.RecipeTit>
          <S.Tit>꿀조합 레시피</S.Tit>
          <S.Button onClick={() => navigate('/recipe')}>
            <IcAddLight size={4} color={palette.textPrimary} />
          </S.Button>
        </S.RecipeTit>
        <S.RecipeList>
          {recipeList.map(recipeItem => (
            <RecipeItem
              key={recipeItem.recipeId}
              recipeItem={recipeItem}
              onClick={() => navigateToRecipeDetail(recipeItem.recipeId)}
            />
          ))}
        </S.RecipeList>
      </S.RecipeContainer>

      {/* TOP 요리사 */}
      <S.ChefContainer>
        <S.ChefTit>
          <S.Tit>TOP 요리사</S.Tit>
        </S.ChefTit>
        <S.ChefList>
          {topChefList.map(item => (
            <ChefItem key={item.memberId} item={item} />
          ))}
        </S.ChefList>
      </S.ChefContainer>

      <BottomNavigation />
    </>
  )
}

export default MainPage
