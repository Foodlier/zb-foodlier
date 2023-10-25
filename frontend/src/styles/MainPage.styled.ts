import styled from 'styled-components'
import { breakpoints, typography, weight } from '../constants/Styles'

export const SlickContainer = styled.section`
  margin-top: 3rem;
  ${breakpoints.large} {
    margin-top: 6rem;
  }
`

export const Tit = styled.p`
  font-size: ${typography.mobile.subTitle};
  font-weight: ${weight.subTitle};
  padding: 0 2%;

  ${breakpoints.large} {
    font-size: ${typography.web.subTitle};
  }
`

export const WebSearchWrapper = styled.div`
  display: none;

  ${breakpoints.large} {
    display: block;
  }
`

export const WebSearchContainer = styled.div`
  padding: 0 5%;
`

export const RecipeContainer = styled.section`
  margin-bottom: 5rem;

  ${breakpoints.large} {
    margin-bottom: 10rem;
  }
`
export const RecipeTit = styled.section`
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-right: 1rem;
`
export const RecipeList = styled.section`
  flex-direction: column;
  display: flex;
  padding: 0 1rem;
  margin-top: 2rem;

  ${breakpoints.large} {
    flex-direction: row;
    padding: 0 0.5rem;
    justify-content: center;
    margin-top: 3rem;
    justify-content: space-around;
  }
`
export const ChefContainer = styled.section`
  padding-bottom: 20rem;

  ${breakpoints.large} {
    padding-bottom: 10rem;
  }
`
export const ChefTit = styled.section``
export const ChefList = styled.section`
  display: flex;
  flex-wrap: wrap;
  margin: 2rem 1rem 0;
`

export const Button = styled.button``
