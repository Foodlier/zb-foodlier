import styled from 'styled-components'
import {
  breakpoints,
  palette,
  typography,
  zindex,
} from '../../constants/Styles'

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

export const Title = styled.span`
  font-size: 1.6rem;
  font-weight: 600;
`

export const WrapInput = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 2rem 0;
  font-size: ${typography.mobile.desc};

  ${breakpoints.large} {
    font-size: ${typography.web.desc};
  }
`

export const Input = styled.input`
  width: 60%;
  border: 1px solid black;
  border-radius: 1rem;
  margin-right: 0.5rem;
  padding: 1rem;
  font-size: ${typography.mobile.desc};

  ${breakpoints.large} {
    font-size: ${typography.web.desc};
    width: 100%;
  }
`

export const Alert = styled.p`
  color: red;
  margin-bottom: 1rem;
`

export const Button = styled.button`
  background-color: ${palette.main};
  color: ${palette.white};
  padding: 1rem 2rem;
  border-radius: 0.5rem;
  font-size: ${typography.mobile.desc};

  ${breakpoints.large} {
    font-size: ${typography.web.desc};
  }
`
