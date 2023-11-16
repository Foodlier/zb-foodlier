import styled from 'styled-components'
import {
  breakpoints,
  palette,
  typography,
  weight,
} from '../../constants/Styles'

export const Container = styled.div`
  display: flex;
  flex-direction: column;
  gap: 20px;
  padding: 0 2rem;
`

export const Title = styled.span`
  font-size: ${typography.mobile.mainTitle};
  font-weight: ${weight.mainTitle};
  color: ${palette.textPrimary};
  margin: 2rem 0 5rem;
  ${breakpoints.large} {
    font-size: ${typography.web.mainTitle};
  }
`

export const SubTitle = styled.p`
  font-size: ${typography.mobile.subTitle};
  font-weight: ${weight.subTitle};
`

export const ReviewTextArea = styled.textarea`
  border: 1px solid black;
  border-radius: 10px;
  padding: 1rem;
  box-sizing: border-box;
  &:focus {
    outline: none;
  }
`

export const ReviewWriteStar = styled.div`
  display: inline-flex;
  cursor: pointer;
  width: 200px;
  ${breakpoints.large} {
    width: 280px;
  }
`

export const ReviewStar = styled.span`
  width: 100%;
`

export const ButtonList = styled.div`
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-top: 50px;
`

export const Button = styled.button<{ $reject: boolean }>`
  background-color: ${props =>
    props.$reject ? palette.divider : palette.main};
  color: ${props => props.$reject === false && palette.white};
  padding: 0.5rem 2rem;
  border-radius: 5px;
`
