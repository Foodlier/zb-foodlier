import React, { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import * as S from '../../styles/recipe/RecipePage.styled'
import Header from '../../components/Header'
import BottomNavigation from '../../components/BottomNavigation'
import RecipeItem from '../../components/recipe/RecipeItem'
import axiosInstance from '../../utils/FetchCall'
import { RecipeListItem } from '../../constants/Interfaces'

const RecipePage = () => {
  const navigate = useNavigate()
  const selectList = ['좋아요 많은 순', '댓글 많은 순']

  const [filterSelect, setFilterSelect] = useState('')
  const [recipeList, setRecipeList] = useState<RecipeListItem[]>([])

  const handleSelect = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setFilterSelect(e.target.value)
  }

  const navigateToWriteRecipe = () => {
    navigate('/recipe/write')
  }

  const navigateToRecipeDetail = () => {
    navigate('/recipe/detail')
  }

  const getRecipe = async () => {
    const body = {
      orderType: 'COMMENT_COUNT',
    }
    const { data } = await axiosInstance.get('/recipe/default/0/10', {
      params: body,
    })
    setRecipeList(data.content)
  }

  useEffect(() => {
    getRecipe()
  }, [])

  return (
    <>
      <Header />
      <S.Container>
        <S.WrapFilter>
          <S.FilterSelect onChange={handleSelect} value={filterSelect}>
            {selectList.map(select => (
              <S.FilterOption key={select} value={select}>
                {select}
              </S.FilterOption>
            ))}
          </S.FilterSelect>
        </S.WrapFilter>
        <S.WrapRecipeItem>
          {recipeList.map(recipeItem => (
            <RecipeItem
              key={recipeItem.id}
              recipeItem={recipeItem}
              onClick={navigateToRecipeDetail}
            />
          ))}
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
