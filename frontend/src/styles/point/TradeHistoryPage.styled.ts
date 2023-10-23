import styled from 'styled-components'
import {
  breakpoints,
  palette,
  typography,
  weight,
} from '../../constants/Styles'

export const Container = styled.div`
  display: flex;
  flex-direction: column;
  padding: 0 2rem;
`

export const Title = styled.span`
  font-size: ${typography.mobile.mainTitle};
  font-weight: ${weight.mainTitle};
  color: ${palette.textPrimary};

  ${breakpoints.large} {
    font-size: ${typography.web.mainTitle};
  }
`
