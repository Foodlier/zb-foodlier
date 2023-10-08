import React from 'react'
import useIcon from '../hooks/useIcon'
import * as S from '../styles/StarRating.styled'

interface StarRatingProps {
  rating: number
  size: number
}

const StarRating: React.FC<StarRatingProps> = ({ rating, size }) => {
  const { IcStar, IcEmptyStar } = useIcon()
  const yelloStarCount = rating
  const emptyStarCount = 5 - rating
  return (
    <S.StarContainer>
      {Array.from({ length: yelloStarCount }, (_, index) => (
        <IcStar key={index} size={size} />
      ))}
      {Array.from({ length: emptyStarCount }, (_, index) => (
        <IcEmptyStar key={index} size={size} />
      ))}
    </S.StarContainer>
  )
}

export default StarRating
