import { useState } from 'react'
import * as S from '../../styles/slider/SliderCard.styled'
import '../../reset.css'
import useIcon from '../../hooks/useIcon'
import { palette } from '../../constants/Styles'
import { RecipeListItem } from '../../constants/Interfaces'
import axiosInstance from '../../utils/FetchCall'

function SliderCard({ item }: { item: RecipeListItem }) {
  const { IcFavorite, IcFavoriteFill } = useIcon()

  const [isLike, setIsLike] = useState(item.isHeart)
  const [likeCount, setLikeCount] = useState(item.heartCount)

  const postLike = async () => {
    const res = await axiosInstance.post(`/api/recipe/heart/${item.recipeId}`)
    console.log(res)
  }

  const deleteLike = async () => {
    const res = await axiosInstance.delete(`/api/recipe/heart/${item.recipeId}`)
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
    <S.BoxContainer>
      <S.Image src={item.mainImageUrl} alt={item.title} />
      <S.TextWrap>
        <S.FlexWrap>
          <S.Title>{item.title}</S.Title>
          <S.Like onClick={onClickLikeButton}>
            {isLike ? (
              <IcFavoriteFill size={2.5} color={palette.main} />
            ) : (
              <IcFavorite size={2.5} color={palette.textPrimary} />
            )}
            {likeCount}
          </S.Like>
        </S.FlexWrap>
        <S.Content>{item.content}</S.Content>
      </S.TextWrap>
    </S.BoxContainer>
  )
}

export default SliderCard
