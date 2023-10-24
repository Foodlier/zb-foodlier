import styled from 'styled-components'
import {
  breakpoints,
  palette,
  typography,
  weight,
} from '../../constants/Styles'

export const Container = styled.div`
  width: 100%;
  height: 44rem;
  background-color: teal;
`

export const Marker = styled.div`
  display: flex;
  width: 30rem;
  flex-direction: column;
  background-color: ${palette.white};
  border: 0.1rem solid ${palette.divider};
  border-radius: 1rem;
  padding: 2rem;
`

export const FlexWrap = styled.div`
  display: flex;
  align-items: center;
`

export const MarkerTitle = styled.span`
  color: ${palette.textPrimary};
  font-weight: ${weight.subTitle};
  font-size: ${typography.mobile.subTitle};
  margin-right: 0.5rem;

  ${breakpoints.large} {
    font-size: ${typography.web.subTitle};
  }
`

export const MarkerRate = styled.span`
  font-size: ${typography.mobile.desc};
  color: ${palette.textSecondary};
  margin-left: 0.5rem;

  ${breakpoints.large} {
    font-size: ${typography.web.desc};
  }
`

export const MarkerSubTitle = styled.span`
  color: ${palette.textSecondary};
  font-size: ${typography.mobile.desc};
  margin: 1rem 0;

  ${breakpoints.large} {
    font-size: ${typography.web.desc};
  }
`

export const MarkerButton = styled.button`
  background-color: ${palette.main};
  color: ${palette.white};
  border-radius: 1rem;
  padding: 0.5rem 0;
  font-size: ${typography.mobile.desc};

  ${breakpoints.large} {
    font-size: ${typography.web.desc};
  }
`
