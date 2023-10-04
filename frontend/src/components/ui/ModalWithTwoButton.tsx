import * as S from '../../styles/ui/ModalWithTwoButton.styled'

interface ModalWithTwoButtonProps {
  content: string
  subContent?: string
  setIsModalFalse: () => void
  modalEvent: () => void
}

// 예, 아니요 2개의 버튼이 있는 Modal
// 아니요 및 modal 밖 영역 클릭 시 modal close
const ModalWithTwoButton = ({
  content,
  subContent,
  setIsModalFalse,
  modalEvent,
}: ModalWithTwoButtonProps) => {
  return (
    <S.ModalBackdrop onClick={() => setIsModalFalse()}>
      <S.Container onClick={e => e.stopPropagation()}>
        <S.Content>{content}</S.Content>
        {subContent && <S.SubContent>{subContent}</S.SubContent>}
        <S.WrapButton>
          <S.Button onClick={() => setIsModalFalse()}>아니요</S.Button>
          <S.Button onClick={() => modalEvent()} $isActive>
            네
          </S.Button>
        </S.WrapButton>
      </S.Container>
    </S.ModalBackdrop>
  )
}

// defaultProps 설정
ModalWithTwoButton.defaultProps = {
  subContent: '',
}

export default ModalWithTwoButton
