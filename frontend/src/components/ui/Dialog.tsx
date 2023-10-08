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
`
const DialogBlock = styled.div`
  width: 32rem;
  padding: 1.5rem;
  background: ${palette.white};
  border-radius: 0.2rem;

  h3 {
    margin: 0;
    font-size: 1.5rem;
  }

  p {
    font-size: 1.125rem;
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
  if (!visible) return null
  return (
    <DarkBackground>
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
