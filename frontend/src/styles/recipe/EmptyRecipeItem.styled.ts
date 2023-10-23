import styled from 'styled-components'
import { breakpoints, palette, typography } from '../../constants/Styles'

export const Container = styled.button`
  display: flex;
  align-items: center;
  width: 100%;

  ${breakpoints.large} {
    flex-direction: column;
    width: 30%;
    height: 50%;
  }
`

export const Title = styled.span`
  color: ${palette.textSecondary};
  font-size: ${typography.mobile.content};
  margin-top: 1rem;

  ${breakpoints.large} {
    font-size: ${typography.web.content};
  }
`

export const Image = styled.div`
  width: 30%;
  height: 10rem;
  border: 0.1rem solid ${palette.divider};
  box-shadow: 1px 1px 1px ${palette.shadow};
  margin-right: 1rem;
  border-radius: 1rem;

  ${breakpoints.large} {
    width: 100%;
    height: 25rem;
    margin: 0;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
  }
`
