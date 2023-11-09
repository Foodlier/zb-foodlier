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

export const HistoryWrap = styled.div`
  display: flex;
  padding: 1rem;
  justify-content: space-between;
  align-items: center;
  border: 1px solid ${palette.divider};
  border-radius: 0.5rem;
  box-shadow: 0px 2px 1px 1px rgba(89, 97, 104, 0.1);
`

export const HistoryLeftWrap = styled.div``

export const HistoryRightWrap = styled.div``

export const HistoryPartWrap = styled.div`
  display: flex;
  align-items: center;
  gap: 1rem;
`

export const HistoryPartTitle = styled.span`
  display: inline-block;
  min-width: 86px;
  font-weight: ${weight.subTitle};
`

export const HistoryPartDes = styled.span``

export const ReviewButton = styled.button`
  background-color: ${palette.main};
  color: ${palette.white};
  padding: 0.5rem 1.2rem;
  border-radius: 0.5rem;
  white-space: nowrap;
  ${breakpoints.large} {
    margin-right: 1rem;
  }
`

export const ReviewedButton = styled.button`
  background-color: ${palette.divider};
  color: ${palette.white};
  padding: 0.5rem 1.2rem;
  border-radius: 0.5rem;
  white-space: nowrap;
  cursor: initial;
  ${breakpoints.large} {
    margin-right: 1rem;
  }
`

export const NoHistoryCard = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 140px;
  border-radius: 10px;
  color: ${palette.divider};
  box-shadow: 0px 2px 6px 2px rgba(89, 97, 104, 0.1);
`
