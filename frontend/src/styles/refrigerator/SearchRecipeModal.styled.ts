import styled from 'styled-components'
import { breakpoints, palette, zindex } from '../../constants/Styles'

export const ModalBackdrop = styled.div`
  z-index: ${zindex.modal};
  position: fixed;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: rgba(0, 0, 0, 0.4);
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
`
export const Container = styled.div`
  width: 100vw;
  max-width: 90%;
  height: 80vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background-color: ${palette.white};
  border-radius: 1rem;
  padding: 3rem 0;

  ${breakpoints.large} {
    max-width: 90%;
    padding: 5rem 0;
  }
`

export const WrapList = styled.div`
  display: flex;
  flex-wrap: wrap;
  overflow-y: scroll;
  padding: 0 3rem;
`
