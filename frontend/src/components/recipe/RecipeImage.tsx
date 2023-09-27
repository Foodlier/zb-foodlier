import React from 'react'
import * as S from '../../styles/recipe/RecipeImage.styled'
import useIcon from '../../hooks/useIcon'

// size : 해당 component width, height (1:1)
// isText : '대표 이미지를 등록해주세요' 텍스트 노출 여부
const RecipeImage = ({ size, isText }: { size: number; isText: boolean }) => {
  const { IcAddRoundDuotone } = useIcon()
  return (
    <S.ImageButton $size={size}>
      <IcAddRoundDuotone size={4} />
      {isText && <S.SubText>대표 이미지를 등록해주세요.</S.SubText>}
    </S.ImageButton>
  )
}

export default RecipeImage
