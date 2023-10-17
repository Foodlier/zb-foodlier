import { palette } from '../../constants/Styles'
import useIcon from '../../hooks/useIcon'
import * as S from '../../styles/recipe/EmptyRecipeItem.styled'

interface RecipeItemProps {
  onClick: (e: React.MouseEvent<HTMLButtonElement>) => void
}

function EmptyRecipeItem({ onClick }: RecipeItemProps) {
  const { IcAddLight } = useIcon()
  return (
    <S.Container onClick={onClick}>
      <S.Image>
        <IcAddLight size={5} color={palette.textPrimary} />
        <S.Title>견적서를 선택해주세요</S.Title>
      </S.Image>
    </S.Container>
  )
}

export default EmptyRecipeItem
