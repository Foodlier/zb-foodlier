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
  max-width: 70%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background-color: ${palette.white};
  border-radius: 1rem;
  padding: 3rem 0;

  ${breakpoints.large} {
    max-width: 50%;
    padding: 5rem 0;
  }
`

export const Content = styled.span`
  font-size: 1.8rem;
`

export const SubContent = styled.span`
  width: 75%;
  font-size: 1.4rem;
  margin-top: 1rem;
  text-align: center;
  color: ${palette.textSecondary};
`

export const WrapButton = styled.div`
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 3rem;
`

export const Button = styled.button<{ $isActive?: boolean }>`
  width: 25%;
  background-color: ${props =>
    props.$isActive ? palette.main : palette.white};
  color: ${props => (props.$isActive ? palette.white : palette.main)};
  border: 1px solid ${props => (props.$isActive ? palette.white : palette.main)};
  margin: 0 1rem;
  padding: 1rem 0;
  border-radius: 1rem;
`
