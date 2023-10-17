/* eslint-disable import/no-extraneous-dependencies */
import DatePicker from 'react-datepicker'
import styled from 'styled-components'
import { breakpoints, palette } from '../../constants/Styles'

export const RequestContainer = styled.div`
  display: flex;
  flex-direction: column;
  padding: 2rem 0;
  margin: 0 auto;
`

export const RequestForm = styled.form`
  width: 100%;
  padding: 0 2%;
`

export const RequestFormList = styled.ul`
  display: flex;
  flex-direction: column;
  /* gap: 30px; */
  padding-left: 0;
`

export const ElementTitle = styled.span`
  font-size: 1.8rem;
  color: ${palette.textPrimary};
`

export const ElementInput = styled.input`
  width: 100%;
  border-radius: 1rem;
  border: 1px solid ${palette.divider};
  padding: 0 2rem;

  &::placeholder {
    font-size: 1.6rem;
  }
  &:focus {
    outline: none;
  }
  ${breakpoints.large} {
    padding: 1rem;
    font-size: 1.6rem;
  }
`

export const ElementSourceInput = styled.input`
  width: 100%;
  border-radius: 10px;
  border: 1px solid ${palette.divider};
  font-size: 2rem;
  margin-right: 0px;
  box-sizing: border-box;
  &:focus {
    outline: none;
  }
  ${breakpoints.large} {
  }
`

export const LikeInputDiv = styled.div`
  display: flex;
  width: 100%;
  border-radius: 10px;
  border: 1px solid ${palette.divider};
  box-sizing: border-box;
  padding: 0 20px;
  font-size: 20px;
  gap: 10px;
  align-items: center;
  ${breakpoints.large} {
  }
`
export const InvisibleInput = styled.input`
  width: 100%;
  border-radius: 1rem;
  padding: 0 2rem;
  &:focus {
    outline: none;
  }
  ${breakpoints.large} {
    padding: 1rem;
    font-size: 1.6rem;
  }
`

export const ElementSpan = styled.span`
  font-size: 2rem;
`

export const PlusButton = styled.button`
  border: 1px solid ${palette.divider};
  border-radius: 10px;
  font-size: 24px;
  ${breakpoints.large} {
    padding: 1rem;
  }
`

export const ButtonList = styled.div`
  display: flex;
  justify-content: center;
  gap: 2rem;
  width: auto;
  margin: 0 auto;
  /* margin: 50px 0; */
  ${breakpoints.large} {
    /* margin-top: 300px; */
    margin: 3rem 0;
  }
`

export const WritingButton = styled.button`
  width: 146px;
  height: 44px;
  line-height: 44px;
  text-align: center;
  background-color: ${palette.main};
  color: ${palette.white};
  border-radius: 5px;
  margin-bottom: 95px;
  ${breakpoints.large} {
    margin-bottom: 0px;
  }
`

export const WrapForm = styled.div`
  width: 80%;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin: 0 auto 1rem;
`

export const Title = styled.span`
  font-size: 1.4rem;
  font-weight: 600;
  margin-bottom: 1rem;
`
export const Input = styled.input<{
  $width?: number
  $marginRi?: number
  $marginLf?: number
  $marginBt?: number
}>`
  width: ${props => props.$width || 100}%;
  border: 1px solid ${palette.divider};
  border-radius: 0.6rem;
  padding: 0.5rem 1rem;
  font-size: 1.2rem;
  color: ${palette.textPrimary};
  margin-right: ${props => props.$marginRi || 0}rem;
  margin-left: ${props => props.$marginLf || 0}rem;
  margin-bottom: ${props => props.$marginBt || 0}rem;

  &::placeholder {
    font-size: 1.2rem;
    color: ${palette.textDisablePlace};
  }
`

export const AddButton = styled.button<{ $width: number }>`
  width: 15%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.2rem;
  padding: 0.4rem 1.2rem;
  border: 1px solid ${palette.divider};
  border-radius: 0.8rem;
  background-color: #f3f3f3;
  color: ${palette.textSecondary};
  /* margin-top: 1rem; */
`

export const SearchButton = styled.button`
  width: 15%;
  color: ${palette.main};
  border: 1px solid ${palette.main};
  border-radius: 0.6rem;
  padding: 0.5rem 1rem;
  margin-left: 1rem;

  ${breakpoints.large} {
    font-size: 1.2rem;
  }
`

export const FlexWrap = styled.div`
  width: 100%;
  display: flex;
  align-items: center;
`

export const StyledDatePicker = styled(DatePicker)`
  border: 1px solid ${palette.divider};
  border-radius: 0.6rem;
  padding: 0.5rem 1rem;
  font-size: 1.2rem;
  color: ${palette.textSecondary};
`

export const ErrorMessage = styled.span`
  margin-top: 1rem;
  color: ${palette.main};
  font-size: 1.2rem;
  font-weight: 600;
`
