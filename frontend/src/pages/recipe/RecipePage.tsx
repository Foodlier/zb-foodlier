import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import * as S from '../../styles/recipe/RecipePage.styled'
import Header from '../../components/Header'
import BottomNavigation from '../../components/BottomNavigation'
import RecipeItem from '../../components/recipe/RecipeItem'

const RecipePage = () => {
  const navigate = useNavigate()
  const selectList = ['좋아요 많은 순', '댓글 많은 순']

  const [filterSelect, setFilterSelect] = useState('')

  const handleSelect = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setFilterSelect(e.target.value)
  }

  const navigateToWriteRecipe = () => {
    navigate('/recipe/write')
  }

  return (
    <>
      <Header />
      <S.Container>
        <S.WrapFilter>
          <S.FilterSelect onChange={handleSelect}>
            {selectList.map(select => (
              <S.FilterOption key={select} value={select}>
                {select}
              </S.FilterOption>
            ))}
          </S.FilterSelect>
        </S.WrapFilter>
        <S.WrapRecipeItem>
          <RecipeItem />
          <RecipeItem />
          <RecipeItem />
        </S.WrapRecipeItem>
        <S.WritePage onClick={navigateToWriteRecipe}>
          + 꿀조합 레시피 작성하기
        </S.WritePage>
      </S.Container>
      <BottomNavigation />
    </>
  )
}

export default RecipePage
