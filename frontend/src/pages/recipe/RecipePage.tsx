/* eslint-disable react-hooks/exhaustive-deps */
import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import * as S from '../../styles/recipe/RecipePage.styled'
import Header from '../../components/Header'
import BottomNavigation from '../../components/BottomNavigation'
import RecipeItem from '../../components/recipe/RecipeItem'
import axiosInstance from '../../utils/FetchCall'
import { RecipeListItem } from '../../constants/Interfaces'
import SearchBar from '../../components/search/SearchBar'
import CustomSelect from '../../components/ui/CustomSelect'
import {
  RECIPE_OPTION_MENU_LIST,
  RECIPE_TYPE_MENU_LIST,
} from '../../constants/Data'

const RecipePage = () => {
  const navigate = useNavigate()

  const [searchValue, setSearchValue] = useState('')
  const [recipeList, setRecipeList] = useState<RecipeListItem[]>([])
  const [sortSelectValue, setSortSelectValue] = useState(
    RECIPE_OPTION_MENU_LIST[0]
  )
  const [typeSelectValue, setTypeSelectValue] = useState(
    RECIPE_TYPE_MENU_LIST[0]
  )

  const navigateToWriteRecipe = () => {
    navigate('/recipe/write')
  }

  const navigateToRecipeDetail = (id: number) => {
    navigate(`/recipe/detail/${id}`)
  }

  // 전체 레시피 조회 API
  const getRecipe = async () => {
    const pageIdx = 0
    const pageSize = 20
    const body = {
      orderType: sortSelectValue.value,
    }
    const { data } = await axiosInstance.get(
      `/api/recipe/default/${pageIdx}/${pageSize}`,
      {
        params: body,
      }
    )
    console.log(data)
    setRecipeList(data.content)
  }

  // 검색어에 맞는 레시피 리스트 조회
  const searchRecipe = async () => {
    const pageIdx = 0
    const pageSize = 20

    const res = await axiosInstance.get(
      `/api/recipe/search/${typeSelectValue.value}/${sortSelectValue.value}/${pageIdx}/${pageSize}`,
      {
        params: { searchText: searchValue },
      }
    )
    console.log(res)
    setRecipeList(res.data.content)
  }

  // input 클릭 / enter 시 호출되는 함수
  const onClickSearchButton = () => {
    if (searchValue) {
      searchRecipe()
    } else {
      getRecipe()
    }
  }

  useEffect(() => {
    onClickSearchButton()
  }, [sortSelectValue])

  return (
    <>
      <Header />
      <S.Container>
        <SearchBar
          onClickSearchButton={onClickSearchButton}
          typeSelectValue={typeSelectValue}
          setTypeSelectValue={setTypeSelectValue}
          searchValue={searchValue}
          setSearchValue={setSearchValue}
        />

        <S.WrapFilter>
          <CustomSelect
            data={RECIPE_OPTION_MENU_LIST}
            setCurrentSelectValue={setSortSelectValue}
            currentSelectValue={sortSelectValue}
          />
        </S.WrapFilter>
        <S.WrapRecipeItem>
          {recipeList.map(recipeItem => (
            <RecipeItem
              key={recipeItem.recipeId}
              recipeItem={recipeItem}
              onClick={() => navigateToRecipeDetail(recipeItem.recipeId)}
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
