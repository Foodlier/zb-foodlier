import { RecipeIngredientDtoList } from '../../../constants/Interfaces'
import * as S from '../../../styles/recipe/detail/DetailIngredients.styled'

interface DetailIngredientsProps {
  ingredients: RecipeIngredientDtoList[] | undefined
}

const DetailIngredients = ({ ingredients }: DetailIngredientsProps) => {
  if (!ingredients) return null

  return (
    <S.IngredientsContainer>
      <S.MainTit>재료</S.MainTit>
      <S.IngredientsWrap>
        {ingredients.map(ingredient => (
          <S.IngredientsBox key={ingredient.name}>
            <S.Name>{ingredient.name}</S.Name>
            <S.CountWrap>
              <S.Count>{ingredient.count}</S.Count>
              <S.Unit>{ingredient.unit}</S.Unit>
            </S.CountWrap>
          </S.IngredientsBox>
        ))}
      </S.IngredientsWrap>
    </S.IngredientsContainer>
  )
}

export default DetailIngredients
