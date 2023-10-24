import React, { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import * as S from '../../styles/user/UserRecipeItem.styled'
import useIcon from '../../hooks/useIcon'
import axiosInstance from '../../utils/FetchCall'

interface RecipeItem {
  recipeId: number
  title: string
  content: string
  mainImageUrl: string
  heartCount: number
}

interface UserRecipeItemProps {
  EA: number
  memberId: number | undefined
}

const UserRecipeItem: React.FC<UserRecipeItemProps> = ({ EA, memberId }) => {
  const { IcFavoriteFill } = useIcon()
  const [userRecipes, setUserRecipes] = useState<RecipeItem[]>([])
  const navigate = useNavigate()

  const getUserRecipe = async () => {
    try {
      const res = await axiosInstance.get(
        `/api/profile/public/recipe/${0}/${EA}/${memberId}`
      )
      setUserRecipes(res.data.content)
    } catch (error) {
      console.log(error)
    }
  }

  const goToReciepe = (id: number) => {
    navigate(`/recipe/detail/${id}`)
  }

  useEffect(() => {
    getUserRecipe()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  return userRecipes.length > 0 ? (
    <S.MyRecipeList>
      {userRecipes.map(el => (
        <S.RecipeCard
          key={el.recipeId}
          onClick={() => goToReciepe(el.recipeId)}
        >
          <S.RecipeImg src={el.mainImageUrl} alt="" />
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
      ))}
    </S.MyRecipeList>
  ) : (
    <S.NoRecipeCard>작성한 게시물이 없습니다.</S.NoRecipeCard>
  )
}

export default UserRecipeItem
