import * as S from '../../styles/slider/SliderCard.styled'
import '../../reset.css'
import useIcon from '../../hooks/useIcon'
import { palette } from '../../constants/Styles'
import { RecipeListItem } from '../../constants/Interfaces'

function SliderCard({ item }: { item: RecipeListItem }) {
  const { IcFavorite, IcFavoriteFill } = useIcon()

  return (
    <S.BoxContainer>
      <S.Image src={item.mainImageUrl} alt={item.title} />
      <S.TextWrap>
        <S.FlexWrap>
          <S.Title>{item.title}</S.Title>
          <S.Like>
            {item.isHeart ? (
              <IcFavoriteFill size={2.5} color={palette.main} />
            ) : (
              <IcFavorite size={2.5} color={palette.textPrimary} />
            )}
            {item.heartCount}
          </S.Like>
        </S.FlexWrap>
        <S.Content>{item.content}</S.Content>
      </S.TextWrap>
    </S.BoxContainer>
  )
}

export default SliderCard
