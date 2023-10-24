import styled from 'styled-components'
import { palette, typography, weight } from '../../constants/Styles'

export const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 5rem 0;
`

export const Title = styled.span`
  font-size: ${typography.web.mainTitle};
  font-weight: ${weight.mainTitle};
  margin-bottom: 5rem;
`

export const Label = styled.label`
  width: 30%;
`

export const Input = styled.input`
  border: 1px solid ${palette.divider};
  padding: 1rem;
  width: 70%;
  border-radius: 1rem;
`

export const FlexWrap = styled.div`
  width: 100%;
  display: flex;
  align-items: flex-start;
  padding: 0 20%;
  margin-bottom: 2rem;
`

export const Button = styled.button<{ $isActive: boolean }>`
  align-self: center;
  display: block;
  width: 50%;
  padding: 1rem;
  border-radius: 1rem;
  margin-top: 10rem;
  background-color: ${props =>
    props.$isActive ? palette.main : palette.textDisablePlace};
  color: ${palette.white};
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
