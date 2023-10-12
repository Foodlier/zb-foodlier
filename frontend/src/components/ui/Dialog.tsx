import React from 'react'
import styled from 'styled-components'
import { palette } from '../../constants/Styles'
import CommonButton from './Button'

interface DialogProps {
  title: string
  children: React.ReactNode
  confirmText: string
  cancelText: string
  visible: boolean
  onConfirm: () => void
  onCancel: () => void
}

const DarkBackground = styled.div`
  position: fixed;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: ${palette.dim};
  z-index: 11;
`
const DialogBlock = styled.div`
  width: 52rem;
  height: 32rem;
  padding: 1.5rem;
  background: ${palette.white};
  border-radius: 0.2rem;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  h3 {
    margin: 0;
    font-size: 1.8rem;
  }

  p {
    margin: 3rem 0 2rem;
    font-size: 1.425rem;
  }
`

const ButtonGroup = styled.div`
  margin-top: 3rem;
  display: flex;
  justify-content: flex-end;
`

const Dialog: React.FC<DialogProps> = ({
  title,
  children,
  confirmText,
  cancelText,
  onConfirm,
  onCancel,
  visible,
}) => {
  const handleBackgroundClick = (e: React.MouseEvent<HTMLDivElement>) => {
    if (e.target === e.currentTarget) {
      onCancel()
    }
  }
  if (!visible) return null
  return (
    <DarkBackground onClick={handleBackgroundClick}>
      <DialogBlock>
        <h3>{title}</h3>
        <p>{children}</p>
        <ButtonGroup>
          <CommonButton color="divider" onClick={onCancel}>
            {cancelText}
          </CommonButton>
          <CommonButton color="main" onClick={onConfirm}>
            {confirmText}
          </CommonButton>
        </ButtonGroup>
      </DialogBlock>
    </DarkBackground>
  )
}

export default Dialog
