import styled from 'styled-components'
import { breakpoints, palette, typography } from '../../constants/Styles'

export const Container = styled.button`
  border: 1px solid ${palette.divider};
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 2rem;
  border-radius: 0.5rem;
  box-shadow: 1px 1px 4px ${palette.shadow};
  margin-bottom: 1rem;
`

export const Text = styled.span`
  font-size: ${typography.mobile.content};
  color: ${palette.textPrimary};

  ${breakpoints.large} {
    font-size: ${typography.web.content};
  }
`
