import React, { useState } from 'react'
import * as R from '../../styles/recipe/RecipeItem.styled'
import { palette } from '../../constants/Styles'
import useIcon from '../../hooks/useIcon'

// interface RecipeItemProps {
//   recipeId: string | number
//   title: string
//   nickname : string
//   introduce: string
//   imagePath: string
//   likeCount: number
//   isLike: boolean
// }

const RecipeItem = () => {
  const { IcFavorite, IcFavoriteFill } = useIcon()

  const dummyData = {
    id: 1,
    title: '마크정식',
    nickname: '닉네임입니다',
    introduce:
      '마크정식 어쩌구 저쩌구마크정식 어쩌구 저쩌구마크정식 어쩌구 저쩌구마크정식 어쩌구 저쩌구마크정식 어쩌구 저쩌구',
    imagePath: '',
    likeCount: 150,
    isLike: true,
  }

  const [isLike, setIsLike] = useState(dummyData.isLike)
  const [likeCount, setLikeCount] = useState(dummyData.likeCount)

  const onClickLikeButton = () => {
    setIsLike(!isLike)
    setLikeCount(isLike ? likeCount - 1 : likeCount + 1)
  }

  return (
    <R.Container>
      <R.Image />
      <R.WrapContent>
        <R.FlexRowJustiBet>
          <R.FlexRow>
            <R.Title>{dummyData.title}</R.Title>
            <R.Nickname>{dummyData.nickname}</R.Nickname>
          </R.FlexRow>
          <R.LikeButton onClick={onClickLikeButton}>
            {isLike ? (
              <IcFavoriteFill size={2} color="#EA5455" />
            ) : (
              <IcFavorite size={2} color={palette.textPrimary} />
            )}
            <R.LikeCount>{likeCount}</R.LikeCount>
          </R.LikeButton>
        </R.FlexRowJustiBet>
        <R.Introduce>{dummyData.introduce}</R.Introduce>
      </R.WrapContent>
    </R.Container>
  )
}

export default RecipeItem
