import { useState } from 'react'
import * as S from '../../styles/recipe/RecipeItem.styled'
import { palette } from '../../constants/Styles'
import useIcon from '../../hooks/useIcon'
import { RecipeListItem } from '../../constants/Interfaces'

interface RecipeItemProps {
  recipeItem: RecipeListItem
  onClick: () => void
}

function RecipeItem({ recipeItem, onClick }: RecipeItemProps) {
  const { IcFavorite, IcFavoriteFill } = useIcon()

  const [isLike, setIsLike] = useState(recipeItem.heart)
  const [likeCount, setLikeCount] = useState(recipeItem.heartCount)

  const onClickLikeButton = () => {
    setIsLike(!isLike)
    setLikeCount(isLike ? likeCount - 1 : likeCount + 1)
  }

  return (
    <S.Container onClick={onClick}>
      <S.Image
        src={recipeItem.mainImageUrl}
        alt={`${recipeItem.title} 미리보기 이미지`}
      />
      <S.WrapContent>
        <S.FlexRowJustiBet>
          <S.FlexRow>
            <S.Title>{recipeItem.title}</S.Title>
          </S.FlexRow>
          <S.LikeButton onClick={onClickLikeButton}>
            {isLike ? (
              <IcFavoriteFill size={2} color="#EA5455" />
            ) : (
              <IcFavorite size={2} color={palette.textPrimary} />
            )}
            <S.LikeCount>{likeCount}</S.LikeCount>
          </S.LikeButton>
        </S.FlexRowJustiBet>
        <S.Introduce>{recipeItem.content}</S.Introduce>
      </S.WrapContent>
    </S.Container>
  )
}

export default RecipeItem
