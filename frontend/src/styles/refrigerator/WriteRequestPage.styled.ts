/* eslint-disable import/no-extraneous-dependencies */
import DatePicker from 'react-datepicker'
import styled from 'styled-components'
import {
  breakpoints,
  palette,
  typography,
  weight,
} from '../../constants/Styles'

export const RequestContainer = styled.div`
  display: flex;
  flex-direction: column;
  padding: 3rem 3% 0;
  margin: 0 auto;

  ${breakpoints.large} {
    font-size: ${typography.web.content};
    padding: 5rem 0 0;
  }
`

export const RequestForm = styled.form`
  width: 100%;
  padding: 0 5%;
`

export const RequestFormList = styled.ul`
  display: flex;
  flex-direction: column;
  padding-left: 0;
`

export const ElementTitle = styled.span`
  font-size: ${typography.mobile.content};
  color: ${palette.textPrimary};

  ${breakpoints.large} {
    font-size: ${typography.web.content};
  }
`

export const ElementInput = styled.input`
  width: 100%;
  border-radius: 1rem;
  border: 0.1rem solid ${palette.divider};
  padding: 0 2rem;

  &::placeholder {
    font-size: ${typography.mobile.desc};
  }
  &:focus {
    outline: none;
  }
  ${breakpoints.large} {
    padding: 1rem;
    font-size: ${typography.web.desc};
  }
`

export const ElementSourceInput = styled.input`
  width: 100%;
  border-radius: 1rem;
  border: 0.5rem solid ${palette.divider};
  font-size: ${typography.mobile.content};
  margin-right: 0;
  box-sizing: border-box;
  &:focus {
    outline: none;
  }
  ${breakpoints.large} {
    font-size: ${typography.web.content};
  }
`

export const LikeInputDiv = styled.div`
  display: flex;
  width: 100%;
  border-radius: 1rem;
  border: 0.1rem solid ${palette.divider};
  box-sizing: border-box;
  padding: 0 2rem;
  font-size: ${typography.mobile.subTitle};
  gap: 1rem;
  align-items: center;
  ${breakpoints.large} {
    font-size: ${typography.web.subTitle};
  }
`
export const InvisibleInput = styled.input`
  width: 100%;
  border-radius: 1rem;
  padding: 0 2rem;
  font-size: ${typography.mobile.content};
  &:focus {
    outline: none;
  }
  ${breakpoints.large} {
    padding: 1rem;
    font-size: ${typography.web.content};
  }
`

export const ElementSpan = styled.span`
  font-size: ${typography.mobile.subTitle};

  ${breakpoints.large} {
    font-size: ${typography.web.subTitle};
  }
`

export const PlusButton = styled.button`
  border: 0.1rem solid ${palette.divider};
  border-radius: 0.5rem;
  font-size: ${typography.mobile.mainTitle};
  ${breakpoints.large} {
    padding: 1rem;
    font-size: ${typography.web.mainTitle};
  }
`

export const ButtonList = styled.div`
  display: flex;
  justify-content: center;
  gap: 1.5rem;
  width: auto;
  margin: 0 auto;

  ${breakpoints.large} {
    margin: 3rem 0;
  }
`

export const WritingButton = styled.button`
  width: 20rem;
  height: 4.7rem;
  line-height: 1.5;
  text-align: center;
  font-weight: ${weight.mainTitle};
  background-color: ${palette.main};
  color: ${palette.white};
  border-radius: 0.5rem;
  margin-bottom: 10rem;
  margin-top: 2rem;

  ${breakpoints.large} {
    margin-bottom: 0;
    margin-top: 5rem;
    font-size: ${typography.web.desc};
  }
`

export const WrapForm = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: flex-start;
  margin: 0 auto 2.5rem;

  ${breakpoints.large} {
    flex-direction: row;
  }
`

export const Title = styled.span`
  font-size: ${typography.mobile.subTitle};
  font-weight: ${weight.subTitle};
  margin-top: 1rem;
  margin-right: 2rem;
  margin-bottom: 1rem;
  white-space: nowrap;

  ${breakpoints.large} {
    margin-bottom: 0;
  }
`

export const Content = styled.span`
  /* font-size: ${typography.mobile.subTitle};
  font-weight: ${weight.subTitle};
  margin-right: 2rem;
  white-space: nowrap; */
`

export const Input = styled.input<{
  $width?: number
  $marginRi?: number
  $marginLf?: number
  $marginBt?: number
}>`
  display: flex;
  flex-direction: column;
  width: 100%;
  border: 0.1rem solid ${palette.divider};
  border-radius: 0.5rem;
  padding: 1rem;
  color: ${palette.textPrimary};
  margin-right: ${props => props.$marginRi || 0}rem;
  margin-left: ${props => props.$marginLf || 0}rem;
  margin-bottom: ${props => props.$marginBt || 0}rem;

  &::placeholder {
    color: ${palette.textDisablePlace};
    /* font-size: ${typography.mobile.desc}; */
  }

  ${breakpoints.large} {
    font-size: ${typography.web.desc};
    width: ${props => props.$width || 100}%;
  }
`

export const AddButton = styled.button<{ $width: number }>`
  width: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: ${typography.mobile.desc};
  padding: 0.4rem 1.2rem;
  border: 0.1rem solid ${palette.divider};
  border-radius: 0.5rem;
  background-color: #f3f3f3;
  color: ${palette.textSecondary};

  ${breakpoints.large} {
    width: 100%;
    font-size: ${typography.web.desc};
  }
`

export const SearchButton = styled.button`
  width: 20rem;
  max-width: 20rem;
  color: ${palette.main};
  border: 0.1rem solid ${palette.main};
  border-radius: 0.5rem;
  padding: 1rem;
  margin-left: 1rem;
  height: 4.6rem;
  line-height: 1;
  /* font-size: ${typography.mobile.desc}; */

  ${breakpoints.large} {
    font-size: ${typography.web.desc};
    max-width: 20rem;
  }
`

export const FlexWrap = styled.div`
  width: 100%;
  display: flex;
  align-items: center;
`

export const StyledDatePicker = styled(DatePicker)`
  border: 0.1rem solid ${palette.divider};
  border-radius: 0.5rem;
  padding: 1rem;
  color: ${palette.textDisablePlace};
`

export const ErrorMessage = styled.span`
  margin-top: 1rem;
  color: ${palette.main};
  font-size: ${typography.mobile.desc};
  font-weight: ${weight.subTitle};
`
