import { useNavigate } from 'react-router-dom'
import { useEffect, useState } from 'react'
import * as S from '../styles/MainPage.styled'
import SlickSlider from '../components/slider/Slider'
import { slides } from '../components/slider/Slide'
import { palette } from '../constants/Styles'
import useIcon from '../hooks/useIcon'
import Header from '../components/Header'
import BottomNavigation from '../components/BottomNavigation'
import WebSearch from '../components/search/WebSearch'
import RecipeItem from '../components/recipe/RecipeItem'
import ChefItem from '../components/chef/ChefItem'
import ChefData from '../components/chef/ChefData'
import { SearchResult } from '../components/search/SearchBar'
import axiosInstance from '../utils/FetchCall'
import { RecipeListItem } from '../constants/Interfaces'

const MainPage = () => {
  const { IcAddLight } = useIcon()
  const navigate = useNavigate()

  const [recipeList, setRecipeList] = useState<RecipeListItem[]>([])

  const handleSearch = (results: SearchResult[]) => {
    console.log(`WebSearch에서 검색 결과:`, results)
  }
  const navigateToRecipeDetail = () => {
    navigate('/recipe/detail')
  }

  const getRecipe = async () => {
    try {
      const { data } = await axiosInstance.get('/recipe/main')
      setRecipeList(data)
    } catch (error) {
      console.log(error)
    }
  }

  useEffect(() => {
    getRecipe()
  }, [])

  return (
    <>
      {/* Header */}
      <Header />

      {/* 검색 */}
      <S.WebSearchWrapper>
        <S.WebSearchContainer>
          <WebSearch onSearch={handleSearch} />
        </S.WebSearchContainer>
      </S.WebSearchWrapper>

      {/* 배너 */}
      <S.SlickContainer>
        <S.Tit>오늘 이 요리 어때요?</S.Tit>
        <SlickSlider slides={slides} />
      </S.SlickContainer>

      {/* 레시피 */}
      <S.RecipeContainer>
        <S.RecipeTit>
          <S.Tit>꿀조합 레시피</S.Tit>
          <IcAddLight size={4} color={palette.textPrimary} />
        </S.RecipeTit>
        <S.RecipeList>
          {recipeList.map(recipeItem => (
            <RecipeItem
              key={recipeItem.id}
              recipeItem={recipeItem}
              onClick={navigateToRecipeDetail}
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
          {ChefData.map(data => (
            <ChefItem
              key={`chef-${data.chefId}`}
              chefId={data.chefId}
              nickname={data.nickname}
              profileUrl={data.profileUrl}
            />
          ))}
        </S.ChefList>
      </S.ChefContainer>

      {/* BottomNavigation */}
      <BottomNavigation />
    </>
  )
}

export default MainPage
