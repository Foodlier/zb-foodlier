import { useEffect, useState } from 'react'
import { RECIPE_OPTION_MENU_LIST } from '../../constants/Data'
import * as S from '../../styles/refrigerator/SearchRecipeModal.styled'
import axiosInstance from '../../utils/FetchCall'
import RecipeItem from '../recipe/RecipeItem'
import { RecipeListItem } from '../../constants/Interfaces'

interface ModalWithoutButtonProps {
  setIsModalFalse: () => void
  setRecipeId: (e: { title: string; id: number }) => void
}
const SearchRecipeModal = ({
  //   content,
  setIsModalFalse,
  setRecipeId,
}: ModalWithoutButtonProps) => {
  const [recipeList, setRecipeList] = useState<RecipeListItem[]>([])
  const [sortSelectValue] = useState(RECIPE_OPTION_MENU_LIST[0])

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

  useEffect(() => {
    getRecipe()
  }, [])

  return (
    <S.ModalBackdrop onClick={() => setIsModalFalse()}>
      <S.Container onClick={e => e.stopPropagation()}>
        <S.WrapList>
          {recipeList.map(item => (
            <RecipeItem
              key={item.recipeId}
              recipeItem={item}
              onClick={() => {
                setRecipeId({ title: item.title, id: item.recipeId })
                setIsModalFalse()
              }}
            />
          ))}
        </S.WrapList>
      </S.Container>
    </S.ModalBackdrop>
  )
}

export default SearchRecipeModal
