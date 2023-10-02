import React, { useState } from 'react'
import * as S from '../../styles/recipe/RecipeItem.styled'
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
    <S.Container>
      <S.Image />
      <S.WrapContent>
        <S.FlexRowJustiBet>
          <S.FlexRow>
            <S.Title>{dummyData.title}</S.Title>
            <S.Nickname>{dummyData.nickname}</S.Nickname>
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
        <S.Introduce>{dummyData.introduce}</S.Introduce>
      </S.WrapContent>
    </S.Container>
  )
}

export default RecipeItem
