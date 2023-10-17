import * as S from '../../styles/ui/ModalWithoutButton.styled'

interface ModalWithoutButtonProps {
  content: string
  setIsModalFalse: () => void
}
// Button이 존재하지 않는 Modal
// modal 밖 영역 클릭 시 modal close
const ModalWithoutButton = ({
  content,
  setIsModalFalse,
}: ModalWithoutButtonProps) => {
  return (
    <S.ModalBackdrop onClick={() => setIsModalFalse()}>
      <S.Container onClick={e => e.stopPropagation()}>
        <S.Content>{content}</S.Content>
      </S.Container>
    </S.ModalBackdrop>
  )
}

export default ModalWithoutButton
