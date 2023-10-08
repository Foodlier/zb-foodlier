import React, { useEffect, useState } from 'react'
import * as S from '../../styles/user/MyRecipeItem.styled'
import useIcon from '../../hooks/useIcon'

interface RecipeItem {
  recipeid: number
  title: string
  content: string
  mainImageUrl: string
  heartCount: number
}

interface MyRecipeItemProps {
  EA: number
}

const MyRecipeItem: React.FC<MyRecipeItemProps> = ({ EA }) => {
  const { IcFavoriteFill } = useIcon()
  const [myRecipes, setMyRecipes] = useState<RecipeItem[]>([])

  useEffect(() => {
    const MY_RECIPES_EXAM = [
      {
        recipeid: 1,
        title: '레시피1',
        content: '나는 레시피1입니다',
        mainImageUrl: '',
        heartCount: 100,
      },
      {
        recipeid: 2,
        title: '레시피2',
        content: '나는 레시피2입니다',
        mainImageUrl: '',
        heartCount: 111,
      },
      {
        recipeid: 3,
        title: '레시피3',
        content: '나는 레시피3입니다',
        mainImageUrl: '',
        heartCount: 123,
      },
      {
        recipeid: 4,
        title: '레시피4',
        content: '나는 레시피4입니다',
        mainImageUrl: '',
        heartCount: 47,
      },
      {
        recipeid: 5,
        title: '레시피5',
        content: '나는 레시피5입니다',
        mainImageUrl: '',
        heartCount: 47,
      },
    ]
    setMyRecipes(MY_RECIPES_EXAM)
  }, [])

  const showedCardList = myRecipes.slice(0, EA)

  const test = a => {
    console.log(a)
  }

  return showedCardList.map(el => (
    <S.RecipeCard key={el.recipeid} onClick={() => test(el.heartCount)}>
      <S.RecipeImg src="" alt="" />
      <S.RecipeInfo>
        <S.RecipeTopInfo>
          <S.RecipeTitle>{el.title}</S.RecipeTitle>
          <S.LikeDiv>
            <IcFavoriteFill size={2} color="#EA5455" />
            <S.LikeCount>{el.heartCount}</S.LikeCount>
          </S.LikeDiv>
        </S.RecipeTopInfo>
        <S.RecipeContent>{el.content}</S.RecipeContent>
      </S.RecipeInfo>
    </S.RecipeCard>
  ))
}

export default MyRecipeItem
