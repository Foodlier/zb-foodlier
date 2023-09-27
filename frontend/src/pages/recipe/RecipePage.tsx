import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import * as R from '../../styles/recipe/RecipePage.styled'
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
      <R.Container>
        <R.WrapFilter>
          <R.FilterSelect onChange={handleSelect}>
            {selectList.map(select => (
              <R.FilterOption key={select} value={select}>
                {select}
              </R.FilterOption>
            ))}
          </R.FilterSelect>
        </R.WrapFilter>
        <R.WrapRecipeItem>
          <RecipeItem />
          <RecipeItem />
          <RecipeItem />
        </R.WrapRecipeItem>
        <R.WritePage onClick={navigateToWriteRecipe}>
          + 꿀조합 레시피 작성하기
        </R.WritePage>
      </R.Container>
      <BottomNavigation />
    </>
  )
}

export default RecipePage
