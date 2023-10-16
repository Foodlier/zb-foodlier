/* eslint-disable import/prefer-default-export */
import styled from 'styled-components'
import { palette } from '../../constants/Styles'

export const EmptyView = styled.div`
  display: flex;
  flex: 1;
  align-items: center;
  justify-content: center;
  color: ${palette.textDisablePlace};
`
