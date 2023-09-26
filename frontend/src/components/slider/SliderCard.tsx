import React, { useState } from 'react'
import * as S from '../../styles/slider/SliderCard.styled'
import '../../reset.css'
import useIcon from '../../hooks/useIcon'
import { palette } from '../../constants/Styles'

interface SliderCardProps {
  title: string
  content: string
  like: boolean
  image: string
}

const SliderCard: React.FC<SliderCardProps> = ({
  title,
  content,
  like,
  image,
}) => {
  const { IcFavorite, IcFavoriteFill } = useIcon()
  const [isLike, setIsLike] = useState(like)
  const [likeCount, setLikeCount] = useState(like ? 1 : 0)

  const onClickLikeButton = () => {
    setIsLike(!isLike)
    setLikeCount(isLike ? likeCount - 1 : likeCount + 1)
  }

  return (
    <S.BoxContainer>
      <S.ImgWrap>
        <img src={image} alt={title} />
      </S.ImgWrap>
      <S.TextWrap>
        <div>
          <S.Title>{title}</S.Title>
          <S.LikeButton onClick={onClickLikeButton}>
            {isLike ? (
              <IcFavoriteFill size={2.5} color="#EA5455" />
            ) : (
              <IcFavorite size={2.5} color={palette.textPrimary} />
            )}
            {likeCount}
          </S.LikeButton>
        </div>
        <S.Content>{content}</S.Content>
      </S.TextWrap>
    </S.BoxContainer>
  )
}

export default SliderCard
