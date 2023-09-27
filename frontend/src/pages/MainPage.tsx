import React from 'react'
import styled from 'styled-components'
import * as S from '../styles/MainPage.styled'
import SlickSlider from '../components/slider/Slider'
import { slides } from '../components/slider/Slide'
import { palette } from '../constants/Styles'
import useIcon from '../hooks/useIcon'
import Header from '../components/Header'
import WebSearch from '../components/search/WebSearch'
import RecipeItem from '../components/recipe/RecipeItem'
import RecipeData from '../components/recipe/RecipeData'
import ChefItem from '../components/chef/ChefItem'
import ChefData from '../components/chef/ChefData'


const MainPage = () => {
  const { IcAddLight } = useIcon()

  const handleSearch = (query: string) => {
    console.log(`WebSearch에서 검색: ${query}`)
  }

  return (
    <>
      {/* 헤더 */}
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
          {RecipeData.map(data => (
            <RecipeItem
              key={`recipe-${data.recipeId}`}
              recipeId={data.recipeId}
              title={data.title}
              nickname={data.nickname}
              introduce={data.introduce}
              imagePath={data.imagePath}
              likeCount={data.likeCount}
              isLike={data.isLike}
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
              imagePath={data.imagePath}
            />
          ))}
        </S.ChefList>
      </S.ChefContainer>
    </>
  )
}

export default MainPage
