import styled from 'styled-components'
import {
  palette,
  breakpoints,
  typography,
  weight,
} from '../../../constants/Styles'

export const IngredientsContainer = styled.section`
  width: 100%;
  margin-bottom: 5rem;
`

export const MainTit = styled.h1`
  width: 100%;
  font-size: ${typography.mobile.mainTitle};
  font-weight: 800;
  padding: 0.5rem 2rem;
  margin: 2rem 0 0;
  border-bottom: 0.2rem solid ${palette.divider};

  ${breakpoints.large} {
    font-size: ${typography.web.mainTitle};
    margin-top: 0;
  }
`
export const IngredientsWrap = styled.ul`
  width: 100%;
  padding: 0;
  display: flex;
  flex-direction: column;

  ${breakpoints.large} {
    margin-top: 0;
    flex-direction: row;
    width: 100%;
    flex-wrap: wrap;
  }
`

export const IngredientsBox = styled.li`
  display: flex;
  justify-content: space-between;
  padding: 1rem 2rem 1rem;
  border-bottom: 0.1rem solid ${palette.divider};
  font-size: ${typography.mobile.content};

  ${breakpoints.large} {
    width: 50%;
    font-size: ${typography.web.content};
  }
`

export const CountWrap = styled.div`
  display: flex;
  color: ${palette.textSecondary};
`

export const Name = styled.p`
  font-size: ${weight.subTitle};
`

export const Count = styled.p``
export const Unit = styled.p``
