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
import { get } from '../utils/FetchCall'
import { RecipeListItem } from '../constants/Interfaces'

const MainPage = () => {
  const { IcAddLight } = useIcon()
  const navigate = useNavigate()

  const [recipeList, setRecipeList] = useState<RecipeListItem[]>([])
  const [recommendList, setRecommendList] = useState<RecipeListItem[]>([])
  const [topChefList, setTopChefList] = useState<MainChef[]>([])

  const navigateToRecipeDetail = (id: number) => {
    navigate(`/recipe/detail/${id}`)
  }

  const getRecipe = async () => {
    try {
<<<<<<< HEAD
      const mainRecipe = await axiosInstance.get('/api/recipe/main')
      const recommendRecipe = await axiosInstance.get('/api/recipe/recommended')
      const chefList = await axiosInstance.get('/api/profile/public/top-chef')
=======
      const mainRecipe = await get('/api/recipe/main')
      const recommendRecipe = await get('/api/recipe/recommended')
      const chefList = await get('/api/profile/public/top-chef')
>>>>>>> 747bd847fdeb1b09294e3706221d9b774ca96e1a

      setRecipeList(mainRecipe.data)
      setRecommendList(recommendRecipe.data)
      setTopChefList(chefList.data)
    } catch (error) {
      console.log(error)
    }
  }

  useEffect(() => {
    getRecipe()
<<<<<<< HEAD
    getMyProfile()
    // eslint-disable-next-line react-hooks/exhaustive-deps
=======
>>>>>>> 747bd847fdeb1b09294e3706221d9b774ca96e1a
  }, [])

  return (
    <>
      {/* Header */}
      <Header />

      {/* 배너 */}
      {recommendList.length > 2 && (
        <S.SlickContainer>
          <S.Tit>오늘 이 요리 어때요?</S.Tit>
          <SlickSlider slides={recommendList} />
        </S.SlickContainer>
      )}

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
