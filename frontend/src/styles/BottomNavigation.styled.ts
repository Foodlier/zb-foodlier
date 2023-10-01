import styled from 'styled-components'
import { breakpoints, palette } from '../constants/Styles'

export const Container = styled.div`
  position: fixed;
  display: flex;
  bottom: 0;
  width: 100vw;
  height: 10rem;
  padding: 2rem 0;
  border-radius: 2rem 2rem 0 0;
  background-color: white;
  box-shadow: 0px 4px 15px ${palette.shadow};
  z-index: 9999;

  ${breakpoints.large} {
    display: none;
  }
`

export const WrapIcon = styled.button`
  display: flex;
  flex: 1;
  flex-direction: column;
  align-items: center;
  color: ${palette.textPrimary};
`

export const Text = styled.span`
  font-size: 1.3rem;
  margin-top: 0.5rem;
`
