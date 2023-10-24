import { useState } from 'react'
import * as S from '../../styles/recipe/RecipeItem.styled'
import { palette } from '../../constants/Styles'
import useIcon from '../../hooks/useIcon'
import { RecipeListItem } from '../../constants/Interfaces'
import axiosInstance from '../../utils/FetchCall'

interface RecipeItemProps {
  recipeItem: RecipeListItem
  onClick: () => void
}

function RecipeItem({ recipeItem, onClick }: RecipeItemProps) {
  const { IcFavorite, IcFavoriteFill } = useIcon()

  const [isLike, setIsLike] = useState(recipeItem.isHeart)
  const [likeCount, setLikeCount] = useState(recipeItem.heartCount)

  const postLike = async () => {
    const res = await axiosInstance.post(
      `/api/recipe/heart/${recipeItem.recipeId}`
    )
    console.log(res)
  }

  const deleteLike = async () => {
    const res = await axiosInstance.delete(
      `/api/recipe/heart/${recipeItem.recipeId}`
    )
    console.log(res)
  }

  const onClickLikeButton = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault()
    if (isLike) {
      deleteLike()
      setIsLike(false)
      setLikeCount(likeCount - 1)
    } else {
      postLike()
      setIsLike(true)
      setLikeCount(likeCount + 1)
    }
  }

  return (
    <S.Container>
      <S.Button onClick={onClick}>
        <S.Image
          src={recipeItem.mainImageUrl}
          alt={`${recipeItem.title} 미리보기 이미지`}
        />
        <S.Content>
          <S.Title>{recipeItem.title}</S.Title>
          <S.Introduce>{recipeItem.content}</S.Introduce>
        </S.Content>
      </S.Button>
      <S.LikeButton onClick={onClickLikeButton}>
        {isLike ? (
          <IcFavoriteFill size={2} color={palette.main} />
        ) : (
          <IcFavorite size={2} color={palette.textPrimary} />
        )}
        <S.LikeCount>{likeCount}</S.LikeCount>
      </S.LikeButton>
    </S.Container>
  )
}

export default RecipeItem
