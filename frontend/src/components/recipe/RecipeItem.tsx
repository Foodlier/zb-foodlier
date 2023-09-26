import React, { useState } from 'react'
import * as S from '../../styles/recipe/RecipeItem.styled'
import { palette } from '../../constants/Styles'
import useIcon from '../../hooks/useIcon'

interface RecipeItemProps {
  // recipeId: string | number
  title: string
  nickname: string
  introduce: string
  imagePath: string
  likeCount: number
  isLike: boolean
}

const RecipeItem: React.FC<RecipeItemProps> = ({
  // recipeId,
  title,
  nickname,
  introduce,
  imagePath,
  likeCount,
  isLike,
}) => {
  const { IcFavorite, IcFavoriteFill } = useIcon()
  const [isLiked, setIsLiked] = useState(isLike)
  const [likes, setLikes] = useState(likeCount)

  const handleLikeClick = () => {
    setIsLiked(!isLiked)
    setLikes(isLiked ? likes - 1 : likes + 1)
  }

  return (
    <S.Container>
      <S.Image src={imagePath} alt={title} />
      <S.WrapContent>
        <S.FlexRowJustiBet>
          <S.FlexRow>
            <S.Title>{title}</S.Title>
            <S.Nickname>{nickname}</S.Nickname>
          </S.FlexRow>
          <S.LikeButton onClick={handleLikeClick}>
            {isLiked ? (
              <IcFavoriteFill size={2} color="#EA5455" />
            ) : (
              <IcFavorite size={2} color={palette.textPrimary} />
            )}
            <S.LikeCount>{likes}</S.LikeCount>
          </S.LikeButton>
        </S.FlexRowJustiBet>
        <S.Introduce>{introduce}</S.Introduce>
      </S.WrapContent>
    </S.Container>
  )
}

export default RecipeItem
