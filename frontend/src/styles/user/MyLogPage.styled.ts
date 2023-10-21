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
  padding: 0 2rem;
`

export const WrapTitle = styled.div`
  display: flex;
  align-items: flex-end;
  margin: 2rem 0 5rem;
`

export const Title = styled.span`
  font-size: ${typography.mobile.mainTitle};
  font-weight: ${weight.mainTitle};
  color: ${palette.textPrimary};

  ${breakpoints.large} {
    font-size: ${typography.web.mainTitle};
  }
`

export const SubTitle = styled.span`
  margin-left: 1rem;
  font-size: ${typography.mobile.subTitle};
  font-weight: ${weight.subTitle};
  color: ${palette.textSecondary};
  ${breakpoints.large} {
    font-size: ${typography.web.subTitle};
  }
`

export const WrapList = styled.div`
  display: flex;
  flex-wrap: wrap;
`
