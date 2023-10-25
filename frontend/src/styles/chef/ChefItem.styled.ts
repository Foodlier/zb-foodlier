import styled from 'styled-components'
import { breakpoints, typography } from '../../constants/Styles'

export const Container = styled.section`
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-right: 0.5rem;

  cursor: pointer;

  ${breakpoints.large} {
    margin-right: 3rem;
  }
`
export const Image = styled.img`
  width: 10rem;
  height: 10rem;
  max-width: 100%;
  max-height: 10rem;
  border-radius: 50%;
  padding: 15%;
`
export const Nickname = styled.p`
  margin-top: 1rem;
  font-weight: 600;
  font-size: ${typography.mobile.content};
  ${breakpoints.large} {
    font-size: ${typography.web.content};
  }
`
