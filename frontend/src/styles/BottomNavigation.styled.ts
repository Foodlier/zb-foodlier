import styled from 'styled-components'
import { breakpoints, palette, typography, zindex } from '../constants/Styles'

export const Container = styled.div`
  position: fixed;
  display: flex;
  bottom: 0;
  width: 100vw;
  height: 10rem;
  padding: 2rem 0;
  border-radius: 2rem 2rem 0 0;
  background-color: white;
  box-shadow: 0 -0.5rem 1rem rgba(0, 0, 0, 0.1);
  z-index: ${zindex.base + 1};

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
  font-size: ${typography.mobile.content};
  margin-top: 0.5rem;
`
