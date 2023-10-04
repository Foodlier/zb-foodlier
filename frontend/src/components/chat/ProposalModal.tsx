import * as S from '../../styles/chat/ProposalModal.styled'

interface ProposalModalProps {
  setIsProposalModalOpen: React.Dispatch<React.SetStateAction<boolean>>
}

const ProposalModal = ({ setIsProposalModalOpen }: ProposalModalProps) => {
  return (
    <S.ModalBackdrop onClick={() => setIsProposalModalOpen(false)}>
      <S.Container onClick={e => e.stopPropagation()}>
        <S.Title>제안 금액을 입력해주세요.</S.Title>
        <S.WrapInput>
          <S.Input />원
        </S.WrapInput>
        <S.Button>제안하기</S.Button>
      </S.Container>
    </S.ModalBackdrop>
  )
}

export default ProposalModal
