import styled from 'styled-components'
import { palette } from '../../constants/Styles'

export const Container = styled.div`
  width: 80rem;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-bottom: 10rem;
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
  width: 100%;
  padding: 0 20%;
  margin: 5rem 0;
  /* margin: 0 auto; */
`

export const Label = styled.label`
  width: 20%;
`

export const Input = styled.input<{ $readOnly?: boolean }>`
  border: 1px solid ${palette.divider};
  padding: 1rem;
  border-radius: 1rem;
  width: 75%;
  color: ${props =>
    props.$readOnly ? palette.textDisablePlace : palette.textPrimary};
  background-color: ${props =>
    props.$readOnly ? palette.divider : palette.white};
`

export const CheckNickNameButton = styled.button<{ $isActive: boolean }>`
  background-color: ${props =>
    props.$isActive ? palette.main : palette.divider};
  color: ${palette.white};
  border-radius: 1rem;
  margin-left: 1rem;
  padding: 1rem;
  transition: all 0.3s ease-in-out;
`

export const Button = styled.button`
  background-color: ${palette.main};
  color: ${palette.white};
  border-radius: 1rem;
  margin-left: 1rem;
  padding: 1rem;
`

export const FlexWrap = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 2rem;
  /* align-items: center; */
`

export const Flex = styled.div`
  display: flex;
  width: 80%;
`

export const Wrap = styled.div`
  position: relative;
  display: flex;
  flex-direction: column;
`

export const EditButton = styled.button<{ $isActive: boolean }>`
  align-self: center;
  width: 100%;
  border-radius: 1rem;
  padding: 1rem;
  margin-top: 5rem;
  background-color: ${props =>
    props.$isActive ? palette.main : palette.divider};
  color: ${palette.white};
  transition: all 0.3s ease-in-out;
`

export const ErrorMessage = styled.span`
  position: absolute;
  color: ${palette.main};
  margin-top: 1rem;
  top: 100%;
`
