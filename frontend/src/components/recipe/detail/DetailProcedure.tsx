import { RecipeDetailDtoList } from '../../../constants/Interfaces'
import * as S from '../../../styles/recipe/detail/DetailProcedure.styled'

interface DetailProcedureProps {
  detail: RecipeDetailDtoList[] | undefined
}

const DetailProcedure = ({ detail }: DetailProcedureProps) => {
  if (!detail) return null

  return (
    <S.ProcedureContainer>
      <S.MainTit>조리 순서</S.MainTit>

      <S.ProcedureWrap>
        {detail.map((step, index) => (
          <S.ProcedureBox key={step.cookingOrder}>
            <S.stepTxt>
              {index + 1}. {step.cookingOrder}
            </S.stepTxt>
            <S.ImgWrap>
              <S.ProcedureImg
                src={step.cookingOrderImageUrl}
                alt={`조리 순서 이미지 ${index + 1}`}
              />
            </S.ImgWrap>
          </S.ProcedureBox>
        ))}
      </S.ProcedureWrap>
    </S.ProcedureContainer>
  )
}

export default DetailProcedure
