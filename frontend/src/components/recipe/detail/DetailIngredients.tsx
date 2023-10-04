import * as S from '../../../styles/recipe/detail/DetailIngredients.styled'

interface Ingredient {
  name: string
  count: number
  unit: string
}

interface DetailIngredientsProps {
  ingredients: Ingredient[]
}

function DetailIngredients({ ingredients }: DetailIngredientsProps) {
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
