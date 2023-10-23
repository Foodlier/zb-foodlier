import styled from 'styled-components'
import { palette } from '../../constants/Styles'

export const SuccessContainer = styled.div`
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
`
export const SuccessDiv = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
`

export const SuccessMsg = styled.h2``

export const SuccessImg = styled.img``

export const SuccessPrice = styled.p``

export const ButtonList = styled.div`
  display: flex;
  flex-direction: column;
  gap: 4px;
`

export const CancelButton = styled.button`
  padding: 6px 16px;
  border-radius: 5px;
  background-color: ${palette.divider};
`

export const GoBackButton = styled.button`
  padding: 6px 16px;
  border-radius: 5px;
  background-color: #3182f6;
  color: ${palette.white};
`
