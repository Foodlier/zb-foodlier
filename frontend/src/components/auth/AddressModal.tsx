import styled from 'styled-components'
import { palette } from '../../constants/Styles'

// 스타일드 컴포넌트로 모달 스타일 정의
const ModalWrapper = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5); /* 어두운 배경 색상 */
  display: flex;
  justify-content: center;
  align-items: center;
`

const ModalContent = styled.div`
  width: 50rem;
  max-height: 50vh;
  overflow-y: scroll;
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.2);
`

const ModalTitle = styled.h2`
  font-size: 24px;
  margin-bottom: 10px;
`

const ModalBody = styled.div`
  font-size: 16px;
  margin-bottom: 20px;
`

const CloseButton = styled.button`
  background-color: ${palette.main};
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 4px;
  cursor: pointer;
`

interface ModalProps {
  isOpen: boolean
  onClose: () => void
  title: string
  content: React.ReactNode
}

const Modal: React.FC<ModalProps> = ({ isOpen, onClose, title, content }) => {
  return isOpen ? (
    <ModalWrapper>
      <ModalContent>
        <ModalTitle>{title}</ModalTitle>
        <ModalBody>{content}</ModalBody>
        <CloseButton onClick={onClose}>닫기</CloseButton>
      </ModalContent>
    </ModalWrapper>
  ) : null
}

export default Modal
