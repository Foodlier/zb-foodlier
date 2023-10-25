import styled from 'styled-components'
import {
  breakpoints,
  palette,
  typography,
  weight,
} from '../../constants/Styles'

export const Container = styled.div`
  width: 100%;
  padding: 0 10%;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-bottom: 10rem;

  ${breakpoints.large} {
    width: 70%;
  }
`

export const WrapImage = styled.div`
  cursor: pointer;
`

export const FileLabel = styled.label`
  display: flex;
  align-items: center;
  justify-content: center;
  width: 20rem;
  height: 20rem;
  border: 1px solid ${palette.divider};
  border-radius: 1rem;
  margin-top: 5rem;
`

export const File = styled.input`
  display: none;
`

export const Image = styled.img<{ $size: number }>`
  width: ${props => props.$size}rem;
  height: ${props => props.$size}rem;
  border-radius: 1rem;
  cursor: pointer;
`

export const WrapForm = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
  padding: 0 2%;
  margin: 5rem 0;

  ${breakpoints.large} {
    font-size: ${typography.web.desc};
    padding: 1rem;
  }
`

export const Label = styled.label`
  margin-right: 1.5rem;
  font-weight: ${weight.subTitle};
  width: auto;
  margin-bottom: 1rem;
`

export const Input = styled.input<{ $readOnly?: boolean }>`
  border: 1px solid ${palette.divider};
  padding: 1rem;
  height: 4.7rem;
  border-radius: 0.5rem;
  width: 100%;
  color: ${props =>
    props.$readOnly ? palette.textDisablePlace : palette.textPrimary};
  background-color: ${props =>
    props.$readOnly ? palette.divider : palette.white};
`

export const CheckNickNameButton = styled.button<{ $isActive: boolean }>`
  background-color: ${props =>
    props.$isActive ? palette.main : palette.divider};
  color: ${palette.white};
  border-radius: 0.5rem;
  height: 4.7rem;
  margin-left: 1.5rem;
  padding: 1rem 0.5rem;
  font-size: ${typography.mobile.desc};
  font-weight: ${weight.subTitle};
  transition: all 0.3s ease-in-out;
  width: 12rem;
  ${breakpoints.large} {
    font-size: ${typography.web.desc};
    padding: 1rem;
  }
`

export const Button = styled.button`
  background-color: ${palette.main};
  color: ${palette.white};
  border-radius: 0.5rem;
  height: 4.7rem;
  margin-left: 1.5rem;
  width: 12rem;
  padding: 1rem 0.5rem;
  font-size: ${typography.mobile.desc};
  font-weight: ${weight.subTitle};

  ${breakpoints.large} {
    font-size: ${typography.web.desc};
    padding: 1rem;
  }
`

export const FlexWrap = styled.div`
  display: flex;
  width: 100%;
  flex-direction: column;
  justify-content: center;
  align-items: flex-start;
  margin-bottom: 2.5rem;
`

export const Flex = styled.div`
  display: flex;
  width: 100%;
  justify-content: center;
`

export const Wrap = styled.div`
  position: relative;
  display: flex;
  flex-direction: column;
`

export const EditButton = styled.button<{ $isActive: boolean }>`
  align-self: center;
  width: 40%;
  border-radius: 0.5rem;
  padding: 1rem;
  margin-top: 5rem;
  font-weight: ${weight.subTitle};
  background-color: ${props =>
    props.$isActive ? palette.main : palette.divider};
  color: ${palette.white};
  transition: all 0.3s ease-in-out;

  ${breakpoints.large} {
    width: 30%;
  }
`

export const ErrorMessage = styled.span`
  position: absolute;
  color: ${palette.main};
  margin-top: 1rem;
  top: 100%;
`
