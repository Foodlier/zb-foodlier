import styled from 'styled-components'
import { palette } from '../../constants/Styles'

export const PaymentContainer = styled.div`
  position: relative;
`

export const PaymentWidget = styled.div``

export const ChargeButton = styled.button`
  position: absolute;
  left: 50%;
  transform: translate(-50%);
  bottom: -20px;
  width: 120px;
  padding: 6px;
  border-radius: 5px;
  background-color: #3182f6;
  color: ${palette.white};
`
