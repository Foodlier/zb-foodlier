import styled from 'styled-components'
import { breakpoints, palette, typography } from '../../../constants/Styles'

export const WrapButton = styled.div`
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding: 1rem;
`

export const Button = styled.button`
  color: ${palette.textSecondary};
  border-radius: 1rem;
  padding: 1rem;
  margin-left: 1rem;
  font-size: ${typography.mobile.content};

  ${breakpoints.large} {
    font-size: ${typography.web.content};
  }
`
