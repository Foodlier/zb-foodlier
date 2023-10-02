import React from 'react'
import * as S from '../../../styles/recipe/detail/DetailProcedure.styled'

interface ProcedureStep {
  cookingOrderImageUrl: string
  cookingOrder: string
}

interface DetailProcedureProps {
  detail: ProcedureStep[]
}

function DetailProcedure({ detail }: DetailProcedureProps) {
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
