import styled from 'styled-components'
import {
  breakpoints,
  palette,
  typography,
  weight,
} from '../../constants/Styles'

export const Container = styled.div`
  width: 100%;
  margin: 5rem auto 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 0 10%;
  margin-bottom: 10rem;

  ${breakpoints.large} {
    width: 70%;
  }
`

export const Title = styled.span`
  font-size: ${typography.web.mainTitle};
  font-weight: ${weight.mainTitle};
  margin-bottom: 5rem;
`

export const Label = styled.label`
  font-weight: ${weight.subTitle};
  width: 18rem;
  max-width: 100%;
  font-size: ${typography.mobile.content};
  margin-bottom: 1rem;

  ${breakpoints.large} {
    font-size: ${typography.web.content};
  }
`

export const Input = styled.input`
  border: 1px solid ${palette.divider};
  padding: 1rem;
  width: 100%;
  border-radius: 0.5rem;
`

export const FlexWrap = styled.div`
  width: 100%;
  display: flex;
  margin-bottom: 2rem;
  flex-direction: column;

  ${breakpoints.large} {
    flex-direction: row;
  }
`

export const Button = styled.button<{ $isActive: boolean }>`
  align-self: center;
  display: block;
  width: 40%;
  padding: 1rem;
  border-radius: 0.5rem;
  margin-top: 5rem;
  background-color: ${props =>
    props.$isActive ? palette.main : palette.textDisablePlace};
  color: ${palette.white};
  font-weight: ${weight.subTitle};

  ${breakpoints.large} {
    width: 30%;
  }
`

export const ErrorMessage = styled.span`
  color: ${palette.main};
  font-size: ${typography.web.desc};
`

export const Wrap = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
`
