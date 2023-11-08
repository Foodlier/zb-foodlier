import React from 'react'
import * as S from '../../styles/refrigerator/RejectReviewModal.styled'

interface ChargeModalProps {
  setIsModal: React.Dispatch<React.SetStateAction<boolean>>
}

const RejectReviewModal: React.FC<ChargeModalProps> = ({ setIsModal }) => {
  return (
    <S.ModalBackdrop onClick={() => setIsModal(false)}>
      <S.Container onClick={e => e.stopPropagation()}>
        <S.Title>후기 내용을 작성해주세요.</S.Title>
      </S.Container>
    </S.ModalBackdrop>
  )
}

export default RejectReviewModal
