import React from 'react'
import styled from 'styled-components'
import SlickSlider from '../components/slider/Slider'
import { slides } from '../components/slider/Slide'
import { breakpoints } from '../constants/Styles'
import WebSearch from '../components/search/WebSearch'
import RecipeItem from '../components/recipe/RecipeItem'
import RecipeData from '../components/recipe/RecipeData'
import Header from '../components/Header'

const SlickContainer = styled.section`
  margin-top: 3rem;
`

const Tit = styled.h1`
  font-size: 2.5rem;
  font-weight: 800;
  padding: 0 5%;

  ${breakpoints.large} {
    font-size: 3rem;
  }
`

const WebSearchWrapper = styled.div`
  display: none;

  ${breakpoints.large} {
    display: block;
  }
`

const WebSearchContainer = styled.div`
  padding: 0 5%;
`

const RecipeContainer = styled.section``
const RecipeTit = styled.section``
const RecipeList = styled.section`
  flex-direction: column;
  display: flex;

  ${breakpoints.large} {
    flex-direction: row;
  }
`

const MainPage = () => {
  const handleSearch = (query: string) => {
    console.log(`WebSearch에서 검색: ${query}`)
  }
  return (
    <>
      {/* 헤더 */}
      <Header />
      {/* 검색 */}
      <WebSearchWrapper>
        <WebSearchContainer>
          <WebSearch onSearch={handleSearch} />
        </WebSearchContainer>
      </WebSearchWrapper>
      {/* 배너 */}
      <SlickContainer>
        <Tit>오늘 이 요리 어때요?</Tit>
        <SlickSlider slides={slides} />
      </SlickContainer>
      {/* 레시피 */}
      <RecipeContainer>
        <RecipeTit>
          <Tit>꿀조합 레시피</Tit>
        </RecipeTit>
        <RecipeList>
          {RecipeData.map(data => (
            <RecipeItem
              key={`recipe-${data.id}`}
              // recipeId={data.id}
              title={data.title}
              nickname={data.nickname}
              introduce={data.introduce}
              imagePath={data.imagePath}
              likeCount={data.likeCount}
              isLike={data.isLike}
            />
          ))}
        </RecipeList>
      </RecipeContainer>
    </>
  )
}

export default MainPage
